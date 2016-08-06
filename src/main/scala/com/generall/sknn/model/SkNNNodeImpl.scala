package com.generall.sknn.model

import com.generall.sknn.model.storage.NodeStorage
import com.generall.sknn.model.storage.elements.BaseElement

import scala.collection.mutable

/**
  * Created by generall on 06.08.16.
  */
class SkNNNodeImpl[T <: BaseElement, S <: NodeStorage[T]](_label: String, _k: Int)(storageFac: () => S) extends SkNNNode[T]{

  val forwardMap = new mutable.HashMap[String, SkNNNode[T]]()
  val backwardMap = new mutable.HashMap[String, SkNNNode[T]]()


  val storages = new mutable.HashMap[String, S]()

  override val label: String = _label

  override def calcDistances(element: T): Map[SkNNNode[T], Double] = {
    storages.map(pair => pair match {
      case (linkLabel, linkStorage) => forwardMap(linkLabel) -> linkStorage.getMinDistance(element, k)
    }).toMap
  }

  override def removeLink(otherLabel: String): Unit = {
    forwardMap.remove(otherLabel) match {
      case Some(x) => x.removeBackLink(label)
      case None =>
    }
  }

  override def getIncommingNodes: List[SkNNNode[T]] = backwardMap.values.toList

  override def addElement(element: T, label: String): Unit = storages.getOrElseUpdate(label, storageFac()).addElement(element)

  override def getOutgoingNodes: List[SkNNNode[T]] = forwardMap.values.toList

  override def addLink(other: SkNNNode[T]): Unit = {
    forwardMap(other.label) = other
    other.addBackLink(this)
  }

  override def hasLink(otherLabel: String): Boolean = forwardMap.contains(otherLabel)

  override val k: Int = _k
  override val output: mutable.Set[String] = new mutable.HashSet[String]()

  override def addBackLink(other: SkNNNode[T]): Unit = backwardMap(other.label) = other

  override def removeBackLink(otherLabel: String): Unit = backwardMap.remove(otherLabel)
}
