# SkNN implementation in Scala

SkNN is a metric algorithm for labeling\classification sequential data.
It accepts for classification any type of sequence elements with only condition:
you need to define distance function which can calculate distance between pair of elements.

Detailed description of SkNN could be found at [arXiv](https://arxiv.org/abs/1610.04718).

# Usage

First of all you need to implement class for you data derived from 
`BaseElement` trait.

Let's create `TestElement` class for our test data.
TestElement contains only single value of `Double`.
```$scala
class TestElement(_label: String) extends BaseElement{
  override var label : String = _label
  override var output: Set[String] = Set()

  var value: Double = 0.0
}
```

* `label` - is a class label of sequence element
* `output` - is an additional field for storing possible outputs of element (analogue to HMM outputs). Not actually used in this example
* `value` - element value with default initialization

Now we can define train and test sets for our example:

```$scala

/**
* Train sequences
*/
    val seq1 = List(
      elemCreator("l1", 1),
      elemCreator("l2", 100),
      elemCreator("l3", 10),
      elemCreator("l3", 11)
    )

    val seq2 = List(
      elemCreator("l1", 1),
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

/**
* Test sequences
*/

    val test1 = List(
      elemCreator(null, 1),
      elemCreator(null, 70),
      elemCreator(null, 0),
      elemCreator(null, 0)
    )
```

The next step is the creation of a `Model` object.
We will use simple distance measure which will calc 
absolute difference between values of pair of the elements:


```$scala
 val model = Model.createModel[TestElement](1, new PlainAverageStorageFactory((x, y) => (x.value - y.value).abs))

/**
* Learning of the model
*/
 model.processSequence(seq1)
 model.processSequence(seq2)
 model.processSequence(seq3)
```

* `TestElement` - is a type of our sequence elements
* `PlainAverageStorageFactory` - is a factory which will create searchable storage for our training elements. It is easy to create your oun storage for special implementation of searchable storage like KD-tree.
* `(x, y) => (x.value - y.value).abs)` - is our distance function

The next step is the creation of classifier object and performing labeling:

```$scala
    val sknn = new SkNN[TestElement](model)
    
    val labeling:Option[(List[SkNNNode[TestElement]], Double /* summary distance */)] = sknn.tag(test1)

    val res1 = labeling.get._1

    assert(res1.size == 4)

    assert(res1(0).label == "l1")
    assert(res1(1).label == "l2")
    assert(res1(2).label == "l3")
    assert(res1(3).label == "l3")
```

* `sknn.tag(test1)` - is a labeling function call for `test1` sequence
* `res1` contains sequence of SkNN nodes which can be used to lookup result labels.

# References

* SkNN detailed [description](https://arxiv.org/abs/1610.04718)
* Named Entity Linking with SkNN - [https://github.com/generall/SkNN-NER](https://github.com/generall/SkNN-NER)
* Ruby implementation of SkNN [https://github.com/generall/SkNN-ruby](https://github.com/generall/SkNN-ruby)