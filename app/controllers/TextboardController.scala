package controllers

import java.time.OffsetDateTime
import javax.inject._
import play.api.i18n._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._

case class TextBoardPostRequest(body: String)

@Singleton
class TextboardController @Inject() (mcc: MessagesControllerComponents) extends MessagesAbstractController(mcc) {
  private val form = Form(
    mapping(
      "post" -> text(minLength = 1, maxLength = 10)
    )(TextBoardPostRequest.apply)(TextBoardPostRequest.unapply)
  )

  def get = Action { implicit request: MessagesRequest[AnyContent] =>
    val messages: Messages = request.messages
    Ok(views.html.textboard.index(PostRepository.findAll, form))
  }

  def post = Action { implicit request: MessagesRequest[AnyContent] =>
    form
      .bindFromRequest()
      .fold(
        error => BadRequest(views.html.textboard.index(PostRepository.findAll, error)),
        postRequest => {
          val post = Post(postRequest.body, OffsetDateTime.now)
          PostRepository.add(post)
          Redirect("/textboard")
        }
      )
  }
}
