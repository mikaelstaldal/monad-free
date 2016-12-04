package nu.staldal.monadfree.db

import nu.staldal.monadfree.{Evaluator, MonadFree}

import scala.collection.{immutable, mutable}


sealed trait DbOp
case class GetOp(key: String) extends DbOp
case class PutOp(key: String, value: String) extends DbOp
case object ListOp extends DbOp

class Db(val accumulator: immutable.Seq[DbOp] = Vector.empty) extends MonadFree[DbOp] {
  def get(key: String): Db = new Db(accumulator :+ GetOp(key))
  def put(key: String, value: String): Db = new Db(accumulator :+ PutOp(key, value))
  def list(): Db = new Db(accumulator :+ ListOp)
}

class PrintEvaluator extends Evaluator[DbOp, Unit] {
  override def apply(a: DbOp): Unit = println(a)

  override def result: Unit = ()
}

class MemoryEvaluator extends Evaluator[DbOp, Map[String, String]] {
  val db: mutable.Map[String, String] = mutable.Map.empty

  override def apply(a: DbOp): Unit = a match {
    case GetOp(key) =>
      db.get(key)

    case PutOp(key, value) =>
      db.put(key, value)

    case ListOp =>
      db.toSeq
  }

  override def result: Map[String, String] = db.toMap
}

object Example extends App {
  println("Start")

  val db: Db = new Db()
    .put("A", "foo")
    .get("A")
    .put("B", "a")
    .list()

  println("Run print")
  db.run(new PrintEvaluator)

  println("Run memory: " + db.run(new MemoryEvaluator))

  println("End")
}
