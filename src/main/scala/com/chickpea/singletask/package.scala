package com.chickpea.singletask

import java.sql.Timestamp
import java.util.Date

import com.roundeights.hasher.Implicits._

import com.chickpea.util.ISO8601

package object singletasks {

    case class ForResponseSingleTasks(singleTasks: Seq[SingleTaskForResponse], singleTaskCount: Int)

    case class ForResponseSingleTask(singleTask: SingleTaskForResponse)

    case class SingleTaskUpdated( title: Option[String],
                                  description: Option[String])

    case class SingleTaskPosted( title: String,
                                 description: String) {
        def create(userId: Long): SingleTask = {
            SingleTask(0, 
                       title, 
                       description, 
                       userId, 
                       new Timestamp((new Date).getTime), 
                       new Timestamp((new Date).getTime), 
                       new Timestamp((new Date).getTime), 
                       new Timestamp((new Date).getTime))
        }
    }

    case class SingleTaskForResponse( title: String,
                                      description: String,
                                      startDate: String,
                                      endDate: String,
                                      createdAt: String,
                                      updatedAt: String)

    case class SingleTaskRequest( userName: Option[String],
                                  limit: Option[Long],
                                  offset: Option[Long])
}