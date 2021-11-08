package hk.edu.polyu.comp4342.g17.service

import hk.edu.polyu.comp4342.g17.dto.TaskDTO
import hk.edu.polyu.comp4342.g17.dto.TaskListDTO
import hk.edu.polyu.comp4342.g17.model.Task
import hk.edu.polyu.comp4342.g17.model.TaskList
import org.bson.types.ObjectId
import java.util.*

interface TaskService {
    /**
     * Create an empty list for the user
     */
    fun createListFor(username: String, baseList: TaskListDTO): TaskList

    /**
     * Fetch the TaskList based on id
     */
    fun getTaskList(listId: ObjectId): Optional<TaskList>

    /**
     * Fetch all the [TaskList]s for the user
     */
    fun getAllTaskListsForUser(username: String): List<TaskList>

    @Throws(NoSuchElementException::class)
    fun updateTaskList(patch: TaskListDTO): TaskList

    @Throws(NoSuchElementException::class)
    fun deleteList(listId: ObjectId)

    @Throws(NoSuchElementException::class)
    fun createTask(task: TaskDTO, listId: ObjectId): Task

    fun getTask(taskId: ObjectId): Optional<Task>

    @Throws(NoSuchElementException::class)
    fun updateTask(patch: TaskDTO): Task

    @Throws(NoSuchElementException::class)
    fun deleteTask(taskId: ObjectId)
}