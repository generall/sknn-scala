package ml.generall.sknn.model.storage.common

import org.scalatest.FunSuite

/**
  * Created by generall on 07.08.16.
  */
class FixedLengthPriorityQueueTest extends FunSuite {

  test("testPut") {

    val pq = new FixedLengthPriorityQueue[String]()

    pq.sz = 3

    pq.put("a", 10)
    pq.put("b", 40)
    pq.put("c", 15)
    pq.put("d", 90)
    pq.put("e", 30)
    pq.put("f", 20)
    pq.put("9", 70)

    assert(pq.toList.map(_._1).toSet == Set("a", "c", "f"))

  }

}
