package org.consultar.scala.swtdsl

import org.eclipse.swt._
import layout._
import widgets._

trait Layouts {
  private class GridCell(
    val span: Int => Unit, 
    val align: Int => Unit, 
    val grabExcessSpace: Boolean => Unit, 
    val sizeHint: Int => Unit
  )

  def rowLayout(setups: (RowLayout => Unit)*)(composite: Composite): RowLayout = {
  	val layout = new RowLayout
    setups.foreach(_(layout))
  	composite.setLayout(layout)
  	layout
  }
  
  def gridLayout(setups: (GridLayout => Unit)*)(composite: Composite): GridLayout = {
    val layout = new GridLayout
    setups.foreach(_(layout))
    composite.setLayout(layout)
    layout
  }
  
  def horizontal(settings: GridCell => Unit*)(target: Control): Unit = {
    val data = target.getLayoutData() match {
      case x: GridData => x
      case _ => new GridData
    }
    val cell = new GridCell(data.horizontalSpan=_,
                            data.horizontalAlignment=_,
                            data.grabExcessHorizontalSpace=_,
                            data.widthHint=_)
    settings foreach(_(cell))
    target.setLayoutData(data)
  }

  def vertical(settings: GridCell => Unit*)(target: Control) = {
    val data = target.getLayoutData() match {
      case x: GridData => x
      case _ => new GridData
    }
    val cell = new GridCell(data.verticalSpan=_,
                            data.verticalAlignment=_,
                            data.grabExcessVerticalSpace=_, 
                            data.heightHint=_)
    settings foreach(_(cell))
    target.setLayoutData(data)
  }

  def grabExcessSpace(target:GridCell) = target.grabExcessSpace(true)

  def fill(target:GridCell) = target.align(SWT.FILL)
  
  def beginning(target: GridCell) = target.align(SWT.BEGINNING)
  
  def end(target: GridCell) = target.align(SWT.END)

  def columns(n: Int)(layout: GridLayout) = layout.numColumns = n

  def fillLayout(setups: (FillLayout=>Unit)*)(composite: Composite) {
    val l = new FillLayout()
    setups.foreach(_(l))
    composite.setLayout(l)
  }

  def horizontal(layout: FillLayout) {
    layout.`type` = SWT.HORIZONTAL
  }

  def horizontal(layout: RowLayout) {
    layout.`type` = SWT.HORIZONTAL
  }

  def vertical(layout: FillLayout) {
    layout.`type` = SWT.VERTICAL
  }

  def vertical(layout: RowLayout) {
    layout.`type` = SWT.VERTICAL
  }

  def margins(size: Int)(layout: Layout) = layout match {
    case layout: FillLayout =>
      layout.marginWidth = size
	  layout.marginHeight = size
    case layout: GridLayout =>
      layout.marginWidth = size
	  layout.marginHeight = size
    case layout: RowLayout =>
      layout.marginWidth = size
      layout.marginHeight = size
  }
}