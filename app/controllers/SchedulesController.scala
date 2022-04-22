package controllers

import scala.util.{Try, Success, Failure}
import java.time.{LocalDate, LocalDateTime}
import java.time.format.DateTimeParseException
import javax.inject._
import play.api.i18n._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.libs.json.Writes

case class ApiCreateScheduleRequest(title: String, startsAt: LocalDateTime, endsAt: LocalDateTime)

@Singleton
class SchedulesController @Inject() (mcc: MessagesControllerComponents) extends MessagesAbstractController(mcc) {
  def validateIsAfter(title: String, startsAt: LocalDateTime, endsAt: LocalDateTime) = {
    if (endsAt.isAfter(startsAt)) Some(ApiCreateScheduleRequest(title, startsAt, endsAt))
    else None
  }

  private val form = Form(
    mapping(
      "title" -> nonEmptyText(maxLength = 30),
      "startsAt" -> localDateTime("yyyy-MM-dd'T'HH:mm"),
      "endsAt" -> localDateTime("yyyy-MM-dd'T'HH:mm")
    )(ApiCreateScheduleRequest.apply)(ApiCreateScheduleRequest.unapply).verifying(
      "error.afterDateTime",
      request => validateIsAfter(request.title, request.startsAt, request.endsAt).isDefined
    )
  )

  def getList(date: Option[String]) = Action { implicit request =>
    val localDateTry: Try[LocalDate] = Try {
      date.map(LocalDate.parse(_)).getOrElse(LocalDate.now)
    }
    localDateTry match {
      case Success(localDate) => {
        val schedules = ScheduleRepository.findOfDate(localDate)
        Ok(views.html.calendar.index(schedules, localDate))
        Ok(
          Json.toJson(
            Response(Meta(200), Some(Json.obj("schedules" -> Json.toJson(ScheduleRepository.findOfDate(localDate)))))
          )
        )
      }
      case Failure(exception) => {
        BadRequest(Json.toJson(Response(Meta(400, Some(Messages("calendar.error.parseDate"))))))
      }
    }
  }

  def create = Action { implicit request =>
    form
      .bindFromRequest()
      .fold(
        error => {
          val errorMessage = Messages(error.errors(0).message)
          BadRequest(Json.toJson(Response(Meta(400, Some(errorMessage)))))
        },
        postRequest => {
          val schedule = Schedule(postRequest.title, postRequest.startsAt, postRequest.endsAt)
          ScheduleRepository.add(schedule)
          Ok(Json.toJson(Response(Meta(200))))
        }
      )
  }
}
