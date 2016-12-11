package nu.staldal.monadfree.todo

import nu.staldal.monadfree.{Evaluator, MonadFree}

import scala.collection.immutable
import scala.collection.immutable.Queue

sealed trait TodoOp
case class NewTask(task: String) extends TodoOp
case class CompleteTask(task: String) extends TodoOp
case object GetTasks extends TodoOp

class Todo(val accumulator: immutable.Seq[TodoOp] = Queue.empty) extends MonadFree[TodoOp] {
  def newTask(task: String): Todo = new Todo(accumulator :+ NewTask(task))
  def completeTask(task: String): Todo = new Todo(accumulator :+ CompleteTask(task))
  def getTasks: Todo = new Todo(accumulator :+ GetTasks)
}

class PrintEvaluator extends Evaluator[TodoOp, Unit, Unit] {
  override def apply(a: TodoOp, prev: Unit): Unit = {
    a match {
      case NewTask(task) =>
        println(s"New task added: $task")
      case CompleteTask(task) =>
        println(s"Task completed: $task")
      case GetTasks =>
        println(s"Request to fetch tasks")
    }
  }

  override def result: Unit = ()
}


object Example extends App {
  println("Start")

  val todos: Todo = new Todo()
    .newTask("Try Free Monad example")
    .newTask("Extract the essence")
    .newTask("Code something different")
    .completeTask("Try Free Monad example")
    .getTasks

  println("Run")

  todos.run((), new PrintEvaluator)

  println("End")
}
