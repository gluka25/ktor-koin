package otus.kotlinqa

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import org.koin.core.context.startKoin
import otus.kotlinqa.database.initDB
import otus.kotlinqa.database.removeDB
import otus.kotlinqa.models.Habit
import otus.kotlinqa.plugins.configureRouting
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Before
    fun init() {
        //todo: exclude after test module
        initDB()
        startKoin {
            //todo: replace with test module
            modules(habitsModule)
        }
    }

    @After
    fun remove() {
        removeDB()
    }

    @Test
    fun testCreating() {
        withTestApplication({ configureRouting() }) {
            application.install(ContentNegotiation) {
                json()
            }
            val habits_empty = handleRequest(HttpMethod.Get, "/habit/1")
            assertEquals(404, habits_empty.response.status()!!.value)
            val result = handleRequest(
                HttpMethod.Post, "/habit"
            ) {
                addHeader("Content-Type", "application/json")
                val testHabit = Habit(id = 1, name = "test", description = "Test description")
                setBody(Json.encodeToString(Habit.serializer(), testHabit))
            }
            assertEquals(200, result.response.status()?.value)
            val habits = handleRequest(HttpMethod.Get, "/habit/1")
            assertEquals(200, habits.response.status()!!.value)
            val data = Json.decodeFromString(Habit.serializer(), habits.response.content!!)
            assertEquals("test", data.name)
            assertEquals("Test description", data.description)
        }
    }

    fun testDeleteTask() {
        //todo: implement
    }

    fun testTasksListAfterCreation() {
        //todo: implement
    }

    fun testTaskUpdate() {
        //todo: implement
    }
}