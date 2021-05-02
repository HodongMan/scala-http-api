package com.chickpea.user


import java.sql.Timestamp
import java.util.Date

import slick.jdbc.MySQLProfile.api.{
  DBIO => _,
  MappedTo => _,
  Rep => _,
  TableQuery => _,
  _
}

import slick.lifted.TableQuery

import com.chickpea.user.core.User


trait UserTable {

    class Users(tag: Tag) extends Table[User](tag, "Chickpea_User") {

        def currentTime = new Timestamp((new Date).getTime)

        def id          = column[Long]("id", O.AutoInc, O.PrimaryKey)
        def username    = column[String]("username")
        def password    = column[String]("password")
        def email       = column[String]("email")
        def bio         = column[Option[String]]("bio")
        def image       = column[Option[String]]("image")
        def salted      = column[Option[String]]("salted")

        def createdAt   = column[Timestamp]("created_at", O.Default(currentTime))
        def updatedAt   = column[Timestamp]("updated_at", O.Default(currentTime))

        def *           = (id, username, password, email, bio, image, salted, createdAt, updatedAt) <> ((User.apply _).tupled, User.unapply)
    }

    protected val users = TableQuery[Users]
}