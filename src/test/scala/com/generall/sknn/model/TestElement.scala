package com.generall.sknn.model

import com.generall.sknn.model.storage.elements.BaseElement

/**
  * Created by generall on 06.08.16.
  */
class TestElement(_label: String) extends BaseElement{
  override val label : String = _label
  override var output: Set[String] = Set()
}
