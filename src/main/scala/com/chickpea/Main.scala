package com.chickpea

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

import com.chickpea.util.Config
import com.chickpea.database.{DatabaseConnector, DatabaseMigrateManager, StorageRunner}
import com.chickpea.user.{JdbcUserStorage, UserService}
import com.chickpea.singletask.{JdbcSingleTaskStorage, SingleTaskService}
import com.chickpea.route.HttpRoute


object Main extends App {

    def startServerApplication() = {

        implicit val system: ActorSystem = ActorSystem("chickpea-server")
        implicit val executor: ExecutionContext = system.dispatcher
        implicit val materializer: ActorMaterializer = ActorMaterializer()

        val config = Config.load()
        val jdbcURL = s"jdbc:mysql://${config.database.host}:${config.database.port}/${config.database.db}?serverTimezone=UTC"

        val databaseConnector = new DatabaseConnector(jdbcURL, config.database.username, config.database.password)
        val flywayService = new DatabaseMigrateManager(jdbcURL, config.database.username, config.database.password)
        flywayService.migrateDatabaseSchema()

        val userStorage = new JdbcUserStorage()

        val singleTaskStorage = new JdbcSingleTaskStorage()

        val storageRunner = new StorageRunner(databaseConnector)

        val userService = new UserService(storageRunner, userStorage, config.secretKey)

        val singleTaskService = new SingleTaskService(storageRunner, singleTaskStorage, userStorage)

        val httpRoute = new HttpRoute(userService, singleTaskService, config.secretKey)

        Http().bindAndHandle(httpRoute.routes, config.http.host, config.http.port)

        println(s"Working at http://${config.http.host}:${config.http.port}/")

        Await.result(system.whenTerminated, Duration.Inf)
    }

    startServerApplication()
}