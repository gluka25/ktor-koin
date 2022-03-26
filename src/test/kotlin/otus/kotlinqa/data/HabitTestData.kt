package otus.kotlinqa.data

import otus.kotlinqa.models.Habit


object HabitTestData {
    var habitList = mutableListOf<Habit>()

    fun getHabit(id: Int): Habit? {
        return habitList.find { it.id == id }
    }

    fun getHabitsList(): List<Habit?> {
        return habitList
    }

    fun deleteHabit(id: Int) {
        habitList.remove(habitList.find { it.id == id })
    }

    fun insertHabit(habit: Habit) {
        habitList.add(habit)
    }

    fun updateHabit(id:Int, habit: Habit) {
        val item = habitList.find { it.id == id }
        item?.name = habit.name
        item?.description = habit.description
    }
}