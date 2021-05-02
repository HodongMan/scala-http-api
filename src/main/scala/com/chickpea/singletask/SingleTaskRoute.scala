package com.chickpea.singletask


import scala.concurrent.ExecutionContext

import io.circe.generic.auto._
import io.circe.syntax._

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport

import com.chickpea.singletask.singletasks._


class SingleTaskRoute(secretKey: String, singleTaskService: SingleTaskService) 
    (implicit executionContext: ExecutionContext) extends FailFastCirceSupport  {

    import akka.http.scaladsl.model.StatusCodes._
    import singleTaskService._
    import com.chickpea.util.JwtAuthDirectives._


    val route = pathPrefix("singletask") {
        pathEnd {
        get {
            parameters('userName.?,
                       'limit.as[Long].?,
                       'offset.as[Long].?).as(SingleTaskRequest) { request =>
                complete(getSingleTasks(request).map(_.asJson))
            }
        } ~
            post {
                authenticate(secretKey) { userId =>
                    entity(as[CreateSingleTask]) { singleTask =>
                        complete(
                            createSingleTask(userId, singleTask.singleTask, Some(userId)).map {
                                singleTask =>
                                    singleTask.asJson
                            }
                        )
                    }
                }
            }
        }
    }
}


private case class CreateSingleTask(singleTask: SingleTaskPosted)