package ml.generall.sknn.model.storage.elements

/**
  * Created by generall on 02.08.16.
  */
trait BaseElement extends Serializable{
  var label : String
  var output: Set[String]
}
