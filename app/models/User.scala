package models

import play.api.Play.current
import play.api.PlayException

import com.novus.salat._
import com.novus.salat.dao._

import com.mongodb.casbah.commons.Imports._
import com.mongodb.casbah.MongoConnection

import com.novus.salat.Context

import mongoContext._



case class User(_id: ObjectId = new ObjectId, email: String, password: String)

object UserDao extends SalatDAO[User, ObjectId](
	collection = MongoConnection()(
		current.configuration.getString("mongodb.default.db")
		.getOrElse(throw new PlayException("Configuration error",
			"Could not find mongodb.default.db in settings"))
	)("users"))


object User {
	def all(): List[User] = UserDao.find(MongoDBObject.empty, MongoDBObject.empty).toList

	def create(email: String, password: String): Option[ObjectId] = {
		UserDao.insert(User(email = email, password = password))
	}

	def delete(id: String) {
		UserDao.remove(MongoDBObject("_id" -> new ObjectId(id)))
	}
	def find(email: String): Option[User] = {
		UserDao.findOne(MongoDBObject("email" -> email))
	}
}
