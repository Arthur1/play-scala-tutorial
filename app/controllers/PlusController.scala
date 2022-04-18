package controllers

import javax.inject._
import play.api.mvc._

@Singleton
class PlusController @Inject() (cc: ControllerComponents) extends AbstractController(cc) {
  def get(a: Option[Int], b: Option[Int]) =
    Action { implicit request: Request[AnyContent] =>
      Ok {
        a.map { a =>
          b.map { b =>
            a + b
          }
        }.flatten
          .map(_.toString)
          .getOrElse("Please give arguments of a and b.")
      }
    }
}
