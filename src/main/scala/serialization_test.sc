import java.io._


def save[T <: Serializable](fname: String, serializable: T) = {
  val baos = new FileOutputStream(fname)
  val oos = new ObjectOutputStream(baos)
  oos.writeObject(serializable)
  oos.close()
}

def load[T](fname: String): T = {
  val ois = new  ObjectInputStream(new FileInputStream(fname))
  val res = ois.readObject.asInstanceOf[T]
  ois.close()
  res
}
