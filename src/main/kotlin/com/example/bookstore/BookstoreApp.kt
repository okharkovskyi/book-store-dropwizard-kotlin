package com.example.bookstore

import com.example.bookstore.db.DatabaseModule
import com.example.bookstore.db.FlywayMigration
import com.google.inject.Stage
import io.dropwizard.Application
import io.dropwizard.Configuration
import io.dropwizard.configuration.ResourceConfigurationSourceProvider
import io.dropwizard.db.DataSourceFactory
import io.dropwizard.flyway.FlywayBundle
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import ru.vyarus.dropwizard.guice.GuiceBundle
import ru.vyarus.guicey.eventbus.EventBusBundle

class BookstoreConfiguration(
    var database: DataSourceFactory = DataSourceFactory()
) : Configuration()

class BookstoreApp : Application<BookstoreConfiguration>() {
    override fun run(configuration: BookstoreConfiguration?, environment: Environment?) {
        guiceBundle.injector.getInstance(FlywayMigration::class.java).run()
    }

    private lateinit var guiceBundle: GuiceBundle
    override fun initialize(bootstrap: Bootstrap<BookstoreConfiguration>?) {
        guiceBundle = GuiceBundle.builder()
            .modules(DatabaseModule())
            .bundles(EventBusBundle())
            .enableAutoConfig("com.example")
            .build(Stage.DEVELOPMENT) // See the infamous https://github.com/HubSpot/dropwizard-guice/issues/19


        bootstrap?.addBundle(guiceBundle)

        bootstrap?.configurationSourceProvider = ResourceConfigurationSourceProvider()

        bootstrap?.addBundle(object : FlywayBundle<BookstoreConfiguration>() {
            override fun getDataSourceFactory(configuration: BookstoreConfiguration): DataSourceFactory {
                return configuration.database
            }
        })

        super.initialize(bootstrap)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            BookstoreApp().run(*args)
        }
    }
}