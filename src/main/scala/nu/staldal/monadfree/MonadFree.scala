package nu.staldal.monadfree

import scala.collection.immutable


abstract class MonadFree[E] {
  val accumulator: immutable.Seq[E]

  def run[I, R](initial: I, evaluator: Evaluator[E, I, R]): R = {
    val iter = accumulator.iterator
    var i = initial
    while (iter.hasNext) {
      i = evaluator.apply(iter.next(), i)
    }
    evaluator.result
  }
}
