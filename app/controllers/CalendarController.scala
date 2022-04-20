package controllers

import java.time.OffsetDateTime
import java.time.LocalDateTime
import javax.inject._
import play.api.i18n._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._

case class CreateScheduleRequest(title: String, startsAt: LocalDateTime, endsAt: LocalDateTime)

@Singleton
class CalendarController @Inject() (mcc: MessagesControllerComponents) extends MessagesAbstractController(mcc) {
  private val form = Form(
    mapping(
      "title" -> nonEmptyText(maxLength = 30),
      "startsAt" -> localDateTime,
      "endsAt" -> localDateTime
    )(CreateScheduleRequest.apply)(CreateScheduleRequest.unapply)
  )

  def getIndex = Action { implicit request =>
    val messages: Messages = request.messages
    Ok(views.html.calendar.index())
  }

  def getAdd = Action { implicit request =>
    val messages: Messages = request.messages
    Ok(views.html.calendar.add(form))
  }

  def postAdd = Action { implicit request =>
    form
      .bindFromRequest()
      .fold(
        error => BadRequest(views.html.calendar.add(error)),
        postRequest => {
          // val post = Post(postRequest.body, OffsetDateTime.now)
          // PostRepository.add(post)
          Redirect(routes.CalendarController.getIndex())
        }
      )
  }
}
