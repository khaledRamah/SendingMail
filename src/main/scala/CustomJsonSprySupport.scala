package com.testingMail

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._

trait CustomJsonSprySupport extends SprayJsonSupport with DefaultJsonProtocol{
  implicit val AuthenticationFormat= jsonFormat2(Authentication)
  implicit val StartFormat= jsonFormat3(Start)
  implicit val NewMailFormat= jsonFormat3(NewMail)
}
