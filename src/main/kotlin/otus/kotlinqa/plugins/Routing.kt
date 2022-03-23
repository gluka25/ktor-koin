package otus.kotlinqa.plugins

import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import org.koin.ktor.ext.inject
import otus.kotlinqa.api.RestAPI
import otus.kotlinqa.models.Habit

fun Application.configureRouting() {

    val api by inject<RestAPI>()

    routing {
        get("/habits") {
            call.respond(api.getHabitsList())
        }
        get("/habit/{id}") {
            val id = call.parameters["id"]
            if (id == null) {
                call.respond(status = HttpStatusCode.BadRequest, message = "No id specified")
            } else {
                val habit = api.getHabit(id.toInt())
                if (habit != null) {
                    call.respond(habit)
                } else {
                    call.respond(status = HttpStatusCode.NotFound, "Not found")
                }
            }
        }
        put("/habit/{id}") {
            val id = call.parameters["id"]
            if (id == null) {
                call.respond(status = HttpStatusCode.BadRequest, "No ID specified")
            } else {
                val data = call.receive<Habit>()
                api.updateHabit(id.toInt(), data)
                call.respond(status = HttpStatusCode.OK, message = "Updated")
            }
        }
        post("/habit") {
            val data = call.receive<Habit>()
            api.addHabit(data)
            call.respond(status = HttpStatusCode.OK, message = "Created")
        }
        delete("/habit/{id}") {
            val id = call.parameters["id"]
            if (id == null) {
                call.respond(status = HttpStatusCode.BadRequest, "No ID specified")
            } else {
                api.deleteHabit(id.toInt())
                call.respond(status = HttpStatusCode.OK, message = "Deleted")
            }
        }
    }
}
