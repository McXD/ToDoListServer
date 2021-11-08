package hk.edu.polyu.comp4342.g17.controller

import hk.edu.polyu.comp4342.g17.dto.*
import hk.edu.polyu.comp4342.g17.service.PersistentTaskService
import hk.edu.polyu.comp4342.g17.service.TaskService
import org.bson.types.ObjectId
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/lists")
class TaskListController(
    private val taskService: TaskService
) {
    @PostMapping
    fun createList(@RequestBody taskListDTO: TaskListDTO): TaskListDTO {
        return taskService.createListFor(getUsername(), taskListDTO).toTaskListDTO()
    }

    @GetMapping
    fun getListsForUser(): List<TaskListDTO> {
        return taskService.getAllTaskListsForUser(getUsername()).map { it.toTaskListDTO() }
    }

    @GetMapping("/{taskListId}")
    fun getList(@PathVariable taskListId: ObjectId): TaskListDTO {
        return taskService.getTaskList(taskListId).get().toTaskListDTO()
    }

    @DeleteMapping("/{taskListId}")
    fun deleteList(@PathVariable taskListId: ObjectId) {
        taskService.deleteList(taskListId)
    }
}