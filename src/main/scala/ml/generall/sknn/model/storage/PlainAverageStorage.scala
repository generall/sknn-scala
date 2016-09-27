package ml.generall.sknn.model.storage

/**
  * Created by generall on 07.08.16.
  */
class PlainAverageStorage[T](distFoo: (T,T) => Double) extends PlainStorage[T](distFoo) {
  override def getMinDistance(element: T, n: Int): Double = {
    val closest = getClosestN(element, n)
    closest.foldLeft(0.0)((sum, pair) => sum + pair._2) / closest.size
  }
}
