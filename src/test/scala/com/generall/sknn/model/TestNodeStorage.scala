package com.generall.sknn.model

import com.generall.sknn.model.storage.NodeStorage

import scala.collection.mutable.ListBuffer

/**
  * Created by generall on 06.08.16.
  */
class TestNodeStorage extends NodeStorage[TestElement]{

  val array = new ListBuffer[TestElement]

  override def getClosestN(element: TestElement, n: Int): List[(TestElement, Double)] = array.map(el => {
    val dist = (element.label.length - el.label.length).abs
    (el, dist.toDouble)
  }).sortBy(pair => pair._2).take(n).toList

  override def removeElement(element: TestElement): Unit = ???

  override def addElement(element: TestElement): Unit = array += element

  override def getMinDistance(element: TestElement, n: Int): Double = {
    val closest = getClosestN(element, n)
    closest.foldLeft(0.0)((sum, x) => sum + x._2) / closest.size
  }
}
