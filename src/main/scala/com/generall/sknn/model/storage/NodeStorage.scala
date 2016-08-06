package com.generall.sknn.model.storage

import com.generall.sknn.model.storage.elements.BaseElement

/**
  * Created by generall on 06.08.16.
  */
trait NodeStorage[T <: BaseElement] {

  def getClosestN(element: T, n: Int): List[(T, Double)] // return element and distance

  def getMinDistance(element: T, n: Int): Double

  def addElement(element: T)

  def removeElement(element: T)
}
