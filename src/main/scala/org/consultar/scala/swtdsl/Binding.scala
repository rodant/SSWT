package org.consultar.scala.swtdsl

import scala.reflect.BeanProperty
import _root_.scala.collection.mutable.ListBuffer

import org.eclipse.swt.SWT
import org.eclipse.swt.widgets._
import org.eclipse.core.databinding.beans.PojoObservables
import org.eclipse.core.databinding.DataBindingContext
import org.eclipse.core.databinding.observable.{IChangeListener, IStaleListener, IDisposeListener, Realm}
import org.eclipse.core.databinding.observable.value.{IObservableValue, IValueChangeListener}
import org.eclipse.jface.databinding.swt.{SWTObservables, ISWTObservableValue}

trait Binding {

  private lazy val dbc: DataBindingContext = new DataBindingContext()
  
  protected def emptyBinding() = (c: Control) => dbc

  protected def bind[T <: Any](setter: T => Unit, getter: () => T)(target: Control): DataBindingContext = {
    val controlValue = target match {
      case t: Text =>
        SWTObservables.observeText(target, SWT.Modify)
      case _ =>
        SWTObservables.observeSelection(target)
    }
    val property = new Property[T](setter, getter)
    dbcBind(controlValue, new ObservableProperty(property))
  }
  
  private def dbcBind(controlValue: ISWTObservableValue, modelValue: IObservableValue) = {
    dbc.bindValue(controlValue, modelValue, null, null)
    dbc
  }
  
  protected class Property [T](val setter: T => Unit, val getter: () => T)
  
  protected var bindings = Map[Control, Control => DataBindingContext]()

  protected def setupBindings() {
    bindings.foreach(binding => binding._2(binding._1))
  }

  private class ObservableProperty[T <: Any](val property: Property[T]) extends IObservableValue {
	private object Bean {
	  def setProperty(v: T) = property.setter(v)
	  def getProperty() = property.getter()
	}

    private val delegate = PojoObservables.observeValue(Bean, "property")
    
    override def addValueChangeListener(listener: IValueChangeListener) = delegate.addValueChangeListener(listener)
    
    override def removeValueChangeListener(listener: IValueChangeListener) = delegate.removeValueChangeListener(listener)
    
    override def addChangeListener(listener: IChangeListener) = delegate.addChangeListener(listener)
    
    override def removeChangeListener(listener: IChangeListener) = delegate.removeChangeListener(listener)
    
    override def addStaleListener(listener: IStaleListener) = delegate.addStaleListener(listener)
    
    override def removeStaleListener(listener: IStaleListener) = removeStaleListener(listener)

    override def setValue(value: Object) = delegate.setValue(value)
    
    override def getValue(): Object = delegate.getValue

    override def getValueType: Object = delegate.getValueType
    
    override def getRealm(): Realm = delegate.getRealm
    
    override def dispose() = delegate.dispose()

    override def isStale() = delegate.isStale
    
    override def isDisposed() = delegate.isDisposed
    
    override def addDisposeListener(listener: IDisposeListener) = delegate.addDisposeListener(listener)
    
    override def removeDisposeListener(listener: IDisposeListener) = delegate.removeDisposeListener(listener)
  }
}
