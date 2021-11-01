package hk.edu.polyu.comp4342.g17.dto

import hk.edu.polyu.comp4342.g17.model.Task
import org.bson.types.ObjectId
import java.util.*

data class TaskDTO(
    var id: String? = null,
    var title: String? = "",
    var details: String? = "",
    var due: Date? = null
)

fun TaskDTO.toTaskModel(): Task {
    return Task(
        ObjectId(id), title, details, due
    )
}

fun Task.toTaskDTO(): TaskDTO {
    return TaskDTO(
        id?.toHexString(), title, detail, due
    )
}