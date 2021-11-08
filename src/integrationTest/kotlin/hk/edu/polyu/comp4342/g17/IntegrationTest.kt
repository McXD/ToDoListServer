package hk.edu.polyu.comp4342.g17

import hk.edu.polyu.comp4342.g17.dto.TaskDTO
import hk.edu.polyu.comp4342.g17.dto.TaskListDTO
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

/**
 * Test use cases, with no mocks.
 */
@TestMethodOrder(OrderAnnotation::class)
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@WithMockUser(username = "user1", password = "test", roles = ["user"])
class IntegrationTest {
    companion object {
        const val baseUrl: String = "/api/v1/"

        @Container
        val mongoDbContainer: MongoDBContainer = MongoDBContainer("mongo:5.0.3")

        @JvmStatic
        @DynamicPropertySource
        fun setUpProp(registry: DynamicPropertyRegistry) {
            registry.add("spring.data.mongodb.uri", mongoDbContainer::getReplicaSetUrl)
        }

        // make them static to reuse among test instances
        lateinit var taskListHexId: String
        lateinit var taskHexId: String
    }

    @Autowired lateinit var mockMvc: MockMvc

    @Test
    @Order(1)
    fun createTaskList() {
        val title = "My Test List"
        val taskListCreationDTO = TaskListDTO(title = title)

        mockMvc.post("$baseUrl/lists") {
            contentType = MediaType.APPLICATION_JSON
            content = asJsonString(taskListCreationDTO)
        }.andExpect {
            status().is2xxSuccessful
            content().contentType(MediaType.APPLICATION_JSON)
            jsonPath("$.id", not(emptyString())) // don't know that the id is yet
            jsonPath("$.title", equalTo(title))
            jsonPath("$.tasks", emptyIterable<TaskDTO>())
        }.andDo {
            print()
            handle {
                val createdTaskList = objectMapper.readValue(it.response.contentAsString, TaskListDTO::class.java)
                taskListHexId = createdTaskList.id
            }
        }
    }

    @Test
    @Order(2)
    fun createTask() {
        val taskDTO = TaskDTO(
            listId = taskListHexId,
            title = "Test Task 1",
            details = "Just a test task!",
        )

        mockMvc.post("$baseUrl/tasks") {
            contentType = MediaType.APPLICATION_JSON
            content = asJsonString(taskDTO)
        }.andExpect {
            status().is2xxSuccessful
            content().contentType(MediaType.APPLICATION_JSON)
        }.andDo {
            print()
            handle {
                val createdTask = objectMapper.readValue(it.response.contentAsString, TaskDTO::class.java)
                taskHexId = createdTask.id!!
            }
        }
    }

    @Test
    @Order(3)
    fun getTaskListsForUser() {
        mockMvc.get("$baseUrl/lists")
            .andExpect {
                status().is2xxSuccessful
                content().contentType(MediaType.APPLICATION_JSON)
                jsonPath("$.length()", equalTo(1))
                jsonPath("$.[0].tasks[0].id", equalTo(taskHexId))
            }.andDo {
                print()
            }
    }

    @Test
    @Order(4)
    fun markTaskAsDone() {
        val patch = TaskDTO(
            id = taskHexId,
            isDone = true,
        )

        mockMvc.patch("$baseUrl/tasks") {
            contentType = MediaType.APPLICATION_JSON
            content = asJsonString(patch)
        }.andExpect {
            status().is2xxSuccessful
            content().contentType(MediaType.APPLICATION_JSON)
            jsonPath("$.done", equalTo(true))
        }.andDo {
            print()
        }
    }

    @Test
    @Order(5)
    fun deleteTask() {
        mockMvc.delete("$baseUrl/tasks/$taskHexId")
            .andExpect {
                status().is2xxSuccessful
            }

        // check by getting
        mockMvc.get("$baseUrl/tasks/$taskHexId")
            .andExpect {
                status().is4xxClientError
            }.andDo {
                print()
            }
    }

    @Test
    @Order(6)
    fun retrieveEmptyList() {
        mockMvc.get("$baseUrl/lists")
            .andExpect {
                status().is2xxSuccessful
                content().contentType(MediaType.APPLICATION_JSON)
                jsonPath("$[0].tasks.length()", equalTo(0))
            }.andDo {
                print()
            }
    }

    @Test
    @Order(7)
    fun deleteList() {
        mockMvc.delete("$baseUrl/lists/$taskListHexId")
            .andExpect {
                status().is2xxSuccessful
            }

        // check the list is gone
        mockMvc.get("$baseUrl/lists/$taskListHexId")
            .andExpect {
                status().is4xxClientError
            }.andDo {
                print()
            }
    }
}

