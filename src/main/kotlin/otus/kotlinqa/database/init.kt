package otus.kotlinqa.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun initDB() {
    Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false", driver = "org.h2.Driver", user = "root", password = "")
    transaction {
        SchemaUtils.create(HabitDB)
    }
}

fun removeDB() {
    transaction {
        SchemaUtils.dropDatabase("HabitDB")
    }
}