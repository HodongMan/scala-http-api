package com.chickpea.util


import pureconfig.loadConfig
import pureconfig.generic.auto._


case class Config(secretKey: String, http: HttpConfig, database: DatabaseConfig)


object Config {
    def load() = 
        loadConfig[Config] match {
            case Right(config) => config
            case Left(error) => throw new RuntimeException("Connot load config file " + error)
        }
}


private[util] case class HttpConfig(host: String, port: Int)
private[util] case class DatabaseConfig(host: String,
                                            port: String,
                                            db: String,
                                            username: String,
                                            password: String)