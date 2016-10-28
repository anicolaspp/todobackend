/**
  * Created by anicolaspp on 10/15/16.
  */

package nico.todo
package nico.todo.tests

import java.util.UUID
import org.scalatest._

class DBSpecs extends FlatSpec with Matchers {

  "Storage" should "insert item" in {

    val storage = Storage()

    val item = storage.add(Item(UUID.randomUUID(), "go shopping"))

    item.taskName should be("go shopping")
  }

  it should "be able to get item after added it" in {

    val storage = Storage()

    val item = storage.add(Item(UUID.randomUUID(), "go shopping"))

    storage.get(item.id) should be(Some(item))
  }

  it should "return none is item not found" in {
    val storage = Storage()

    val maybeItem = storage.get(UUID.randomUUID())

    maybeItem should be(None)
  }

  it should "update item" in {
    val storage = Storage()

    val item = storage.add(Item(UUID.randomUUID(), "go shopping"))

    val newItem = storage.update(Item(item.id, "go shopping now"))

    newItem should be(Some(Item(item.id, "go shopping now")))
  }

  it should "fail on update for non-existing items" in {
    val storage = Storage()

    storage.update(Item(UUID.randomUUID(), "")) should be(None)
  }

  it should "return none for non-existing item when deleting" in {
    val storage = Storage()

    storage.delete(Item(UUID.randomUUID(), "")) should be(None)
  }

  it should "delete" in {
    val storage = Storage()

    val item = storage.add(Item(UUID.randomUUID(), "go shopping"))

    storage.delete(item) should be(Some(item))
    storage.get(item.id) should be(None)
  }

  it should "return all todos" in {
    val storage = Storage()

    val item = storage.add(Item(UUID.randomUUID(), "go shopping"))
    val other = storage.add(Item(UUID.randomUUID(), "other go shopping"))

    storage.getAll() should contain inOrder(item, other)
  }
}


