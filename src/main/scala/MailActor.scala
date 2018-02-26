package com.testingMail

import akka.actor.Actor
import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.{Email, SimpleEmail}

case class Authentication(userName: String, passWord: String)
case class Start(hostName: String, smtpPort: Int, sSLOnConnect: Boolean)
case class NewMail(subject: String, message: String, to: List[String])
object Done
class MailActor extends Actor {
  var auth= Authentication("","")
  var mailData= Start("",0,true)
  override def receive: Receive = {
    case Authentication(userName: String, passWord: String) =>
      auth=Authentication(userName,passWord)
      sender() ! Done

    case Start(hostName: String, smtpPort: Int, sSLOnConnect: Boolean) =>
      mailData=Start(hostName,smtpPort,sSLOnConnect)
      sender() ! Done

    case NewMail(subject: String, message: String, to: List[String]) =>
      val email:Email = new SimpleEmail()
      to.foreach(oneMail => email.addTo(oneMail))
      email.setAuthenticator(new DefaultAuthenticator(auth.userName, auth.passWord))
      email.setFrom(auth.userName)
      email.setHostName(mailData.hostName)
      email.setSmtpPort(mailData.smtpPort)
      email.setSSLOnConnect(mailData.sSLOnConnect)
      email.setSubject(subject)
      email.setMsg(message)
      email.send()

      sender() ! Done
  }

}
