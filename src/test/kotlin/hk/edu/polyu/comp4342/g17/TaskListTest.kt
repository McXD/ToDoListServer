package hk.edu.polyu.comp4342.g17

import hk.edu.polyu.comp4342.g17.dto.TaskListDTO
import hk.edu.polyu.comp4342.g17.model.TaskList
import hk.edu.polyu.comp4342.g17.service.TaskService
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@MockBean(TaskService::class)
@AutoConfigureMockMvc
@WithMockUser(username = "user1", password = "test", roles = ["user"])
class TaskListTest {
    private val baseUrl = "/api/v1/lists"

    @Autowired lateinit var mockMvc: MockMvc
    @Autowired lateinit var mockTaskService: TaskService

    @Test
    fun `can create a list with post`() {
        whenever(mockTaskService.createListFor("user1")).thenReturn(
            TaskList(ObjectId(), "user1")
        )

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("$baseUrl")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(TaskListDTO())))
            .andExpect(status().`is`(200))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    }
}