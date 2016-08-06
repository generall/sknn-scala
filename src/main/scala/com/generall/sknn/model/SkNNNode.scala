package com.generall.sknn.model

import com.generall.sknn.model.storage.elements.BaseElement

/**
  * Created by generall on 02.08.16.
  */
trait SkNNNode[T <: BaseElement] extends Serializable{

  /**
    * Label of node, unique for each node in model
    */
  val label:String

  /**
    * Generated output of node
    */
  val output: scala.collection.mutable.Set[String]

  /**
    * Amount of nearest neighbours to search
    */
  val k:Int

  def addElement(element: T, label: String )

  def calcDistances(element: T): List[(SkNNNode[T], Double)]

  def addLink(other: SkNNNode[T])

  def hasLink(otherLabel: String): Boolean

  def removeLink(otherLabel: String)

  def getIncommingNodes: List[SkNNNode[T]]

  def getOutgoingNodes: List[SkNNNode[T]]

  def addBackLink(other: SkNNNode[T])

  def removeBackLink(otherLabel: String)

}
