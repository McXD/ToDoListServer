package hk.edu.polyu.comp4342.g17.service

import hk.edu.polyu.comp4342.g17.model.Task
import hk.edu.polyu.comp4342.g17.model.TaskList
import org.bson.types.ObjectId
import java.util.*

interface TaskService {
    fun createListFor(username: String): TaskList

    fun getTaskList(listId: ObjectId): Optional<TaskList>

    fun getAllTaskListsForUser(username: String): List<TaskList>

    @Throws(NoSuchElementException::class)
    fun updateTaskList(taskListPatch: TaskList): TaskList

    @Throws(NoSuchElementException::class)
    fun deleteList(listId: ObjectId)

    @Throws(NoSuchElementException::class)
    fun createTask(task: Task, listId: ObjectId): Task

    fun getTask(taskId: ObjectId): Optional<Task>

    @Throws(NoSuchElementException::class)
    fun updateTask(taskPatch: Task): Task

    @Throws(NoSuchElementException::class)
    fun deleteTask(taskId: ObjectId)
}