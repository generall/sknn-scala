package com.generall.sknn.model.storage

import com.generall.sknn.model.storage.common.FixedLengthPriorityQueue

/**
  * Created by generall on 06.08.16.
  */
abstract class PlainStorage[T](distFoo: (T,T) => Double) extends NodeStorage[T]{

  var data: List[T] = Nil

  override def getClosestN(element: T, n: Int): List[(T, Double)] = {
    val pq = new FixedLengthPriorityQueue[T]()
    pq.sz = n
    data.foreach(storedElement => {
      pq.put(storedElement, distFoo(storedElement, element))
    })
    pq.dequeueAll.reverse.toList
  }

  override def removeElement(element: T): Unit = throw new NotImplementedError()

  override def addElement(element: T): Unit = data = element :: data
}
