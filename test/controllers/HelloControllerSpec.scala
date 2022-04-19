package controllers

import org.scalatestplus.play.PlaySpec
import play.api.test.FakeRequest
import play.api.test.Helpers._

class HelloControllerSpec extends PlaySpec {

  def controller = new HelloController(stubMessagesControllerComponents())

  "get" should {
    "クエリーパラメータがある場合は「Hello, namae!」というレスポンスを返す" in {
      val name = "namae"
      val result = controller.get(Some(name))(stubMessagesRequest())
      assert(status(result) === 200)
      // TODO: Messagesはmockしない
      assert(contentAsString(result) === s"hello")
    }

    """クエリーパラメータがない場合は「Please give a name as a query parameter named "name".」というレスポンスを返す""" in {
      val result = controller.get(None)(stubMessagesRequest())
      assert(status(result) === 200)
      // TODO: Messagesはmockしない
      assert(contentAsString(result) === "noQuery")
    }
  }
}
