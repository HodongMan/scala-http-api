package com.chickpea.singletask


import scala.language.postfixOps
import scala.concurrent.{ExecutionContext, Future}

import slick.dbio.DBIO

import com.chickpea.user.core.User
import com.chickpea.user.UserStorage
import com.chickpea.database.{DBIOOptional, StorageRunner}
import com.chickpea.util.ISO8601

import com.chickpea.singletask.singletasks._
import com.chickpea.singletask.SingleTaskStorage


class SingleTaskService(runner: StorageRunner,
                        singleTaskStorage: SingleTaskStorage,
                        userStorage: UserStorage)(implicit executionContext: ExecutionContext) {
    
    def getSingleTasks(request: SingleTaskRequest): Future[ForResponseSingleTasks] = {
        runner.run(for {
            singletasks <- singleTaskStorage.getSingleTasks(request)
            users <- userStorage
                .getUsersByUserIds(singletasks.map(_.userId))
                .map(a => a map (t => t.id -> t) toMap)
        } yield {
            ForResponseSingleTasks(
                singletasks.map(a => {
                    SingleTaskForResponse(
                        a.title,
                        a.description,
                        ISO8601(a.startDate),
                        ISO8601(a.endDate),
                        ISO8601(a.createdAt),
                        ISO8601(a.updatedAt)
                    )
                }),
                singletasks.length
            )
        })
    }

    def createSingleTask(userId: Long,
                         newSingleTask: SingleTaskPosted,
                         currentUserId: Option[Long]): Future[Option[ForResponseSingleTask]] =
        runner.runInTransaction(for {
            singleTask <- singleTaskStorage.createSingleTask(newSingleTask.create(userId))
            response <- getSingleTaskResponse(singleTask, currentUserId)
        } yield response)

    private def getSingleTaskResponse(singleTask: SingleTask, currentUserId: Option[Long]): DBIO[Option[ForResponseSingleTask]] = 
        (for {
            user <- DBIOOptional(userStorage.getUser(singleTask.userId))
        } yield ForResponseSingleTask(
            SingleTaskForResponse(
                singleTask.title,
                singleTask.description,
                ISO8601(singleTask.startDate),
                ISO8601(singleTask.endDate),
                ISO8601(singleTask.createdAt),
                ISO8601(singleTask.updatedAt),
            ))).dbio
}