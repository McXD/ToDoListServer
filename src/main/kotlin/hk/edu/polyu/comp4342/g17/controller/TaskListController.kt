package hk.edu.polyu.comp4342.g17.controller

import hk.edu.polyu.comp4342.g17.dto.TaskListDTO
import hk.edu.polyu.comp4342.g17.dto.toTaskListDTO
import hk.edu.polyu.comp4342.g17.dto.toTaskListModel
import hk.edu.polyu.comp4342.g17.model.TaskList
import hk.edu.polyu.comp4342.g17.service.PersistentTaskService
import org.bson.types.ObjectId
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/lists")
class TaskListController(
    private val taskService: PersistentTaskService
) {
    @PostMapping
    fun createList(): TaskListDTO {
        return taskService.createListFor(getUsername()).toTaskListDTO()
    }

    @GetMapping
    fun getListsForUser(): List<TaskListDTO> {
        return taskService.getAllTaskListsForUser(getUsername()).map { it.toTaskListDTO() }
    }

    @GetMapping("/{taskListId}")
    fun getList(@PathVariable taskListId: ObjectId): TaskListDTO {
        return taskService.getTaskList(taskListId).get().toTaskListDTO()
    }

    @PatchMapping
    fun updateList(@RequestBody taskListPatchDTO: TaskListDTO): TaskList {
        return taskService.updateTaskList(taskListPatchDTO.toTaskListModel())
    }

    private fun getUsername() = SecurityContextHolder.getContext().authentication.name
}