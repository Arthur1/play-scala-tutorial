package controllers

import java.time.{LocalDate, LocalTime, LocalDateTime}
import scalikejdbc._

object ScheduleRepository {

  var schedules: Seq[Schedule] = Vector()

  def findAll: Seq[Schedule] = schedules

  def findOfDate(localDate: LocalDate): Seq[Schedule] = {
    val localDateTime = LocalDateTime.of(localDate, LocalTime.of(0, 0))
    val nextLocalDateTime = LocalDateTime.of(localDate.plusDays(1), LocalTime.of(0, 0))
    DB readOnly { implicit session =>
      sql"""
        SELECT id, title, starts_at, ends_at
        FROM schedules
        WHERE starts_at < ${nextLocalDateTime} AND ends_at >= ${localDateTime}
      """
        .map { rs =>
          Schedule(rs.long("id"), rs.string("title"), rs.localDateTime("starts_at"), rs.localDateTime("ends_at"))
        }
        .list()
        .apply()
    }
  }

  def add(schedule: Schedule): Unit = DB localTx { implicit session =>
    sql"INSERT INTO schedules (title, starts_at, ends_at) VALUES (${schedule.title}, ${schedule.startsAt}, ${schedule.endsAt})"
      .update()
      .apply()
  }
}
