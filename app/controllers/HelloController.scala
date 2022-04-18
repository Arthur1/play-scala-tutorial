package controllers

import javax.inject.Inject
import javax.inject.Singleton
import play.api.i18n._
import play.api.mvc._

class HelloController @Inject() (mcc: MessagesControllerComponents) extends MessagesAbstractController(mcc) {
  def get(name: Option[String]) = Action { implicit request: MessagesRequest[AnyContent] =>
    val messages: Messages = request.messages
    Ok {
      name
        .map(s => Messages("hello", s))
        .getOrElse(Messages("noQuery"))
    }
  }
}
