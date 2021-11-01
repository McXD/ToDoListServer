package hk.edu.polyu.comp4342.g17.controller

import hk.edu.polyu.comp4342.g17.dto.TaskDTO
import hk.edu.polyu.comp4342.g17.dto.toTaskDTO
import hk.edu.polyu.comp4342.g17.dto.toTaskModel
import hk.edu.polyu.comp4342.g17.service.PersistentTaskService
import org.bson.types.ObjectId
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/tasks")
class TaskController(
    private val taskService: PersistentTaskService
) {
    @PostMapping("{taskListId}")
    fun createTask(@RequestBody task: TaskDTO, @PathVariable taskListId: ObjectId): TaskDTO {
        return taskService.createTask(task.toTaskModel(), taskListId).toTaskDTO()
    }

    @GetMapping("{taskId}")
    fun getTask(@PathVariable taskId: ObjectId): TaskDTO {
        return taskService.getTask(taskId).get().toTaskDTO()
    }

    @DeleteMapping("{taskId}")
    fun deleteTask(@PathVariable taskId: ObjectId) {
        taskService.deleteTask(taskId)
    }
}