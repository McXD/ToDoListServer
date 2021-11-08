package hk.edu.polyu.comp4342.g17.service

import hk.edu.polyu.comp4342.g17.dto.TaskDTO
import hk.edu.polyu.comp4342.g17.dto.TaskListDTO
import hk.edu.polyu.comp4342.g17.dto.applyPatch
import hk.edu.polyu.comp4342.g17.dto.toTaskModel
import hk.edu.polyu.comp4342.g17.model.Task
import hk.edu.polyu.comp4342.g17.model.TaskList
import hk.edu.polyu.comp4342.g17.repository.TaskListRepository
import hk.edu.polyu.comp4342.g17.repository.TaskRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.util.*
import kotlin.NoSuchElementException

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

    @Throws(
        NoSuchElementException::class, // when list is not found
        NullPointerException::class, // when element required is not specified
    )
    override fun createTask(taskDTO: TaskDTO): Task {
        requireNotNull(taskDTO.listId) { "id must be present"}
        val listId = ObjectId(taskDTO.listId)

        val taskList = getTaskList(listId).get()

        // initialise the task
        requireNotNull(taskDTO.title) { "title must be present" }

        taskDTO.id = ObjectId().toHexString() // assign id
        taskDTO.isDone = false // newly created task is not done
        taskDTO.listId = listId.toHexString() // set list id

        val taskCreated = taskRepo.insert(taskDTO.toTaskModel()) // everything else can be provided by the user

        // update the task list
        taskList.addTask(taskCreated)
        taskListRepo.save(taskList)

        return taskCreated
    }

    override fun getTask(taskId: ObjectId): Optional<Task> {
        return taskRepo.findById(taskId)
    }

    @Throws(NoSuchElementException::class)
    override fun updateTask(patch: TaskDTO): Task {
        val task = taskRepo.findById(ObjectId(patch.id)).get()
        taskRepo.deleteById(task.id) //TODO: use update method instead of delete and insert new

        task.applyPatch(patch)
        return taskRepo.insert(task)
    }

    @Throws(NoSuchElementException::class)
    override fun deleteTask(taskId: ObjectId) {
        // need to delete task reference in the list as well
        val taskToDelete = getTask(taskId).get()
        val taskListToModify = taskListRepo.findById(taskToDelete.listId).get()

        taskListToModify.deleteTask(taskToDelete)

        taskListRepo.deleteById(taskToDelete.listId)
        taskListRepo.insert(taskListToModify)

        taskRepo.deleteById(taskToDelete.id)
    }
}