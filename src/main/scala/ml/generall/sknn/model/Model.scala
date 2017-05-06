package ml.generall.sknn.model

import ml.generall.sknn.model.storage.{NodeStorage, NodeStorageFactory}
import ml.generall.sknn.model.storage.elements.BaseElement

import scala.collection.mutable

/**
  * Main class for SkNN model
  *
  *
  * T - class for storing datum
  * N - implement storage inside single node
  *
  */
class Model[T <: BaseElement, N <: SkNNNode[T]](val nodeFac: String => N) extends Serializable {

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

  def processSequenceImpl[K](sequence: List[K])(elementToLabels: (K => List[(String, T)])) = {
    var currentNodeList = List(initNode)
    sequence.foreach(element => {
      val nextLabelsList = elementToLabels(element)
      val nextDataList = nextLabelsList.map(x => (getOrCreateNode(x._1), x._2))
      nextDataList.foreach(nextData => nextData._1.output ++= nextData._2.output)
      for (
        currentNode <- currentNodeList;
        nextData <- nextDataList
      ) {
        val (nextNode, subelement) = nextData
        currentNode.addElement(subelement, nextNode.label)
        currentNode.addLink(nextNode)
      }
      currentNodeList = nextDataList.map(_._1)
    })
    currentNodeList.foreach(_.addLink(endNode))
  }

  def processSequence[K <: T](sequence: List[K]) = {
    def foo(x: K): List[(String, T)] = List((x.label, x))

    processSequenceImpl(sequence)(foo)
  }

  def isConnected(label1: String, label2: String): Boolean = {
    val node1 = nodes.get(label1)
    return node1 match {
      case None => false
      case Some(x) => x.hasLink(label2)
    }
  }
}

object Model {
  val INIT_LABEL = "init"
  val END_LABEL = "end"

  def createModel[T <: BaseElement](k: Int = 1, nodeFactory: NodeStorageFactory[T]) =
    new Model[T, SkNNNode[T]](label => new SkNNNodeImpl[T](label, k)(() => nodeFactory.create))
}