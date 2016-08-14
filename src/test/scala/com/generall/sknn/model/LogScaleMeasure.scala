package com.generall.sknn.model

/**
  * Created by generall on 09.08.16.
  */
class LogScaleMeasure(a: Double) {

  def compare(x: Double, y: Double) = Math.log((x - y).abs + 1) / Math.log(a)

}
