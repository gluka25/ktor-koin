package otus.kotlinqa.modules

import org.koin.dsl.module
import otus.kotlinqa.api.RestAPI
import otus.kotlinqa.api.RestAPITestImpl

val habitsTestModule = module {
    single<RestAPI> { RestAPITestImpl() }
}