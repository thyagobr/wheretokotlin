package com.whereto.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.ApplicationConfig
import org.jetbrains.exposed.v1.jdbc.Database

object DatabaseFactory {

    fun init(config: ApplicationConfig) {
        val url = config.property("database.url").getString()
        val driver = config.property("database.driver").getString()
        val user = config.property("database.user").getString()
        val password = config.property("database.password").getString()

        val hikariConfig = HikariConfig()
        hikariConfig.jdbcUrl = url
        hikariConfig.driverClassName = driver
        hikariConfig.username = user
        hikariConfig.password = password
        hikariConfig.maximumPoolSize = 10

        val dataSource = HikariDataSource(hikariConfig)
        Database.connect(dataSource)
    }
}
