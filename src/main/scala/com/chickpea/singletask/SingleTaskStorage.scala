package com.chickpea.singletask

import scala.concurrent.ExecutionContext.Implicits.global

import slick.dbio.{DBIO, DBIOAction}
import slick.jdbc.MySQLProfile.api.{
  DBIO => _,
  MappedTo => _,
  Rep => _,
  TableQuery => _,
  _
}

import com.chickpea.database.DatabaseConnector
import com.chickpea.singletask.singletasks.SingleTaskRequest
import com.chickpea.user.UserTable
import com.chickpea.user


trait SingleTaskStorage {
    def getSingleTasks(pageRequest: SingleTaskRequest): DBIO[Seq[SingleTask]]
    def createSingleTask(newSingleTask: SingleTask): DBIO[SingleTask]
    def updateSingleTask(singleTask: SingleTask): DBIO[SingleTask]
}


class JdbcSingleTaskStorage 
    extends SingleTaskStorage 
    with SingleTaskTable 
    with UserTable {

    def getSingleTasks(pageRequest: SingleTaskRequest): DBIO[Seq[SingleTask]] = {

        val query = singletasks.join(users).on(_.userId === _.id)

        query
        .filter { st =>
            pageRequest.userName.fold(true.bind)(st._2.username === _)
        }
        .map(_._1)
        .drop(pageRequest.offset.getOrElse(0L))
        .take(pageRequest.limit.getOrElse(Long.MaxValue))
        .result
    }

     def createSingleTask(newSingleTask: SingleTask): DBIO[SingleTask] = 
        (singletasks returning singletasks.map(_.id) into ((u, id) => u.copy(id = id))) += newSingleTask

    def updateSingleTask(singleTask: SingleTask): DBIO[SingleTask] = {
        singletasks
        .filter(_.id === singleTask.id)
        .update(singleTask)
        .flatMap(_ => singletasks.filter(_.id === singleTask.id).result.head)
    }
}