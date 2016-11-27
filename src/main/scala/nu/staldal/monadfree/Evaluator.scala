package nu.staldal.monadfree


trait Evaluator[E, R] {
  def apply(f: E): Unit
  def result: R
}
