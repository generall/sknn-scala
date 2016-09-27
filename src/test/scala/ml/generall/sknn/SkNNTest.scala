package ml.generall.sknn

import ml.generall.sknn.model.storage.PlainAverageStorage
import ml.generall.sknn.model._
import org.scalatest.FunSuite

/**
  * Created by generall on 07.08.16.
  */
class SkNNTest extends FunSuite {

  def elemCreator(label: String, value: Double): TestElement  = {
    val e = new TestElement(label)
    e.value = value
    e
  }

  test("testViterbi") {

    val seq1 = List(
      elemCreator("l1", 1) ,
      elemCreator("l2", 100),
      elemCreator("l3", 10),
      elemCreator("l3", 11)
    )

    val seq2 = List(
      elemCreator("l1", 1) ,
      elemCreator("l4", 50),
      elemCreator("l5", 21),
      elemCreator("l5", 20)
    )

    val seq3 = List(
      elemCreator("l1", 1),
      elemCreator("l1", 2),
      elemCreator("l1", 3),
      elemCreator("l1", 4),
      elemCreator("l1", 5)
    )


    val test1 = List(
      elemCreator(null, 1) ,
      elemCreator(null, 70),
      elemCreator(null, 0),
      elemCreator(null, 0)
    )

    val test2 = List(
      elemCreator(null, 1) ,
      elemCreator(null, 80),
      elemCreator(null, 23),
      elemCreator(null, 22)
    )

    val model = new Model[TestElement, SkNNNode[TestElement]]( (label) => {
      new SkNNNodeImpl[TestElement, PlainAverageStorage[TestElement]](label, 1)( () => {
        new PlainAverageStorage[TestElement](
          (x, y) => (x.value - y.value).abs
        )
      })
    })

    model.processSequence(seq1)
    model.processSequence(seq2)
    model.processSequence(seq3)

    val sknn = new SkNN[TestElement, SkNNNode[TestElement]](model)

    val res1 = sknn.tag(test1, 1)( (_, _) => true).head._1
    val res2 = sknn.tag(test2, 1)( (_, _) => true).head._1

    assert(res1.size == 4)
    assert(res2.size == 4)

    assert(res1(0).label == "l1")
    assert(res1(1).label == "l2")
    assert(res1(2).label == "l3")
    assert(res1(3).label == "l3")

    assert(res2(0).label == "l1")
    assert(res2(1).label == "l4")
    assert(res2(2).label == "l5")
    assert(res2(3).label == "l5")

    val measure1 = new LogScaleMeasure(2)
    val measure2 = new LogScaleMeasure(10)


    /**
      * Creating model with different measurable functions
      */
    val modelLog1 = new Model[TestElement, SkNNNode[TestElement]]( (label) => {
      new SkNNNodeImpl[TestElement, PlainAverageStorage[TestElement]](label, 1)( () => {
        new PlainAverageStorage[TestElement](
          (x, y) => measure1.compare(x.value, y.value)
        )
      })
    })

    val modelLog2 = new Model[TestElement, SkNNNode[TestElement]]( (label) => {
      new SkNNNodeImpl[TestElement, PlainAverageStorage[TestElement]](label, 1)( () => {
        new PlainAverageStorage[TestElement](
          (x, y) => measure2.compare(x.value, y.value)
        )
      })
    })

    /**
      * Saving data to the models (aka learning)
      */

    modelLog1.processSequence(seq1)
    modelLog1.processSequence(seq2)
    modelLog1.processSequence(seq3)

    modelLog2.processSequence(seq1)
    modelLog2.processSequence(seq2)
    modelLog2.processSequence(seq3)

    val sknn1 = new SkNN[TestElement, SkNNNode[TestElement]](modelLog1)
    val sknn2 = new SkNN[TestElement, SkNNNode[TestElement]](modelLog2)

    val RES_COUNT = 2

    /**
      * Executing classification
      */
    val logRes1 = sknn1.tag(test1, RES_COUNT)( (_, _) => true)
    val logRes2 = sknn2.tag(test1, RES_COUNT)( (_, _) => true)

    assert(logRes1.size == RES_COUNT)
    assert(logRes2.size == RES_COUNT)

    val logResStates = logRes1.head._1

    assert(logResStates(0).label == "l1")
    assert(logResStates(1).label == "l1")
    assert(logResStates(2).label == "l1")
    assert(logResStates(3).label == "l1")

  }

}
