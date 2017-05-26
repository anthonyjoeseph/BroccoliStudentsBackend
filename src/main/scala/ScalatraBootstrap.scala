

import com.readbroccoli.broccolistudents._
import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    context.mount(new UserServlet, "/users/*")
    context.mount(new BookServlet, "/books/*")
    context.mount(new QuizServlet, "/quizzes/*")
    context.mount(new FileServlet, "/files/*")
    context.mount(new BookMakerServlet, "/bookMaker/*")
    context.mount(new BookMakerPagesServlet, "/bookMakerPages/*")
    context.mount(new BookMakerMusicServlet, "/bookMakerMusic/*")
  }
}