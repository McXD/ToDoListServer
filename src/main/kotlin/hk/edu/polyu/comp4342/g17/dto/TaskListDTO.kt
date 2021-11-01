package hk.edu.polyu.comp4342.g17.dto

import hk.edu.polyu.comp4342.g17.model.TaskList
import org.bson.types.ObjectId

data class TaskListDTO(
    var id: String? = null,
    var owner: String? = "",
    var tasks: List<TaskDTO>? = null
)

fun TaskListDTO.toTaskListModel(): TaskList {
    return TaskList(
        ObjectId(id), owner, tasks?.map { it.toTaskModel() }?.toMutableList()
    )
}

fun TaskList.toTaskListDTO(): TaskListDTO {
    return TaskListDTO(
        id?.toHexString(), username, tasks?.map { it.toTaskDTO() }
    )
}