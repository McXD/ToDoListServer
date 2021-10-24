package hk.edu.polyu.comp4342.g17.controller

import hk.edu.polyu.comp4342.g17.dto.TaskDTO
import hk.edu.polyu.comp4342.g17.model.Task
import hk.edu.polyu.comp4342.g17.repository.TaskRepository
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/tasks")
class TaskController(
    private val taskRepository: TaskRepository
) {
    @GetMapping
    fun allTasks(): List<TaskDTO> {
        return taskRepository.findAll().map {
            TaskDTO(it.title, it.detail, it.due)
        }
    }

    @PostMapping
    fun createTask(@RequestBody taskDTO: TaskDTO): TaskDTO {
        val task = taskRepository.insert(Task(taskDTO.title, taskDTO.details, taskDTO.due))
        return TaskDTO(task.title, task.detail, task.due)
    }
}