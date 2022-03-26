package otus.kotlinqa

import io.ktor.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.core.logger.Level
import org.koin.dsl.module
import org.koin.ktor.ext.Koin
import org.koin.logger.slf4jLogger
import otus.kotlinqa.api.RestAPI
import otus.kotlinqa.api.RestAPIImpl
import otus.kotlinqa.api.RestAPITestImpl
import otus.kotlinqa.plugins.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureRouting()
        configureSerialization()
        install(Koin) {
            slf4jLogger(level = Level.DEBUG)
            modules(habitsModule)
        }
    }.start(wait = true)
}

val habitsModule = module {
    single<RestAPI> { RestAPIImpl() }
}

val habitsTestModule = module {
    single<RestAPI> { RestAPITestImpl() }
}