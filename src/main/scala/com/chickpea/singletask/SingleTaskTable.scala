package com.chickpea.singletask

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

import com.chickpea.database.DatabaseConnector


case class SingleTask( id: Long,
                       title: String,
                       description: String,
                       userId: Long,
                       startDate: Timestamp,
                       endDate: Timestamp,
                       createdAt: Timestamp,
                       updatedAt: Timestamp)

trait SingleTaskTable {

    class SingleTasks(tag: Tag) extends Table[SingleTask](tag, "Chickpea_SingleTask") {
        def currentTime = new Timestamp((new Date).getTime)

        def id          = column[Long]("id", O.AutoInc, O.PrimaryKey)
        def title       = column[String]("title")
        def description = column[String]("description")
        def userId      = column[Long]("user_id")

        def startDate   = column[Timestamp]("start_date", O.Default(currentTime))
        def endDate     = column[Timestamp]("end_date", O.Default(currentTime))
        def createdAt   = column[Timestamp]("created_at", O.Default(currentTime))
        def updatedAt   = column[Timestamp]("updated_at", O.Default(currentTime))

        def * = (id, title, description, userId, startDate, endDate, createdAt, updatedAt) <> ((SingleTask.apply _).tupled, SingleTask.unapply)
    }

    protected val singletasks = TableQuery[SingleTasks]
}