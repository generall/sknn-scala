package ml.generall.sknn.model.storage.common

import scala.collection.mutable

class TupleOrdering[T] extends Ordering[(T, Double)]{
  override def compare(x: (T, Double), y: (T, Double)): Int =  x._2 compare y._2
}

/**
  * Created by generall on 06.08.16.
  */
class FixedLengthPriorityQueue[T](implicit x: Ordering[(T, Double)] = null) extends mutable.PriorityQueue[(T, Double)](){

  override val ord = new TupleOrdering[T]

  var sz = 1

  def put(el: T, priority: Double) = {
    this += ((el, priority))
    if(this.size > sz) this.dequeue()
  }
}
