package otus.kotlinqa.database

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import otus.kotlinqa.models.Habit

object HabitDB : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", length = 64)
    var description = varchar("description", length = 512)

    override val primaryKey = PrimaryKey(id, name="PK_Habit_ID")

    fun toHabit(row: ResultRow?): Habit? {
        if (row==null) return null
        return Habit(
            id = row[id],
            name = row[name],
            description = row[description]
        )
    }
}

object HabitDBData {
    fun getHabit(id: Int): Habit? {
        initDB()
        return HabitDB.toHabit(transaction {
            val fd = HabitDB.select { HabitDB.id eq id }
            val result = fd.firstOrNull()
            HabitDB.select { HabitDB.id eq id }.firstOrNull()
        })
    }

    fun getHabitsList(): List<Habit?> {
        return transaction {
            HabitDB.selectAll()
        }.map {
            HabitDB.toHabit(it)
        }
    }

    fun deleteHabit(id: Int) {
        transaction {
            HabitDB.deleteWhere {
                HabitDB.id eq id
            }
        }
    }

    fun insertHabit(habit:Habit) {
        transaction {
            HabitDB.insert {
                it[id] = habit.id
                it[name] = habit.name
                it[description] = habit.description
            }
        }
    }

    fun updateHabit(id:Int, habit:Habit) {
        transaction {
            HabitDB.update({
                HabitDB.id eq id
            }) {
                it[HabitDB.id] = habit.id
                it[name] = habit.name
                it[description] = habit.description
            }
        }
    }
}