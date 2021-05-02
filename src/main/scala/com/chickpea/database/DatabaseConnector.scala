package com.chickpea.database

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}


class DatabaseConnector(jdbcUrl: String, dbUser: String, dbPassword: String) {

    private val hikariDataSource = {
        
        val hikariConfig = new HikariConfig()
        
        hikariConfig.setDriverClassName("com.mysql.jdbc.Driver");
        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setJdbcUrl(jdbcUrl)
        hikariConfig.setUsername(dbUser)
        hikariConfig.setPassword(dbPassword)

        new HikariDataSource(hikariConfig)
    }

    val profile = slick.jdbc.MySQLProfile
    import profile.api._

    val db = Database.forDataSource(hikariDataSource, None)
    db.createSession()

}