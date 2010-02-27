package org.consultar.scala.swtdsl

import java.io.InputStream
import org.eclipse.swt.graphics.Image

trait GraphicsFacade {
  def getImage(path: String): Image
  def getImage(in: InputStream): Image
}

object GraphicsFacade {
	val impl: GraphicsFacade = ImplementationLoader.newInstance(getClass).asInstanceOf[GraphicsFacade]
	def getImage(path: String): Image = impl.getImage(path)
	def getImage(in: InputStream): Image = impl.getImage(in)  
}