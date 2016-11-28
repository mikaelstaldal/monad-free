package nu.staldal.monadfree

import scala.collection.immutable


abstract class MonadFree[E] {
  val accumulator: immutable.Seq[E]

  def run[R](evaluator: Evaluator[E, R]): R = {
    accumulator.foreach(evaluator.apply)
    evaluator.result
  }
}
