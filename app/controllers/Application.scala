package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import models.{Task, User}

object Application extends Controller {

  val taskForm = Form (
	  tuple(
    "label" -> nonEmptyText ,
    "dec" -> nonEmptyText
    )
  )
  
  def index = Action {
    Redirect(routes.Application.tasks)
  }

  def tasks = Action {
    Ok(views.html.index(Task.all(), taskForm))
  }

  def newTask = Action { implicit request =>
    taskForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(Task.all(), errors)),
      label => {
        Task.create(label._1,label._2)
        Redirect(routes.Application.tasks)
      }
    )
  }
	def users = Action {
		Ok(views.html.register(loginForm))
	}
  def newUser = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(Task.all(), errors)),
      user => {
        User.create(user._1,user._2)
        Redirect(routes.Application.tasks)
      }
    )
  }
	def loginF = Action {
		Ok(views.html.login(loginForm))
	}
	def login = Action { implicit request =>
		loginForm.bindFromRequest.fold(
			errors => BadRequest(views.html.index(Task.all(), errors)),
			user => {
				if(User.find(user._1).exists(ur => ur.password == user._2)) Redirect(routes.Application.tasks)
				else Redirect(routes.Application.loginF)
			}
		)
	}
  def deleteTask(id: String) = Action {
    Task.delete(id)
    Redirect(routes.Application.tasks)
  }
	val loginForm = Form(
		tuple(
			"email" -> text,
			"password" -> text
		)
	)
	val anyData = Map("email" -> "bob@gmail.com", "password" -> "secret")
	val (user, password) = loginForm.bind(anyData).get

}
