package com.generall.sknn.model.storage.common

/**
  * Taken from https://gist.github.com/nicl/5620254
  */
class UniSet[A] extends Set[A] {
  def contains(key: A) = true
  def iterator = throw new UnsupportedOperationException()
  def +(elem: A) = this
  def -(elem: A) = throw new UnsupportedOperationException()
  override def toString: String = "UniversalSet"
}