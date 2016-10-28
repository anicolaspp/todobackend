
package nico.todo
package nico.todo.tests

import java.util.UUID
import com.twitter.io.{Buf, Charsets}
import io.finch.{Input, _}
import org.scalatest.{FlatSpec, Matchers}
import io.finch.circe._



class ApiSpecs extends FlatSpec with Matchers {

    it should "get todo item with id" in {

      val storage = Storage()
      val item = storage.add(Item(UUID.randomUUID(), ""))
      val service = TodoService(storage)

      val input = Input.get(s"/todo/${item.id}")

      service.getTodo(input).value should be(Some(item))
    }

    it should "return empty" in {
      val input = Input.get("/todo/234234")

      TodoService(Storage()).getTodo(input).value should be(None)
    }

  it should "get all todos" in {
    val storage = Storage()
    val item = storage.add(Item(UUID.randomUUID(), ""))
    val other = storage.add(Item(UUID.randomUUID(), ""))

    val service = TodoService(storage)

    val input = Input.get("/todos")

    service.todos(input).value should be(Some(List(item, other)))
  }

  it should "delete if exists" in {
    val storage = Storage()
    val item = storage.add(Item(UUID.randomUUID(), ""))

    val service = TodoService(storage)

    val input = Input.delete(s"/todo/${item.id}")

    service.deleteTodo(input).value should be (Some(item))
  }

  it should "return not found when deleting" in {
    val input = Input.delete("/todo/234")

    TodoService(Storage()).deleteTodo(input).value should be(None)
  }

  it should "always add" in {
    val input = Input.post("/todo")
        .withBody[Application.Json](Map("taskName" -> "xxx"), Some(Charsets.Utf8))

    val Some(result) = TodoService(Storage()).add(input).value

    result.taskName should be ("xxx")
  }

//  it should "patch" in {
//
//    val storage = Storage()
//    val item = storage.add(Item(UUID.randomUUID(), "xxx"))
//
//    val input = Input.patch(s"todo/${item.id}")
//        .withBody[Application.Json](Map("taskName" -> "yyy"), Some(Charsets.Utf8))
//
//    val Some(result) = TodoService(storage).patchTodo(input).value
//
////    x should be (Some)
//
////
//    result.id should be (item.id)
//    result.taskName should be ("yyy")
//  }
}







