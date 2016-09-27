package ml.generall.sknn.model

import ml.generall.sknn.model.storage.elements.BaseElement

import scala.collection.mutable

/**
  * Created by generall on 06.08.16.
  */
class TestSkNNNode[T <: BaseElement](_label: String) extends SkNNNode[T]{

  override val k = 1

  val map = new mutable.HashMap[String, SkNNNode[T]]()

  val data = new mutable.ListBuffer[T]

  override def addElement(element: T, label: String): Unit = data += element

  override def calcDistances(element: T): Map[SkNNNode[T], Double] = null

  override val label: String = _label
  override val output: mutable.Set[String] = new mutable.HashSet[String]()

  override def addLink(other: SkNNNode[T]): Unit = if(map.contains(other.label)) {
    if(map(other.label) != other){
      throw new RuntimeException("Wrong node linking")
    }
  } else map(other.label) = other

  override def removeLink(otherLabel: String): Unit = map.remove(otherLabel)

  override def getIncomingNodes: List[SkNNNode[T]] = Nil

  override def getOutgoingNodes: List[SkNNNode[T]] = map.values.toList

  override def hasLink(otherLabel: String): Boolean = map.contains(otherLabel)

  override def addBackLink(other: SkNNNode[T]): Unit = {}

  override def removeBackLink(otherLabel: String): Unit = {}

  override def calcDistance(element: T, node: SkNNNode[T]): Double = 1.0
}
