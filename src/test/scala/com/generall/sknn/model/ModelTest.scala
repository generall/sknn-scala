package com.generall.sknn.model

import org.scalatest.{BeforeAndAfterEach, FunSuite}

/**
  * Created by generall on 06.08.16.
  */
class ModelTest extends FunSuite with BeforeAndAfterEach {

  var model:Model[TestElement, TestSkNNNode[TestElement]] = null

  override def beforeEach() {
    model = new Model[TestElement, TestSkNNNode[TestElement]](label => new TestSkNNNode[TestElement](label))
  }

  test("testProcessSequence") {

    val seq = List(
      new TestElement("1"),
      new TestElement("2"),
      new TestElement("3"),
      new TestElement("2"),
      new TestElement("4")
    )

    val seq2 = List(
      new TestElement("a"),
      new TestElement("2"),
      new TestElement("b"),
      new TestElement("2"),
      new TestElement("c")
    )

    model.processSequence(seq)
    model.processSequence(seq2)

    assert(model.nodes.size == (4 + 3 + 2))

    assert(model.isConnected("init", "1"))
    assert(model.isConnected("1", "2"))
    assert(model.isConnected("2", "3"))
    assert(model.isConnected("3", "2"))
    assert(model.isConnected("2", "4"))
    assert(model.isConnected("4", "end"))
    assert(!model.isConnected("end", "4"))
    assert(!model.isConnected("4", "2"))

    assert(model.getOrCreateNode("2").getOutgoingNodes.size == 4)

  }

  test("testGetOrCreateNode") {

    val node = model.getOrCreateNode("test")

    assert(node.label == "test")

    assert(model.nodes.size == 3)

  }

}
