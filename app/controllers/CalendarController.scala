package controllers

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
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
      "startsAt" -> localDateTime("yyyy-MM-dd'T'HH:mm"),
      "endsAt" -> localDateTime("yyyy-MM-dd'T'HH:mm")
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
        error => {
          BadRequest(views.html.calendar.add(error))
        },
        postRequest => {
          val schedule = Schedule(postRequest.title, postRequest.startsAt, postRequest.endsAt)
          ScheduleRepository.add(schedule)
          Redirect(routes.CalendarController.getIndex())
        }
      )
  }
}
