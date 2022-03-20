package otus.kotlinqa.models

@kotlinx.serialization.Serializable
data class Habit(val id:Int, val name:String, val description:String)
