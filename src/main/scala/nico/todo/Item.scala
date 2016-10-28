/**
  * Created by anicolaspp on 10/15/16.
  */
package nico.todo

import java.util.UUID


import scala.util.{Success, Failure, Try}

case class Item(id: UUID, taskName: String)


trait Storage {
  def getAll(): List[Item]

  def add(item: Item): Item

  def get(id: UUID): Option[Item]

  def update(item: Item): Option[Item]

  def delete(item: Item): Option[Item]
}

object Storage {

  def apply(): Storage = InMemoryStorage

  object InMemoryStorage extends Storage {
    val db = scala.collection.mutable.ListBuffer.empty[Item]

    override def add(item: Item): Item = {
      db += item

      item
    }

    override def get(id: UUID): Option[Item] =
      db.find(_.id == id)

    override def update(item: Item): Option[Item] =
      for {
        n      <-  db.zipWithIndex.find { case (x, s) => x.id == item.id }.map(_._2)
        _      <-  Try { db.update(n, item) }.toOption
        nItem  <-  get(item.id)
      } yield nItem

    override def delete(item: Item): Option[Item] =
      for {
        n      <-  db.zipWithIndex.find { case (x, s) => x.id == item.id }.map(_._2)
        x      <-  Try { db.remove(n) }.toOption
      } yield x

    override def getAll(): List[Item] = db.toList
  }
}

