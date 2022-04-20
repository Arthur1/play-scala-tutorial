package controllers

object ScheduleRepository {

  var schedules: Seq[Schedule] = Vector()

  def findAll: Seq[Schedule] = schedules

  def add(schedule: Schedule): Unit = { schedules = schedules :+ schedule }
}
