import scala.collection.mutable

trait A{
  def a: Int
}

class B extends A{
  override def a: Int = 7
}

val map = new mutable.HashMap[String, A ]()

map("7") = new B()

map("7").a

val map1 = new mutable.HashMap[Int, Int]()

map1(1) = 4
map1(2) = 8

map1.map(x => (x, 1) ).toList


List(1,2,3,4).foldLeft(1)(_ + _)

