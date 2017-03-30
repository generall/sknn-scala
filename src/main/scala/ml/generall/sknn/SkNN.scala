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

  def viterbi(seq: List[T])(filterNodes: (T, TNode) => Boolean): (List[Map[TNode, Double]], List[Map[TNode, TNode]]) = {
    var v = List(
      Map[TNode, Double](
        model.initNode -> 0.0
      ).withDefaultValue(Double.PositiveInfinity)
    )

    var path: List[Map[TNode, TNode]] = Nil

    val maxIterations = seq.size

    seq.foreach(element => {
      val prev = v.head

      // Iterate all reached at step k-1 nodes

      val (currentDistances, currentPath) = prev
        .par
        .filter(_._2 != Double.PositiveInfinity)
        .flatMap {
          case (node, dist) =>
            node.getOutgoingNodes
              .filter(x => filterNodes(element, x))
              .map((node, dist, _))
        }
        .map {
          case (node, dist, nextNode) =>
            (node, nextNode, dist + node.calcDistance(element, nextNode))
        }
        .filter(_._3 != Double.PositiveInfinity)
        .groupBy(_._2)
        .map {
          case (node, list) =>
            val (from, to, minDist) = list.minBy(_._3)
            ((to, minDist), (to, from))
        }
        .unzip match {
        case (dists, prevs) =>
          (dists.toList.toMap, prevs.toList.toMap)
      }

      v = currentDistances :: v
      path = currentPath :: path
    })

    // do not calc distances for paths no ended with +end_node+
    val fixedHead = path.head.map {
      case (lastNode, _) =>
        if (lastNode.hasLink(Model.END_LABEL)) {
          lastNode -> v.head(lastNode)
        } else {
          lastNode -> Double.PositiveInfinity
        }
    }

    val _ :: vTail = v

    (fixedHead :: vTail, path)
  }

  def extractPath(v: List[Map[TNode, Double]], path: List[Map[TNode, TNode]]): (List[TNode], Double) = {
    var (node, score) = v.head.minBy(pair => pair._2)
    var res: List[TNode] = List()
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
