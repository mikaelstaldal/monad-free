package nu.staldal.monadfree


trait Evaluator[E, I, R] {
  def apply(f: E, prev: I): I
  def result: R
}
