package hk.edu.polyu.comp4342.g17.controller

import hk.edu.polyu.comp4342.g17.asJsonString
import hk.edu.polyu.comp4342.g17.dto.TaskDTO
import hk.edu.polyu.comp4342.g17.dto.TaskListDTO
import hk.edu.polyu.comp4342.g17.model.TaskList
import hk.edu.polyu.comp4342.g17.service.TaskService
import org.bson.types.ObjectId
import org.hamcrest.Matchers.emptyIterable
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * Test the web layer. This includes testing endpoints, object mapping, security, etc.
 */
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "user1", password = "test", roles = ["user"]) // Set the security context for current requests
class TaskListControllerTest {
    private val baseUrl = "/api/v1/lists"

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var mockTaskService: TaskService

    @Test
    fun `can create an empty list with post`() {
        val title = "My Test List"
        val objectId = ObjectId()
        val taskListCreationDTO = TaskListDTO(title = title)

        whenever(mockTaskService.createListFor("user1", taskListCreationDTO)).thenReturn(
            TaskList(objectId, title, "user1")
        )

        mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = asJsonString(taskListCreationDTO)
        }.andExpect {
            status().is2xxSuccessful
            content().contentType(MediaType.APPLICATION_JSON)
            jsonPath("$.id", equalTo(objectId.toHexString()))
            jsonPath("$.title", equalTo(title))
            jsonPath("$.tasks", emptyIterable<TaskDTO>())
        }
    }
}