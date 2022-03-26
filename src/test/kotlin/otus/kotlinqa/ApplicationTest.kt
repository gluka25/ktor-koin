package otus.kotlinqa

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.testing.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import org.koin.core.context.startKoin
import otus.kotlinqa.database.initDB
import otus.kotlinqa.database.removeDB
import otus.kotlinqa.models.Habit
import otus.kotlinqa.modules.habitsTestModule
import otus.kotlinqa.plugins.configureRouting
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Before
    fun init() {
        //todo: exclude after test module
        //initDB()
        startKoin {
            //todo: replace with test module
            //modules(habitsModule)
            modules(habitsTestModule)
        }
    }

//    @After
//    fun remove() {
//        removeDB()
//    }

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

    @Test
    fun testDeleteTask() {
        withTestApplication({ configureRouting() }) {
            application.install(ContentNegotiation) {
                json()
            }

            val result = handleRequest(
                HttpMethod.Post, "/habit"
            ) {
                addHeader("Content-Type", "application/json")
                val testHabit = Habit(id = 1, name = "testD", description = "Habit for delete")
                setBody(Json.encodeToString(Habit.serializer(), testHabit))
            }
            assertEquals(200, result.response.status()?.value)

            handleRequest(
                HttpMethod.Post, "/habit"
            ) {
                addHeader("Content-Type", "application/json")
                val testHabit = Habit(id = 2, name = "test2", description = "Habit for delete2")
                setBody(Json.encodeToString(Habit.serializer(), testHabit))
            }

            val habits = handleRequest(HttpMethod.Get, "/habit/1")
            assertEquals(200, habits.response.status()!!.value)

            val habits2 = handleRequest(HttpMethod.Get, "/habit/2")
            assertEquals(200, habits2.response.status()!!.value)

            handleRequest(HttpMethod.Delete, "/habit/1").apply { assertEquals(200, response.status()?.value) }
            handleRequest(HttpMethod.Get, "/habit/1").apply { assertEquals(404, response.status()?.value)  }
            handleRequest(HttpMethod.Get, "/habit/2").apply { assertEquals(200, response.status()?.value)  }

        }
    }

    @Test
    fun testTasksListAfterCreation() {
        withTestApplication({ configureRouting() }) {
            application.install(ContentNegotiation) {
                json()
            }

            handleRequest(
                HttpMethod.Post, "/habit"
            ) {
                addHeader("Content-Type", "application/json")
                val testHabit = Habit(id = 1, name = "test1", description = "Habit 1")
                setBody(Json.encodeToString(Habit.serializer(), testHabit))
            }

            handleRequest(
                HttpMethod.Post, "/habit"
            ) {
                addHeader("Content-Type", "application/json")
                val testHabit = Habit(id = 2, name = "test2", description = "Habit 2")
                setBody(Json.encodeToString(Habit.serializer(), testHabit))
            }

            val habit1 = handleRequest(HttpMethod.Get, "/habit/1")
            assertEquals(200, habit1.response.status()!!.value)

            val habit2 = handleRequest(HttpMethod.Get, "/habit/2")
            assertEquals(200, habit2.response.status()!!.value)

            val getList = handleRequest(HttpMethod.Get, "/habits")
            assertEquals(200, getList.response.status()?.value)

            val habitList = Json.decodeFromString(ListSerializer(Habit.serializer()), getList.response.content!!)
            assertEquals("test1", habitList[0].name)
            assertEquals("test2", habitList[1].name)
        }
    }

    @Test
    fun testTaskUpdate() {
        withTestApplication({ configureRouting() }) {
            application.install(ContentNegotiation) {
                json()
            }
            handleRequest(
                HttpMethod.Post, "/habit"
            ) {
                addHeader("Content-Type", "application/json")
                val testHabit = Habit(id = 1, name = "test", description = "Test description")
                setBody(Json.encodeToString(Habit.serializer(), testHabit))
            }

            val update = handleRequest(HttpMethod.Put,"/habit/1"){
                addHeader("Content-Type", "application/json")
                val testHabit = Habit(id = 1, name = "test2", description = "Test description2")
                setBody(Json.encodeToString(Habit.serializer(), testHabit))
            }
            assertEquals(200, update.response.status()!!.value)

            val habitNew = handleRequest(HttpMethod.Get, "/habit/1")
            assertEquals(200, habitNew.response.status()!!.value)
            val data = Json.decodeFromString(Habit.serializer(), habitNew.response.content!!)
            assertEquals("test2", data.name)
            assertEquals("Test description2", data.description)
        }
    }
}