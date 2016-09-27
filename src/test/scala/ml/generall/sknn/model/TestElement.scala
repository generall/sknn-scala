package ml.generall.sknn.model

import ml.generall.sknn.model.storage.elements.BaseElement

/**
  * Created by generall on 06.08.16.
  */
class TestElement(_label: String) extends BaseElement{
  override var label : String = _label
  override var output: Set[String] = Set()

  var value: Double = 0.0
}
