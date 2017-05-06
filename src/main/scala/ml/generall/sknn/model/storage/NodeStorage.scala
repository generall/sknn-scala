package ml.generall.sknn.model.storage

/**
  * Created by generall on 06.08.16.
  */
trait NodeStorage[T] {

  def getClosestN(element: T, n: Int): List[(T, Double)] // return element and distance

  def getMinDistance(element: T, n: Int): Double

  def addElement(element: T)

  def removeElement(element: T)
}

trait NodeStorageFactory[T] {
  def create: NodeStorage[T]
}