package controllers

import java.time.OffsetDateTime
import javax.inject._
import play.api.i18n._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._

@Singleton
class CalendarController @Inject() (mcc: MessagesControllerComponents) extends MessagesAbstractController(mcc) {
  def getIndex = Action { implicit request =>
    val messages: Messages = request.messages
    Ok(views.html.calendar.index())
  }

  def getAdd = Action { implicit request =>
    val messages: Messages = request.messages
    Ok(views.html.calendar.add())
  }
}
