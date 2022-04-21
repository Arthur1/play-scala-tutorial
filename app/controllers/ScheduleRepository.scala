package controllers

import java.time.{LocalDate, LocalTime, LocalDateTime}

object ScheduleRepository {

  var schedules: Seq[Schedule] = Vector()

  def findAll: Seq[Schedule] = schedules

  def findOfDate(localDate: LocalDate): Seq[Schedule] = {
    val localDateTime = LocalDateTime.of(localDate, LocalTime.of(0, 0))
    val nextLocalDateTime = LocalDateTime.of(localDate.plusDays(1), LocalTime.of(0, 0))
    schedules
      .filter { schedule =>
        schedule.startsAt.isBefore(nextLocalDateTime)
      }
      .filterNot { schedule =>
        // isAfterOrEqual = !isBefore
        schedule.endsAt.isBefore(localDateTime)
      }
  }

  def add(schedule: Schedule): Unit = { schedules = schedules :+ schedule }
}
