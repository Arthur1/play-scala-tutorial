package controllers

import javax.inject._
import play.api.i18n._
import play.api.mvc._

@Singleton
class PlusController @Inject() (mcc: MessagesControllerComponents) extends MessagesAbstractController(mcc) {
  val logger = play.api.Logger("plus")

  def get(a: Option[Int], b: Option[Int]) =
    Action { implicit request: MessagesRequest[AnyContent] =>
      val messages: Messages = request.messages
      logger.info(s"parameters: a=$a, b=$b")
      Ok {
        a.map { a =>
          b.map { b =>
            a + b
          }
        }.flatten
          .map(_.toString)
          .getOrElse(Messages("plus.notEnoughArguments"))
      }
    }
}
