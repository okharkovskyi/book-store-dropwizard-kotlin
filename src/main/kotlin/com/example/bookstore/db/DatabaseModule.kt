package com.example.bookstore.db

import com.codahale.metrics.health.HealthCheck
import com.example.bookstore.BookstoreConfiguration
import com.google.inject.AbstractModule
import com.google.inject.Inject
import com.google.inject.Provides
import com.google.inject.Singleton
import io.dropwizard.db.DataSourceFactory
import io.dropwizard.setup.Environment
import org.flywaydb.core.Flyway
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.jooq.impl.DataSourceConnectionProvider
import org.jooq.impl.DefaultConfiguration
import org.jooq.impl.ThreadLocalTransactionProvider
import org.slf4j.LoggerFactory
import javax.sql.DataSource

class DatabaseModule : AbstractModule() {

    @Provides
    @Singleton
    fun dslContext(dataSource: DataSource): DSLContext {
        val connectionProvider = DataSourceConnectionProvider(dataSource)
        return DSL.using(
            DefaultConfiguration().set(connectionProvider).set(SQLDialect.POSTGRES)
                .set(ThreadLocalTransactionProvider(connectionProvider))
        )
    }

    @Provides
    @Singleton
    fun dataSource(environment: Environment, configuration: BookstoreConfiguration): DataSource {
        return configuration.database.build(environment.metrics(), "test")
    }

}

@Singleton
class FlywayMigration @Inject constructor(private val environment: Environment,
                                          private val dataSource: DataSource) : Runnable {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun run() {
        val flyway = Flyway.configure().dataSource(dataSource).baselineOnMigrate(true).outOfOrder(true).load()
        try {
            flyway.migrate()
        } catch (e: Exception) {
            logger.error("Flyway migration failed", e)
            environment.healthChecks().register("flyway", FlywayMigrationFailed(e))
        }
    }
}

class FlywayMigrationFailed(private val error: Exception) : HealthCheck() {

    override fun check(): Result {
        return Result.unhealthy(error)
    }
}