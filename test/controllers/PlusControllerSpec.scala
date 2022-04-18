package controllers

import org.scalatestplus.play.PlaySpec
import play.api.test.FakeRequest
import play.api.test.Helpers._

class PlusControllerSpec extends PlaySpec {

  def controller = new PlusController(stubMessagesControllerComponents())

  "get" should {
    "クエリーパラメータがある場合は「3」というレスポンスを返す" in {
      val name = "namae"
      val result = controller.get(Some(1), Some(2))(FakeRequest())
      assert(status(result) === 200)
      assert(contentAsString(result) === s"3")
    }

    """クエリーパラメータaがない場合は「Please give arguments of a and b.」というレスポンスを返す""" in {
      val result = controller.get(Some(1), None)(FakeRequest())
      assert(status(result) === 200)
      assert(contentAsString(result) === """Please give arguments of a and b.""")
    }

    """クエリーパラメータbがない場合は「Please give arguments of a and b.」というレスポンスを返す""" in {
      val result = controller.get(None, Some(2))(FakeRequest())
      assert(status(result) === 200)
      assert(contentAsString(result) === """Please give arguments of a and b.""")
    }
  }
}
