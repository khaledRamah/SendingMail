package com.testingMail

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.pattern.ask

import scala.concurrent.duration._
import akka.util.Timeout
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.ActorMaterializer


object Main extends App with  Directives with CustomJsonSprySupport {
  val Mails :ActorRef = ActorSystem("CartSystem").actorOf(Props[MailActor], "MailActor")

  def Routes: Route = {
    implicit val timeout = Timeout(5.seconds)
    pathPrefix("authentication") {
      pathEnd {
        post {
          entity(as[Authentication]) { user =>
            Mails ? Authentication(user.userName, user.passWord)
            complete(StatusCodes.OK)
          }
        }
      }
    }~
      pathPrefix("initialization") {
          pathEnd {
            post {
              entity(as[Initialization]) { initData =>
                Mails ? Initialization(initData.hostName, initData.smtpPort, initData.sSLOnConnect)
                complete(StatusCodes.OK)
              }
            }
          }
        } ~
      pathPrefix("sendMail") {
          pathEnd {
            post {
              entity(as[NewMail]) { mailData =>
                Mails ? NewMail(mailData.subject, mailData.message, mailData.to)
                complete(StatusCodes.OK)
              }
            }
          }
        }
  }

  val host = "localHost"//config.getString("http.host")
  val port = 8080 //config.getInt("http.port")

  implicit val system = ActorSystem("MainSystem")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher


  Http().bindAndHandle(Routes, host, port) map { binding =>
    println(s"REST interface bound to ${binding.localAddress}") } recover { case ex =>
    println(s"REST interface could not bind to $host:$port", ex.getMessage)
  }
}
