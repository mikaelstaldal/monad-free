package nu.staldal.monadfree.db

import nu.staldal.monadfree.{Evaluator, MonadFree}

import scala.collection.{immutable, mutable}


sealed trait DbOp
case class GetOp(key: String) extends DbOp
case class PutOp(key: String, value: String) extends DbOp

class Db(val accumulator: immutable.Seq[DbOp] = Vector.empty) extends MonadFree[DbOp] {
  def get(key: String): Db = new Db(accumulator :+ GetOp(key))
  def put(key: String, value: String): Db = new Db(accumulator :+ PutOp(key, value))
}

class PrintEvaluator extends Evaluator[DbOp, Unit, Unit] {
  override def apply(a: DbOp, prev: Unit): Unit = println(a)

  override def result: Unit = ()
}

class MemoryEvaluator extends Evaluator[DbOp, Option[String], Map[String, String]] {
  val db: mutable.Map[String, String] = mutable.Map.empty

  override def apply(a: DbOp, prev: Option[String]): Option[String] = a match {
    case GetOp(key) =>
      db.get(key)

    case PutOp(key, value) =>
      db.put(key, prev.getOrElse(value))
      None
  }

  override def result: Map[String, String] = db.toMap
}

object Example extends App {
  println("Start")

  val db: Db = new Db()
    .put("A", "foo")
    .get("A")
    .put("B", "")

  println("Run print")
  db.run((), new PrintEvaluator)

  println("Run memory: " + db.run(None, new MemoryEvaluator))

  println("End")
}
