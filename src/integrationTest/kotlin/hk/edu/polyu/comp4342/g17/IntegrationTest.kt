package hk.edu.polyu.comp4342.g17

import hk.edu.polyu.comp4342.g17.dto.TaskDTO
import hk.edu.polyu.comp4342.g17.dto.TaskListDTO
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

/**
 * Test use cases, with no mocks.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class IntegrationTest {
    companion object {
        const val baseUrl: String = "/api/v1/"

        @Container
        val mongoDbContainer: MongoDBContainer = MongoDBContainer("mongo:5.0.3")

        @DynamicPropertySource
        fun setUpProp(registry: DynamicPropertyRegistry) {
            registry.add("spring.data.mongodb.uri", mongoDbContainer::getReplicaSetUrl)
        }
    }

    @Autowired lateinit var mockMvc: MockMvc

    @Test
    @WithMockUser(username = "user1", password = "test", roles = ["user"])
    fun shouldCreateTaskListForMockUser() {
        val title = "My Test List"
        val taskListCreationDTO = TaskListDTO(title = title)

        mockMvc.post("$baseUrl/lists") {
            contentType = MediaType.APPLICATION_JSON
            content = asJsonString(taskListCreationDTO)
        }.andExpect {
            MockMvcResultMatchers.status().is2xxSuccessful
            MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON)
            jsonPath("$.id", not(emptyString())) // don't know that the id is yet
            jsonPath("$.title", equalTo(title))
            jsonPath("$.tasks", emptyIterable<TaskDTO>())
        }
    }
}

