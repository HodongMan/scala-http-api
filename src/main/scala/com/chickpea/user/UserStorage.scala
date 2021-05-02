package com.chickpea.user



import scala.concurrent.ExecutionContext.Implicits.global

import slick.dbio.DBIO
import slick.jdbc.MySQLProfile.api.{
  DBIO => _,
  MappedTo => _,
  Rep => _,
  TableQuery => _,
  _
}

import com.chickpea.user.core.User

trait UserStorage {
    
    def getUsers(): DBIO[Seq[User]]
    def getUsersByUserIds(userId: Seq[Long]): DBIO[Seq[User]]
    def getUser(userId: Long): DBIO[Option[User]]
    def register(userRegistration: User): DBIO[User]
    def findUserByEmail(email: String, password: String): DBIO[Option[User]]
    def saveUser(user: User): DBIO[User]
}

class JdbcUserStorage extends UserTable with UserStorage {
    
    def getUsers(): DBIO[Seq[User]] = users.result

    def getUsersByUserIds(userIds: Seq[Long]): DBIO[Seq[User]] =
        users.filter(_.id inSet userIds).result

    def getUserByUsername(username: String): DBIO[Option[User]] =
        users.filter(_.username === username).result.headOption

    def getUser(userId: Long): DBIO[Option[User]] =
        users.filter(_.id === userId).result.headOption

    def register(user: User): DBIO[User] =
        (users returning users.map(_.id) into ((u, id) => u.copy(id = id + 1))) += user

    def saveUser(user: User): DBIO[User] = {
        (users returning users).insertOrUpdate(user).map(_.getOrElse(user))
    }

  def findUserByEmail(email: String, password: String): DBIO[Option[User]] =
    users
      .filter(a => a.email === email && a.password === password)
      .result
      .headOption
}