package nu.staldal.monadfree.db

import nu.staldal.monadfree.{Evaluator, MonadFree}

import scala.collection.immutable


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


object Example extends App {
  println("Start")

  val db: Db = new Db()
    .put("A", "foo")
    .get("A")
    .put("B", "a")
    .list()

  println("Run")

  db.run(new PrintEvaluator)

  println("End")
}
