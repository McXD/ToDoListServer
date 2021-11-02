package hk.edu.polyu.comp4342.g17.service

import hk.edu.polyu.comp4342.g17.dto.TaskDTO
import hk.edu.polyu.comp4342.g17.dto.TaskListDTO
import hk.edu.polyu.comp4342.g17.model.Task
import hk.edu.polyu.comp4342.g17.model.TaskList
import hk.edu.polyu.comp4342.g17.model.User
import hk.edu.polyu.comp4342.g17.repository.TaskListRepository
import hk.edu.polyu.comp4342.g17.repository.TaskRepository
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.NullPointerException
import java.util.*

@Service
class PersistentTaskService(
    var taskRepo: TaskRepository,
    var taskListRepo: TaskListRepository
): TaskService {
    @Throws(NullPointerException::class)
    override fun createListFor(username: String, baseList: TaskListDTO): TaskList {
        return taskListRepo.insert(
            TaskList(ObjectId(),
                baseList.title ?: throw NullPointerException("title"),
                username))
    }

    override fun getTaskList(listId: ObjectId): Optional<TaskList> {
        return taskListRepo.findById(listId)
    }

    override fun getAllTaskListsForUser(username: String): List<TaskList> {
        return taskListRepo.findByUsername(username)
    }

    override fun updateTaskList(patch: TaskListDTO): TaskList {
        TODO("Not yet implemented")
    }


    @Throws(NoSuchElementException::class)
    override fun deleteList(listId: ObjectId) {
        taskListRepo.delete(getTaskList(listId).get())
    }

    @Throws(NoSuchElementException::class)
    override fun createTask(task: Task, listId: ObjectId): Task {
        val taskCreated = taskRepo.insert(task)

        // update the task list
        val taskList = getTaskList(listId).get()
        taskList.addTask(task)
        taskListRepo.save(taskList)

        return taskCreated
    }

    override fun getTask(taskId: ObjectId): Optional<Task> {
        return taskRepo.findById(taskId)
    }

    override fun updateTask(patch: TaskDTO): Task {
        TODO("Not yet implemented")
    }


    @Throws(NoSuchElementException::class)
    override fun deleteTask(taskId: ObjectId) {
        taskRepo.delete(getTask(taskId).get())
    }
}