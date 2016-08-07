package com.generall.sknn.model.storage

import org.scalatest.{BeforeAndAfterEach, FunSuite}

/**
  * Created by generall on 07.08.16.
  */
class PlainAverageStorageTest extends FunSuite with BeforeAndAfterEach {

  var storage: PlainAverageStorage[(Double, Double)] = null

  override def beforeEach() {
    storage = new PlainAverageStorage[(Double, Double)](
      (x, y) => Math.sqrt(Math.pow(x._1 - y._1, 2) + Math.pow(x._2 - y._2, 2))
    )

    storage.addElement((1.0, 2.0))
    storage.addElement((2.0, 0.0))
    storage.addElement((0.0, 2.0))
    storage.addElement((4.0, 2.0))
    storage.addElement((3.0, 3.0))
    storage.addElement((1.0, 1.0))
    storage.addElement((2.0, 4.0))
    storage.addElement((1.0, 2.0))

  }

  test("testGetClosestN") {

    val res = storage.getClosestN((1.3, 2.4), 3)

    assert(res.size == 3)

    assert( (res.head._2 - 0.5).abs < 0.0001 )

  }

  test("testGetMinDistance") {
    val res = storage.getMinDistance((1.3, 2.4), 3)

    assert( (res - 0.78).abs < 0.1 )
  }

}
