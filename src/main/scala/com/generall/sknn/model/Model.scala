package com.generall.sknn.model

import com.generall.sknn.model.storage.elements.BaseElement

import scala.collection.mutable

/**
  * Created by generall on 06.08.16.
  */
class Model[T <: BaseElement, N <: SkNNNode[T]](val nodeFac: (String) => N) extends Serializable{

  val nodes = new mutable.HashMap[String, N]()

  def getNode(label: String): Option[N] = nodes.get(label)

  def getOrCreateNode(label: String): N = getNode(label) match {
    case None =>
      val node: N = nodeFac(label)
      nodes(label) = node
      node
    case Some(x) => x
  }

  def labels = nodes.keySet

  val initNode: N = nodeFac(Model.INIT_LABEL)

  val endNode: N = nodeFac(Model.END_LABEL)

  nodes(Model.INIT_LABEL) = initNode
  nodes(Model.END_LABEL) = endNode

  def processSequence(sequence: List[T]) = {
    var currentNode = initNode
    sequence.foreach(element => {
      val nextLabel = element.label
      val nextNode = getOrCreateNode(nextLabel)
      nextNode.output ++= element.output
      currentNode.addElement(element, nextLabel)
      currentNode.addLink(nextNode)
      currentNode = nextNode
    })
    currentNode.addLink(endNode)
  }

  def isConnected(label1: String, label2: String): Boolean = {
    val node1 = nodes.get(label1)
    return node1 match {
      case None => false
      case Some(x) => x.hasLink(label2)
    }
  }
}

object Model{
  val INIT_LABEL = "init"
  val END_LABEL = "end"
}