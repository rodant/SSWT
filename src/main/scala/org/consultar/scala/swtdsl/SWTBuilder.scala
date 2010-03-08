package org.consultar.scala.swtdsl

import org.eclipse.swt._
import org.eclipse.swt.graphics.Image
import org.eclipse.swt.layout._
import org.eclipse.swt.widgets._
import org.eclipse.swt.events._
import org.eclipse.core.databinding.observable.Realm
import org.eclipse.core.databinding.DataBindingContext
import org.eclipse.jface.databinding.swt.SWTObservables

import java.io.InputStream

trait SWTBuilder extends Layouts with Styles with Binding {
  private[this] val display: Display = Display.getDefault()
  private[this] var currentParent: Composite = _

  final def getCurrentParent = currentParent

  def run() {
    def withDisplaysRealm[T >: Null](block: => T): T = {
      var ret: T = null
      Realm.runWithDefault(
        SWTObservables.getRealm(display), new Runnable {
          def run() = ret = block
        }
      )
      ret
    }

    val shell = currentParent.getShell
    withDisplaysRealm {
      setupBindings()
      while(!shell.isDisposed)
        if(!display.readAndDispatch()) display.sleep()
    }
    display.dispose()
  }
  
  private def setupShell(shell: Shell, block: => Unit, setups: (Shell => Unit)*): Shell = {
    shell.setLayout(new FillLayout)
    currentParent = shell
    setups.foreach(_(shell))
    block
    shell.pack()
    shell.open()
    shell
  }

  def shell(setups: (Shell => Unit)*)(block: => Unit): Shell = {
    val shell = new Shell(SWT.SHELL_TRIM)
    setupShell(shell, block, setups: _*)
  }

  def shell(style: ShellStyle)(setups: (Shell => Unit)*)(block: => Unit): Shell = {
    val shell = new Shell(style.value)
    setupShell(shell, block, setups: _*)
  }

  def title(t: String)(titled: {def setText(t: String)}) = titled.setText(t)
  def text = title _
  def icon(image: Image)(target: {def setImage(image: Image)}) = target.setImage(image);
  def icon(image: String)(target: {def setImage(image: Image)}) = target.setImage(GraphicsFacade.getImage(image))
  def icon(image: InputStream)(target: {def setImage(image: Image)}) = target.setImage(GraphicsFacade.getImage(image))
  
  def byClose(handler: Event => Unit)(s: Shell) = s.addListener(SWT.Close, 
                                                                new Listener {
                                                                  def handleEvent(e: Event) = handler(e)
                                                                })

  private def setupContainer[T <: Composite](container: T, setups: (T => Unit)*)(block: => Unit): T = {
    currentParent = container
    container.setLayout(new FillLayout)
    setups.foreach(_(container))
    block
    currentParent = container.getParent
    container 
  }

  def composite(setups: (Composite => Unit)*)(block: => Unit): Composite = {
    setupContainer(new Composite(currentParent, SWT.NONE), setups: _*)(block)
  }
  
  def group(setups: (Group => Unit)*)(block: => Unit): Group = {
    setupContainer(new Group(currentParent, SWT.NONE), setups: _*)(block)
  }

  def label(setups: Label => Unit*): Label = {
    val label = new Label(currentParent, SWT.NONE)
    setups.foreach(_(label))
    label
  }
  
  private def setupControl[T <: Control](c: T, binding: Control => DataBindingContext, setups: T => Unit*): T = {
    setups.foreach(_(c))
    bindings += c -> binding
    c
  }

  def edit(binding: Control => DataBindingContext, setups: Text => Unit*): Text = {
    setupControl(new Text(currentParent, SWT.BORDER), binding, setups: _*)
  }

  def radioButton(binding: Control => DataBindingContext, setups: Button => Unit*): Button = {
    setupControl(new Button(currentParent, SWT.RADIO), binding, setups: _*)
  }
  
  def checkBox(binding: Control => DataBindingContext, setups: Button => Unit*): Button = {
    setupControl(new Button(currentParent, SWT.CHECK), binding, setups: _*)
  }
  
  implicit val dummySelectionHandler = (e: SelectionEvent) => ()

  def button(setups: Button => Unit*)(implicit handler: SelectionEvent => Unit): Button = {
    val button = new Button(currentParent, SWT.NONE)
    setups.foreach(_(button))
    button.addSelectionListener(
      new SelectionAdapter {
        override def widgetSelected(e: SelectionEvent) = handler(e)
      }
    )
    button
  }
  
  def spinner(binding: Control => DataBindingContext, setups: Spinner => Unit*) = {
    setupControl(new Spinner(currentParent, SWT.NONE), binding, setups: _*)
  }
  
  def selected(i: Int)(s: Spinner) = s.setSelection(i)
  
  def selected(widget: {def setSelection(b: Boolean)}) = widget.setSelection(true)
}