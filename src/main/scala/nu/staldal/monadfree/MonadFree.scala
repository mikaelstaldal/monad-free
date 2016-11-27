package nu.staldal.monadfree


abstract class MonadFree[E] {
  val accumulator: Seq[E]

  def run[R](evaluator: Evaluator[E, R]): R = {
    accumulator.foreach(evaluator.apply)
    evaluator.result
  }
}
