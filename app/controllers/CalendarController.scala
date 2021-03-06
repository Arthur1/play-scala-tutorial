package controllers

import scala.util.{Try, Success, Failure}
import java.time.{LocalDate, LocalDateTime}
import java.time.format.DateTimeParseException
import javax.inject._
import play.api.i18n._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._

case class CreateScheduleRequest(title: String, startsAt: LocalDateTime, endsAt: LocalDateTime)

@Singleton
class CalendarController @Inject() (mcc: MessagesControllerComponents) extends MessagesAbstractController(mcc) {
  def validateIsAfter(title: String, startsAt: LocalDateTime, endsAt: LocalDateTime) = {
    if (endsAt.isAfter(startsAt)) Some(CreateScheduleRequest(title, startsAt, endsAt))
    else None
  }

  private val form = Form(
    mapping(
      "title" -> nonEmptyText(maxLength = 30),
      "startsAt" -> localDateTime("yyyy-MM-dd'T'HH:mm"),
      "endsAt" -> localDateTime("yyyy-MM-dd'T'HH:mm")
    )(CreateScheduleRequest.apply)(CreateScheduleRequest.unapply).verifying(
      "error.afterDateTime",
      request => validateIsAfter(request.title, request.startsAt, request.endsAt).isDefined
    )
  )

  def getIndex(date: Option[String]) = Action { implicit request =>
    val messages: Messages = request.messages
    val localDateTry: Try[LocalDate] = Try {
      date.map(LocalDate.parse(_)).getOrElse(LocalDate.now)
    }
    localDateTry match {
      case Success(localDate) => {
        val schedules = ScheduleRepository.findOfDate(localDate)
        Ok(views.html.calendar.index(schedules, localDate))
      }
      case Failure(exception) => {
        BadRequest(views.html.calendar.error(Messages("calendar.error.parseDate")))
      }
    }
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
          Redirect(routes.CalendarController.getIndex(None))
        }
      )
  }
}
