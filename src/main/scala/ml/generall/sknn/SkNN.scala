package ml.generall.sknn

import ml.generall.sknn.model.storage.elements.BaseElement
import ml.generall.sknn.model.{Model, SkNNNode}

import scala.collection.mutable

/**
  * Created by generall on 02.08.16.
  */
class SkNN[T <: BaseElement, N <: SkNNNode[T]](model: Model[T, N]) {

  type TNode = SkNNNode[T]

  def constructDistanceMap = new scala.collection.mutable.HashMap[TNode, Double].withDefaultValue(Double.PositiveInfinity)

  def constructPathMap = new scala.collection.mutable.HashMap[TNode, TNode]

  def viterbi(seq: List[T])(filterNodes: (T, TNode) => Boolean): (List[mutable.Map[TNode, Double]], List[mutable.HashMap[TNode, TNode]]) = {
    var v = List(constructDistanceMap)
    var path: List[scala.collection.mutable.HashMap[TNode, TNode]] = Nil

    v.head(model.initNode) = 0.0

    val maxIterations = seq.size

    seq.zipWithIndex.foreach(pair => {
      val (element, idx) = pair
      val prev = v.head
      val currentDistances = constructDistanceMap
      val currentPath = constructPathMap


      // Iterate all reached at step k-1 nodes
      prev.foreach(pair => {
        val (node, dist) = pair
        if (dist != Double.PositiveInfinity) {
          val outgoingNodes = node.getOutgoingNodes.filter(x => filterNodes(element, x))
          outgoingNodes.foreach(nextNode => {
            val localDistance = node.calcDistance(element, nextNode)
            if (localDistance != Double.PositiveInfinity) {
              val d = dist + localDistance
              if (d < currentDistances(nextNode)) {
                currentDistances(nextNode) = d
                currentPath(nextNode) = node
              }
            }
          })
        }
      })
      v = currentDistances :: v
      path = currentPath :: path
    })

    // do not calc distances for paths no ended with +end_node+
    path.head.keySet.foreach(lastNode => {
      if (!lastNode.hasLink(Model.END_LABEL)) {
        v.last(lastNode) = Double.PositiveInfinity
      }
    })

    (v, path)
  }

  def extractPath(v: List[mutable.Map[TNode, Double]], path: List[mutable.Map[TNode, TNode]]): (List[TNode], Double) = {
    var (node, score) = v.head.minBy(pair => pair._2)
    var res: List[TNode] = List()
    v.head.remove(node)
    path.foreach(pathMap => {
      res = node :: res
      val nextNode = pathMap(node)
      node = nextNode
    })
    (res, score)
  }

  def tag(seq: List[T], closestCount: Int)(filterNodes: (T, TNode) => Boolean): List[(List[TNode], Double)] = {
    val (v, path) = viterbi(seq)(filterNodes)
    val res = (1 to closestCount).map(_ => extractPath(v, path)).toList
    res
  }


}
