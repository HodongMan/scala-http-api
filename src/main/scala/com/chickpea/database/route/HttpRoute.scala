package com.chickpea.route



import scala.concurrent.ExecutionContext

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._

import com.chickpea.user.{UserRoute, UserService}
import com.chickpea.singletask.{SingleTaskRoute, SingleTaskService}


class HttpRoute(userService: UserService, singleTaskService: SingleTaskService, secretKey: String)(implicit executionContext: ExecutionContext) {

    private val userRouter = new UserRoute(secretKey, userService)
    private val singleTaskRouter = new SingleTaskRoute(secretKey, singleTaskService)

    val routes: Route = 
        cors() {
            pathPrefix("api") {
                userRouter.route ~
                    userRouter.route ~
                pathPrefix("healthcheck") {
                    get {
                        complete("OK")
                    }
                }
            }
        }
}