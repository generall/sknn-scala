package com.generall.sknn.model

import org.scalatest.{BeforeAndAfterEach, FunSuite}

/**
  * Created by generall on 06.08.16.
  */
class SkNNNodeImplTest extends FunSuite with BeforeAndAfterEach {

  var node : SkNNNode[TestElement] = null

  override def beforeEach() {
    node = new SkNNNodeImpl[TestElement, TestNodeStorage]("test", 2)(() => new TestNodeStorage())
  }

  test("testCalcDistances") {

    val n1 = new TestSkNNNode[TestElement]("n1")
    val n2 = new TestSkNNNode[TestElement]("n2")

    node.addLink(n1)
    node.addLink(n2)

    val e1 = new TestElement("1")
    val e2 = new TestElement("22")
    val e3 = new TestElement("333")
    val e4 = new TestElement("4444")
    val e5 = new TestElement("55555")
    node.addElement(e1, "n1")
    node.addElement(e2, "n1")
    node.addElement(e3, "n2")
    node.addElement(e4, "n2")
    node.addElement(e5, "n2")

    val testEl = new TestElement("test")

    val dists = node.calcDistances(testEl)

    assert(dists.size == 2)
    assert( (dists(n2) - 0.5).abs < 0.00001)
    assert( (dists(n1) - 2.5).abs < 0.00001)
  }

  test("testRemoveLink") {
    val node2 = new SkNNNodeImpl[TestElement, TestNodeStorage]("test2", 2)(() => new TestNodeStorage())
    node.addLink(node2)
    node.removeLink("test2")

    assert(node2.getIncomingNodes.isEmpty)
    assert(node.getOutgoingNodes.isEmpty)
  }

  test("testAddLink") {
    val node2 = new SkNNNodeImpl[TestElement, TestNodeStorage]("test2", 2)(() => new TestNodeStorage())
    node.addLink(node2)

    assert(node2.getIncomingNodes.head == node)
    assert(node2.getIncomingNodes.size == 1)
    assert(node.getOutgoingNodes.head == node2)
  }

}
