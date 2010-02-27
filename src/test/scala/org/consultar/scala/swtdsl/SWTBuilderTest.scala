package org.consultar.scala.swtdsl

import org.scalatest.matchers.{MustMatchers}

import org.junit.Test

import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.{Shell, Composite,  Layout, Control}
import org.eclipse.swt.layout.FillLayout

class SWTBuilderTest extends MustMatchers with SWTBuilder {
	var blockExecuted = false
	var setupExecuted = false
	val mocksetup = (s: Shell) => setupExecuted = s != null
	val mockblock = blockExecuted = true
	val mockCompositeSetup = (s: Composite) => setupExecuted = s != null

	private def checkComposite(c: Composite) {
	  c must not be (null)
      c.getLayout.getClass must be (classOf[FillLayout])
      setupExecuted must be (true)
      blockExecuted must be (true)
	}

	private def performShellTestWithStyle(style: Option[ShellStyle]): Shell = {
	  val s = style match {
	    case Some(shellStyle) => shell(shellStyle)(mocksetup) {mockblock}
	    case None => shell(mocksetup) {mockblock}
	  }
      checkComposite(s)
      getCurrentParent must be (s)
      s
	}
 
	@Test def testCreateShellWithoutStyle() {
		val shell = performShellTestWithStyle(None)
		// The result of getStyle must only contain (as bit set) the bits put in the Shell's constructor. 
	    // This is true at least for Linux gtk
        shell.getStyle & SWT.SHELL_TRIM must be (SWT.SHELL_TRIM)
        shell.close()
	}
 
	@Test def testCreateShellWithStyle() {
	  val shell = performShellTestWithStyle(Some(Border | Close))
	  // The result of getStyle must only contain (as bit set) the bits put in the Shell's constructor. 
	  // This is true at least for Linux gtk
      shell.getStyle & Border.value must be (Border.value)
      shell.getStyle & Close.value must be (Close.value)
      shell.getStyle & Min.value must be (0)
      shell.close()
	}
 
	private def performTestWithComposite(block: => Composite) {
	  shell() {
		  val currentParentBefore = getCurrentParent
		  val composite = block 
		  checkComposite(composite)
		  getCurrentParent must be (composite.getParent)
		  getCurrentParent must be (currentParentBefore)     
	  }.close()
	}

	@Test def testCreateComposite() {
	  performTestWithComposite {
	    composite(mockCompositeSetup) {mockblock}
	  }
	}
 
	@Test def testCreateGroup() {
	  performTestWithComposite {
	    group(mockCompositeSetup) {mockblock}
	  }
	}
 
	private def performInShell(block: => Unit) {
	  val s = shell() {
	    block
	  }
	  s.close()
	}

	private trait Texted {
		def getText(): String
	}
	
	private implicit def controlToTexted(c: Control): Texted = new Texted() {
		def getText() = c.getClass().getMethod("getText").invoke(c).asInstanceOf[String]
	}
	
	private def performTextedControlCheck(control: Control, expectedText: String): Control = {
	  control must not be (null)
	  control.getText must be (expectedText)
	  control
	}

	@Test def testCreateLabel() {
	  performInShell {
	    performTextedControlCheck(label(title("Title")), "Title")
	  }
	}

	val binding = emptyBinding()

	private def performTextedControlWithBindingTest(control: => Control, expectedText: String) {
	  performInShell {
	    val c = performTextedControlCheck(control, expectedText)
	    bindings must have size (1)
		bindings must contain key (c)
		bindings must contain value (binding)
	  }
	}

	@Test def testCreateTextField() {
		performTextedControlWithBindingTest(edit(binding, text("Title")), "Title")
	}
 
	@Test def testCreateRadioButton() {
	  performTextedControlWithBindingTest(radioButton(binding, title("Title")), "Title")
	}
 
	@Test def testCreateCheckBox() {
	  performTextedControlWithBindingTest(checkBox(binding, title("Title")), "Title")
	}
 
	@Test def testCreateButton() {
	  import org.eclipse.swt.widgets.{Listener, Event}
	  import org.eclipse.swt.events.SelectionEvent
	  performInShell {
	    performTextedControlCheck(button(title("Title")), "Title")
	    var mockHandlerCalled = false
	    val b = performTextedControlCheck(button(title("Title"))(_ => mockHandlerCalled = true), "Title")
	    //b.getListeners(SWT.Selection) must have size (1)// error only in RAP 1.2
	    b.notifyListeners(SWT.Selection, new Event)
	    mockHandlerCalled must be (true)
	  }
	}
 
	@Test def testCreateSpinner() {
	  performTextedControlWithBindingTest(spinner(binding), "0")
	}
}
