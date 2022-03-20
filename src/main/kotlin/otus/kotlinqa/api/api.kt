package otus.kotlinqa.api

import otus.kotlinqa.database.HabitDBData
import otus.kotlinqa.models.Habit

interface RestAPI {
    fun getHabit(id: Int): Habit?
    fun addHabit(habit: Habit)
    fun updateHabit(id: Int, habit: Habit)
    fun deleteHabit(id: Int)
    fun getHabitsList(): List<Habit?>
}

class RestAPIImpl : RestAPI {

    override fun getHabit(id: Int): Habit? = HabitDBData.getHabit(id)

    override fun addHabit(habit: Habit) = HabitDBData.insertHabit(habit)

    override fun updateHabit(id: Int, habit: Habit) = HabitDBData.updateHabit(id, habit)

    override fun deleteHabit(id: Int) = HabitDBData.deleteHabit(id)

    override fun getHabitsList(): List<Habit?> = HabitDBData.getHabitsList()
}