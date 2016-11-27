package nu.staldal.monadfree


sealed trait TodoOp
case class NewTask(task: String) extends TodoOp
case class CompleteTask(task: String) extends TodoOp
case object GetTasks extends TodoOp

class Todo(val accumulator: Seq[TodoOp] = Vector.empty) extends MonadFree[TodoOp] {
  def newTask(task: String): Todo = new Todo(accumulator :+ NewTask(task))
  def completeTask(task: String): Todo = new Todo(accumulator :+ CompleteTask(task))
  def getTasks: Todo = new Todo(accumulator :+ GetTasks)
}

class ProductionEvaluator extends Evaluator[TodoOp, Unit] {
  override def apply(a: TodoOp): Unit = {
    a match {
      case NewTask(_) =>
        // database write
      case CompleteTask(_) =>
        // database write
      case GetTasks =>
        // database read
    }
  }

  override def result: Unit = ()
}

class PrintEvaluator extends Evaluator[TodoOp, Unit] {
  override def apply(a: TodoOp): Unit = {
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

  todos.run(new PrintEvaluator)

  println("End")
}
