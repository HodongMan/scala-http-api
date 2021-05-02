package com.chickpea.util


import akka.http.scaladsl.server.Directive1
import akka.http.scaladsl.server.directives.{
    BasicDirectives,
    HeaderDirectives,
    RouteDirectives
}

import pdi.jwt.{Jwt, JwtAlgorithm}
import io.circe.parser._
import io.circe.generic.auto._

import com.chickpea.user.core.AuthTokenContent
import javax.lang.model.element.ModuleElement.Directive


object JwtAuthDirectives {

    import BasicDirectives._
    import HeaderDirectives._
    import RouteDirectives._

    def authenticate(secretKey: String): Directive1[Long] = {
        headerValueByName("Authorization")
        .map(
            a =>
                Jwt.decodeRaw(a.split(" ").reverse.head, secretKey, Seq(JwtAlgorithm.HS256))
        )
        .map(_.toOption.flatMap(decode[AuthTokenContent](_).toOption))
        .flatMap {
            case Some(result) => provide(result.userId)
            case None => reject
        }
    }
}
