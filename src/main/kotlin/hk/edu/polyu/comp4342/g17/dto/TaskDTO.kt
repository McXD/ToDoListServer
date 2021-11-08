package hk.edu.polyu.comp4342.g17.dto

import hk.edu.polyu.comp4342.g17.model.Task
import org.bson.types.ObjectId
import java.util.*

data class TaskDTO(
    var id: String? = "",
    var title: String? = null,
    var details: String? = null,
    var isDone: Boolean? = null
)

fun TaskDTO.toTaskModel(): Task {
    return Task(
        ObjectId(id), title!!, details!!, isDone!!
    )
}

fun Task.toTaskDTO(): TaskDTO {
    return TaskDTO(
        id.toHexString(), title, detail, isDone
    )
}

fun Task.applyPatch(taskDTO: TaskDTO) {
    title = taskDTO.title ?: title
    detail = taskDTO.details ?: detail
    isDone = taskDTO.isDone ?: isDone
}