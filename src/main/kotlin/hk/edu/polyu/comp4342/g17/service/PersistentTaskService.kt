package hk.edu.polyu.comp4342.g17.service

import hk.edu.polyu.comp4342.g17.model.Task
import hk.edu.polyu.comp4342.g17.model.TaskList
import hk.edu.polyu.comp4342.g17.model.User
import hk.edu.polyu.comp4342.g17.repository.TaskListRepository
import hk.edu.polyu.comp4342.g17.repository.TaskRepository
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class PersistentTaskService {
    @Autowired private lateinit var taskRepo: TaskRepository
    @Autowired private lateinit var taskListRepo: TaskListRepository

    fun createListFor(username: String): TaskList {
        return taskListRepo.insert(
            TaskList(ObjectId(), username))
    }

    fun getTaskList(listId: ObjectId): Optional<TaskList> {
        return taskListRepo.findById(listId)
    }

    fun getAllTaskListsForUser(username: String): List<TaskList> {
        return taskListRepo.findByUsername(username)
    }

    @Throws(NoSuchElementException::class)
    fun updateTaskList(taskListPatch: TaskList): TaskList {
        val list = getTaskList(taskListPatch.id!!).get()
        list.applyPatch(taskListPatch)
        return taskListRepo.save(list) // overwrite?
    }

    @Throws(NoSuchElementException::class)
    fun deleteList(listId: ObjectId) {
        taskListRepo.delete(getTaskList(listId).get())
    }

    @Throws(NoSuchElementException::class)
    fun createTask(task: Task, listId: ObjectId): Task {
        val taskCreated = taskRepo.insert(task)

        // update the task list
        val taskList = getTaskList(listId).get()
        taskList.addTask(task)
        taskListRepo.save(taskList)

        return taskCreated
    }

    fun getTask(taskId: ObjectId): Optional<Task> {
        return taskRepo.findById(taskId)
    }

    @Throws(NoSuchElementException::class)
    fun updateTask(taskPatch: Task): Task {
        val task = getTask(taskPatch.id!!).get()
        task.applyPatch(taskPatch)

        return taskRepo.save(task)
    }

    @Throws(NoSuchElementException::class)
    fun deleteTask(taskId: ObjectId) {
        taskRepo.delete(getTask(taskId).get())
    }
}