import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json._
import play.api.libs.json.JsValue
import play.api.test.WsTestClient
import scala.concurrent.Await
import scala.concurrent.duration.Duration

class SchedulesSpec extends PlaySpec with GuiceOneServerPerSuite {
  val baseUrl = s"http://localhost:$port"

  override def fakeApplication() =
    new GuiceApplicationBuilder()
      .configure("db.default.driver" -> "org.h2.Driver", "db.default.url" -> "jdbc:h2:mem:test;MODE=MYSQL")
      .build()

  "GET /api/schedules" should {
    "何も投稿しない場合は空の配列を返す" in {
      WsTestClient.withClient { ws =>
        val response = Await.result(ws.url(s"$baseUrl/api/schedules").get(), Duration.Inf)
        assert(response.status === 200)
        assert(response.json === Json.parse("""{"meta":{"status":200},"data":{"schedules":[]}}"""))
      }
    }
  }

  "POST /api/schedules" should {
    "投稿したものが返される" in {
      WsTestClient.withClient { ws =>
        val title = "test title"
        val startsAt = "2022-04-25T10:10:00"
        val endsAt = "2022-04-25T10:20:00"
        val scheduleJson = Json.obj(
          "title" -> title,
          "startsAt" -> startsAt,
          "endsAt" -> endsAt
        )
        val postResponse =
          Await.result(ws.url(s"$baseUrl/api/schedules").post(scheduleJson), Duration.Inf)
        assert(postResponse.status === 200)
        val getResponse = Await.result(ws.url(s"$baseUrl/api/schedules?date=2022-04-25").get(), Duration.Inf)
        assert(getResponse.status === 200)
        assert((getResponse.json \ "meta" \ "status").as[Int] === 200)
        val schedules = (getResponse.json \ "data" \ "schedules").as[Array[JsValue]]
        assert(schedules.length === 1)
        assert((schedules(0) \ "title").as[String] === title)
        assert((schedules(0) \ "startsAt").as[String] === startsAt)
        assert((schedules(0) \ "endsAt").as[String] === endsAt)
      }
    }

    "必要なキーがない場合にはエラー" in {
      WsTestClient.withClient { ws =>
        val title = "test title"
        val startsAt = "2022-04-25T10:10:00"
        val scheduleJson = Json.obj(
          "title" -> title,
          "startsAt" -> startsAt
        )
        val postResponse =
          Await.result(ws.url(s"$baseUrl/api/schedules").post(scheduleJson), Duration.Inf)
        assert(postResponse.status === 400)
        assert((postResponse.json \ "meta" \ "status").as[Int] === 400)
        assert((postResponse.json \ "meta" \ "errorMessage").as[String] === "json validate error")
      }
    }

    "開始時刻が終了日時より後の場合にはエラー" in {
      WsTestClient.withClient { ws =>
        val title = "test title"
        val startsAt = "2022-04-25T10:10:00"
        val endsAt = "2022-03-01T09:00:00"
        val scheduleJson = Json.obj(
          "title" -> title,
          "startsAt" -> startsAt,
          "endsAt" -> endsAt
        )
        val postResponse =
          Await.result(ws.url(s"$baseUrl/api/schedules").post(scheduleJson), Duration.Inf)
        assert(postResponse.status === 400)
        assert((postResponse.json \ "meta" \ "status").as[Int] === 400)
        assert(
          (postResponse.json \ "meta" \ "errorMessage").as[String] === "End Datetime must be after Start Datetime."
        )
      }
    }
  }
}
