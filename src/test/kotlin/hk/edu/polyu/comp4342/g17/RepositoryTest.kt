package hk.edu.polyu.comp4342.g17

import hk.edu.polyu.comp4342.g17.model.Task
import hk.edu.polyu.comp4342.g17.repository.TaskRepository
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import kotlin.test.assertEquals

/**
 * Test the database access layer (repository), using the mongo test container.
 */
@Testcontainers
@DataMongoTest
class RepositoryTest {
    companion object {
        @Container
        val mongoDbContainer: MongoDBContainer = MongoDBContainer("mongo:5.0.3")

        @DynamicPropertySource
        fun setUpProp(registry: DynamicPropertyRegistry) {
            registry.add("spring.data.mongodb.uri", mongoDbContainer::getReplicaSetUrl)
        }
    }

    @Autowired lateinit var taskRepository: TaskRepository

    @Test
    fun `should insert a task`() {
        val task = Task(ObjectId(), "Do Homework", "Finish math homework 4")
        taskRepository.insert(task)

        assertEquals(task, taskRepository.findById(task.id).get())
    }
}