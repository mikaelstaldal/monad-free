package nu.staldal.monadfree.todo

import nu.staldal.monadfree.Evaluator
import org.scalatest.{FunSpec, Matchers}


class Spec extends FunSpec with Matchers {

  class TestEvaluator extends Evaluator[TodoOp, Map[String, Boolean]] {
    private var model: Map[String, Boolean] = Map.empty

    override def apply(a: TodoOp): Unit = {
      a match {
        case NewTask(task) =>
          model = model + (task.toString -> false)
        case CompleteTask(task) =>
          model = model + (task.toString -> true)
        case GetTasks =>
      }
    }

    override def result: Map[String, Boolean] = model
  }

  class ActionTestEvaluator extends Evaluator[TodoOp, List[TodoOp]] {
    private var actions: List[TodoOp] = List.empty

    override def apply(a: TodoOp): Unit = {
      a match {
        case NewTask(task) =>
          actions = actions :+ NewTask(task.toString)
        case CompleteTask(task) =>
          actions = actions :+ CompleteTask(task.toString)
        case GetTasks =>
          actions = actions :+ GetTasks
      }
    }

    override def result: List[TodoOp] = actions
  }

  describe("MonadFree") {
    val todos: Todo = new Todo()
      .newTask("Try Free Monad example")
      .newTask("Extract the essence")
      .newTask("Code something different")
      .completeTask("Try Free Monad example")
      .getTasks

    it("should evaluate todos") {
      val result = todos.run(new TestEvaluator)

      val expected: Map[String, Boolean] =
        Map(
          "Try Free Monad example" -> true,
          "Extract the essence" -> false,
          "Code something different" -> false
        )

      result shouldBe expected
    }

    it("should evaluate todos with an action evaluator") {
      val actionTestEvaluator = new ActionTestEvaluator
      todos.run(actionTestEvaluator)

      val expected: List[TodoOp] =
        List(
          NewTask("Try Free Monad example"),
          NewTask("Extract the essence"),
          NewTask("Code something different"),
          CompleteTask("Try Free Monad example"),
          GetTasks
        )

      actionTestEvaluator.result shouldBe expected
    }
  }
}
