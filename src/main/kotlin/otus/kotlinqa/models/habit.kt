package otus.kotlinqa.models

@kotlinx.serialization.Serializable
data class Habit(val id:Int, var name:String, var description:String)
