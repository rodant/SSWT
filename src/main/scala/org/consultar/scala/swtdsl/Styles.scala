package org.consultar.scala.swtdsl

import org.eclipse.swt._

trait Styles {
	trait Style[T] {
	  val value: Int
	  def |(other: T): T
	}

	sealed class ShellStyle(v: Int) extends Style[ShellStyle]  {
	  override val value = v
	  override def |(other: ShellStyle): ShellStyle = new ShellStyle(this.value | other.value)
	}

	case object Border extends ShellStyle(SWT.BORDER)
	case object Close extends ShellStyle(SWT.CLOSE)
	case object Min extends ShellStyle(SWT.MIN)
	case object Max extends ShellStyle(SWT.MAX)
	case object Resize extends ShellStyle(SWT.RESIZE)
	case object Title extends ShellStyle(SWT.TITLE)
	case object NoTrim extends ShellStyle(SWT.NO_TRIM)
	case object ShellTrim extends ShellStyle(SWT.SHELL_TRIM)
	case object DialogTrim extends ShellStyle(SWT.DIALOG_TRIM)
//	case object Modeless extends ShellStyle(SWT.MODELESS)
//	case object PrimaryModal extends ShellStyle(SWT.PRIMARY_MODAL)
	case object ApplicationModal extends ShellStyle(SWT.APPLICATION_MODAL)
//	case object SystemModal extends ShellStyle(SWT.SYSTEM_MODAL)
}