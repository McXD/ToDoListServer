package hk.edu.polyu.comp4342.g17.service

import hk.edu.polyu.comp4342.g17.dto.TaskDTO
import hk.edu.polyu.comp4342.g17.dto.TaskListDTO
import org.bson.types.ObjectId
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

/**
 * Test [PersistentTaskService] with mocked repositories.
 */
@Testcontainers
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class PersistentTaskServiceTest {
    @Autowired
    lateinit var taskService: PersistentTaskService

    companion object {
        @Container
        val mongoDbContainer: MongoDBContainer = MongoDBContainer("mongo:5.0.3")

        @JvmStatic
        @DynamicPropertySource
        fun setUpProp(registry: DynamicPropertyRegistry) {
            registry.add("spring.data.mongodb.uri", mongoDbContainer::getReplicaSetUrl)
        }

        lateinit var taskListId: ObjectId
        lateinit var taskId: ObjectId
    }

    @Test
    @Order(1)
    fun `should create a task list for a user`() {
        val taskListDef = TaskListDTO(title = "Test List 1")
        val taskList = taskService.createListFor("user1", taskListDef)

        assertThat(taskList.title, equalTo("Test List 1"))

        taskListId = taskList.id
    }

    @Test
    @Order(2)
    fun `should retrieve the created list`() {
        val retrieved = taskService.getTaskList(taskListId).get()

        assertThat(retrieved.title, equalTo("Test List 1"))
    }

    @Test
    @Order(3)
    fun `can retrieve task lists for the given user`() {
        val retrieved = taskService.getAllTaskListsForUser("user1")
        retrieved.forEach {
            println(it.toString())
        }

        assertThat(retrieved, hasSize(1))
    }

    @Test
    @Order(4)
    fun `can create a task in a list`() {
        val taskDto = TaskDTO(
            listId = taskListId.toHexString(),
            title = "Task 1-1",
            details = "Task 1 in List 1"
        )

        val task = taskService.createTask(taskDto)

        assertThat(task.title, equalTo("Task 1-1"))

        taskId = task.id
    }

    @Test
    @Order(5)
    fun `can retrieve task list with task`() {
        val retrieved = taskService.getTaskList(taskListId).get()

        assertThat(retrieved.tasks, hasSize(1))
    }

    @Test
    @Order(6)
    fun `can mark task as finished`() {
        val finishedTask = taskService.updateTask(
            TaskDTO(
                id = taskId.toHexString(),
                isDone = true
            )
        )

        assertThat(finishedTask.isDone, equalTo(true))
    }

    @Test
    @Order(7)
    fun `can delete a task`() {
        taskService.deleteTask(taskId)

        val retrievedOptional = taskService.getTask(taskId)

        assertThat(retrievedOptional.isPresent, equalTo(false))
    }

    @Test
    @Order(8)
    fun `can delete a list`() {
        taskService.deleteList(taskListId)

        val retrievedOptional = taskService.getTaskList(taskListId)

        assertThat(retrievedOptional.isPresent, equalTo(false))
    }
}