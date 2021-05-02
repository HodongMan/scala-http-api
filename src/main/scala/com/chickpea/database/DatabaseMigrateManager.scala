package com.chickpea.database

import org.flywaydb.core.Flyway


class DatabaseMigrateManager(jdbcUrl: String, dbUser: String, dbPassword: String) {

    var flyway = Flyway.configure().dataSource(jdbcUrl, dbUser, dbPassword).load()

    def migrateDatabaseSchema(): Unit = flyway.migrate()

    def dropDatabase(): Unit = flyway.clean()
}