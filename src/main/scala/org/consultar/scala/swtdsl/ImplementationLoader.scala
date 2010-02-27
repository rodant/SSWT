package org.consultar.scala.swtdsl

object ImplementationLoader {
  def newInstance(clazz: Class[_]) = clazz.getClassLoader.loadClass(clazz.getName + "Impl").newInstance()
}
