package hk.edu.polyu.comp4342.g17.dto

import hk.edu.polyu.comp4342.g17.model.TaskList

data class TaskListDTO(
    var id: String = "",
    var title: String? = null,
    var tasks: List<TaskDTO>? = null
)

fun TaskList.toTaskListDTO(): TaskListDTO {
    return TaskListDTO(
        id.toHexString(), title, tasks.map { it.toTaskDTO() }
    )
}