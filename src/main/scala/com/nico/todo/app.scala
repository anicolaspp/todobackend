package com.nico.todo

import com.twitter.finagle.Http
import com.twitter.util.Await
import io.circe.generic.auto._
import io.finch.circe._
import nico.todo._


object app
  extends App
    with AsyncAPI {

  val storage = Storage.apply()



  val api = TodoService(storage).api :+: getDataApi

  val service = api.toService

  Await.ready(Http.server.serve(":8080", service))
}

