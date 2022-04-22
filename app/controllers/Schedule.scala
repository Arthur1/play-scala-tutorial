package controllers

import java.time.LocalDateTime

case class Schedule(id: Long, title: String, startsAt: LocalDateTime, endsAt: LocalDateTime)

object Schedule {
  def apply(title: String, startsAt: LocalDateTime, endsAt: LocalDateTime): Schedule =
    Schedule(0, title, startsAt, endsAt)
}
