package io.adetalhouet.directory.db

import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.adetalhouet.directory.db.model.PersonTable
import io.ktor.config.HoconApplicationConfig
import io.ktor.util.KtorExperimentalAPI
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.KoinComponent
import org.slf4j.LoggerFactory

@KtorExperimentalAPI
class DatabaseConfiguration : KoinComponent {

    private val appConfig = HoconApplicationConfig(ConfigFactory.load())
    private val dbUrl = appConfig.property("db.jdbcUrl").getString()
    private val dbUser = appConfig.property("db.dbUser").getString()
    private val dbPassword = appConfig.property("db.dbPassword").getString()
    private val dbDriver = appConfig.property("db.driver").getString()

    private val log = LoggerFactory.getLogger(DatabaseConfiguration::class.java)

    init {
        log.info("STARTED")
    }

    init {
        Database.connect(hikari())

        transaction {
            SchemaUtils.create(PersonTable)
        }
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig().apply {
            driverClassName = dbDriver
            jdbcUrl = dbUrl
            username = dbUser
            password = dbPassword
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        }
        config.validate()
        return HikariDataSource(config)
    }
}


object DatabaseFactory {
    @KtorExperimentalAPI
    suspend fun <T> dbQuery(block: () -> T): T = newSuspendedTransaction { block() }
}
