import java.io.{ByteArrayInputStream, ObjectInputStream, ByteArrayOutputStream, ObjectOutputStream}

import org.jgraph.graph.DefaultEdge
import org.jgrapht.graph.SimpleGraph

val g =
  new SimpleGraph[String, DefaultEdge](classOf[DefaultEdge])

val v1 = "ololo"
val v2 = "trololo"
val v3 = "v3"
val v4 = "v4"

// add the vertices
g.addVertex(v1)
g.addVertex(v2)
g.addVertex(v3)
g.addVertex(v4)

// add edges to create a circuit
g.addEdge(v1, v2)
g.addEdge(v2, v3)
g.addEdge(v3, v4)
g.addEdge(v4, v1)


val baos = new ByteArrayOutputStream
val oos = new ObjectOutputStream(baos)

oos.writeObject(g)

baos.toString



val ois = new  ObjectInputStream(new ByteArrayInputStream(baos.toByteArray))

val g2 = ois.readObject.asInstanceOf[SimpleGraph[String, DefaultEdge]]

g2.vertexSet()