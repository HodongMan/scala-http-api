package com.chickpea.user

import scala.concurrent.ExecutionContext

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._
import io.circe.syntax._

import com.chickpea.user.core
import com.chickpea.user.core.UserRegistration


class UserRoute(secretKey: String, usersService: UserService)(
    implicit executionContext: ExecutionContext)
    extends FailFastCirceSupport {

  import com.chickpea.util.JwtAuthDirectives._
  import StatusCodes._
  import usersService._

  val route = pathPrefix("users") {
        path("login") {
            pathEndOrSingleSlash {
                post {
                    entity(as[LoginPasswordUser]) { idPass =>
                        complete(login(idPass.user.email, idPass.user.password).map {
                            case Some(user) => OK -> user.asJson
                            case None       => BadRequest -> None.asJson
                        })
                    }
                }
            }
        } ~
        pathEndOrSingleSlash {
            post {
                entity(as[RegisterRequest]) { req =>
                    complete(register(req.user).map { user =>
                        user.asJson
                    })
                }
            }
        }
    } ~
    path("user") {
        pathEndOrSingleSlash {
            authenticate(secretKey) { userId =>
                get {
                    complete(getCurrentUser(userId).map {
                        case Some(user) =>
                        OK -> user.asJson
                        case None =>
                        BadRequest -> None.asJson
                    })
                } ~
                put {
                    entity(as[UserUpdateParam]) { update =>
                        complete(updateUser(userId, update.user).map {
                            case Some(user) =>
                                OK -> user.asJson
                            case None =>
                                BadRequest -> None.asJson
                        })
                    }
                }
            }
        }
    }
}

private case class RegisterRequest(user: UserRegistration)

private case class LoginPasswordUser(user: LoginPassword)

private case class LoginPassword(email: String, password: String)
private case class UserProfile(username: String, email: String, bio: Option[String], image: Option[String])
private case class UserUpdateParam(user: core.UserUpdate)