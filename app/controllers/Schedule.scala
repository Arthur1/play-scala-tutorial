package controllers

import java.time.LocalDateTime
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

case class Schedule(id: Long, title: String, startsAt: LocalDateTime, endsAt: LocalDateTime)

object Schedule {
  implicit val writes: Writes[Schedule] = Json.writes[Schedule]

  implicit val reads: Reads[Schedule] = (
    (__ \ "title").read[String] and
      (__ \ "startsAt").read[String].map(LocalDateTime.parse(_)) and
      (__ \ "endsAt").read[String].map(LocalDateTime.parse(_))
  )(Schedule.apply(_: String, _: LocalDateTime, _: LocalDateTime))

  def apply(title: String, startsAt: LocalDateTime, endsAt: LocalDateTime): Schedule =
    Schedule(0, title, startsAt, endsAt)
}
