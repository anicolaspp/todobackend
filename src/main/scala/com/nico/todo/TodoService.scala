package nico.todo

import java.util.UUID

import io.circe.generic.auto._
import io.finch._
import io.finch.circe._



trait TodoService {
  def getTodo: Endpoint[Item]

  def todos: Endpoint[List[Item]]

  def deleteTodo: Endpoint[Item]

  def add: Endpoint[Item]

  def patchTodo: Endpoint[Item]

  val api = getTodo :+: todos :+: deleteTodo :+: add :+: patchTodo
}

object TodoService {

  case class TodoServiceImp(storage: Storage) extends TodoService {
    override def getTodo: Endpoint[Item] = get("todo" :: uuid) { id: UUID =>
      storage
        .get(id)
        .fold[Output[Item]](NotFound(new Exception))(item => Ok(item))
    }

    override def todos: Endpoint[List[Item]] = get("todos") {
      Ok(storage.getAll())
    }

    override def deleteTodo: Endpoint[Item] = delete("todo" :: uuid) { id: UUID =>
      val m = for {
        item  <-  storage.get(id)
        x     <-  storage.delete(item)
      } yield x

      m.fold[Output[Item]](NotFound(new Exception))(Ok)
    }

    private def postTodo: Endpoint[Item] =
      body.as[UUID => Item].map(f => f(UUID.randomUUID()))

    override def add: Endpoint[Item] = post("todo" :: postTodo) { item: Item =>
      val nItem = storage.add(item)

      Ok(nItem)
    }

    override def patchTodo: Endpoint[Item] = // patchItem {item: Item =>
      patch("todo" :: uuid :: postTodo ) {
        (id: UUID, item: Item) =>
          val updated = for {
            old <- storage.get(id)
            n   <- storage.update(Item(old.id, taskName = item.taskName))
          } yield n

          updated.fold[Output[Item]](NotFound(new Exception))(Ok)

        //        Ok(Item(UUID.randomUUID(), ""))
      }
  }


  def apply(storage: Storage): TodoService = TodoServiceImp(storage)
}
