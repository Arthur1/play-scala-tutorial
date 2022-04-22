package controllers

import java.time.LocalDateTime
import play.api.libs.json.Json
import play.api.libs.json.Writes

case class Schedule(id: Long, title: String, startsAt: LocalDateTime, endsAt: LocalDateTime)

object Schedule {
  implicit val writes: Writes[Schedule] = Json.writes[Schedule]

  def apply(title: String, startsAt: LocalDateTime, endsAt: LocalDateTime): Schedule =
    Schedule(0, title, startsAt, endsAt)
}
