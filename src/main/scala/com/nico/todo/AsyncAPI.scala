/**
  * Created by anicolaspp on 10/27/16.
  */
package com.nico.todo

import com.nico.todo.DStore.DError
import com.twitter.util.Future
import io.circe.{Encoder, Json}
import io.finch.{Output, _}

import scalaz.{EitherT, Functor, Monad, \/, \/-}



trait AsyncAPI {

  import TFutureExtensions._

  def getDataApi: Endpoint[List[String]] = get("data" :: int) { n: Int =>
    val x: EitherT[Future, DError, Output[List[String]]] =
      for {
        ints <- | <~ DStore.getSomeDataAsync(n)
        strs <- | <~ DStore.toStr(ints)
      } yield Ok(strs)

    x.valueOr(error => BadRequest(new Exception(error.toString)))
  }
}


object AsyncAPI {

  implicit def xorToJson = new Encoder[\/[DError, List[Int]]] {
    override def apply(a: \/[DError, List[Int]]): Json =
      a.fold(e => Encoder.encodeString(e.toString), s => Json.fromValues(s.map(i => Json.fromInt(i))))
  }
}

object TFutureExtensions {
  implicit def toFunctor: Functor[Future] = new Functor[Future] {
    override def map[A, B](fa: Future[A])(f: (A) => B): Future[B] = fa.map(f)
  }

  implicit def toMonad: Monad[Future] = new Monad[Future] {
    override def point[A](a: => A): Future[A] = Future.value(a)

    override def bind[A, B](fa: Future[A])(f: (A) => Future[B]): Future[B] = fa.flatMap(f)
  }
}


object DStore {

  sealed trait DError

  type AsyncResult[A] = Future[DError \/ A]

  def getSomeDataAsync(n: Int): AsyncResult[List[Int]] = Future { \/-((1 to n).toList) }

  def toStr[A](seq: Seq[A]): List[String] = seq.map(_.toString).toList
}

object | {

  import DStore._

  def <~[A](f: AsyncResult[A]) = EitherT(f)

  def <~[A](a: A) = EitherT[Future, DError, A](Future.value(\/-(a)))
}
