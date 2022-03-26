package otus.kotlinqa.api

import otus.kotlinqa.data.HabitTestData
import otus.kotlinqa.models.Habit

class RestAPITestImpl : RestAPI {

    override fun getHabit(id: Int): Habit? = HabitTestData.getHabit(id)

    override fun addHabit(habit: Habit) = HabitTestData.insertHabit(habit)

    override fun updateHabit(id: Int, habit: Habit) = HabitTestData.updateHabit(id, habit)

    override fun deleteHabit(id: Int) = HabitTestData.deleteHabit(id)

    override fun getHabitsList(): List<Habit?> = HabitTestData.getHabitsList()
}