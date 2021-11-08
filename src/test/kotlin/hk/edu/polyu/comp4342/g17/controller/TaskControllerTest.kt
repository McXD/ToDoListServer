package hk.edu.polyu.comp4342.g17.controller

import hk.edu.polyu.comp4342.g17.asJsonString
import hk.edu.polyu.comp4342.g17.dto.TaskDTO
import hk.edu.polyu.comp4342.g17.id
import hk.edu.polyu.comp4342.g17.model.Task
import hk.edu.polyu.comp4342.g17.service.TaskService
import org.bson.types.ObjectId
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "user1", password = "test", roles = ["user"]) // Set the security context for current requests
class TaskControllerTest {
    private val baseUrl = "/api/v1/tasks"

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var mockTaskService: TaskService

    @Test
    fun `can fetch a given task`() {
        val task = Task(id(), "Test Task", "Just a Test", false)

        whenever(mockTaskService.getTask(task.id)).thenReturn(Optional.of(task))

        mockMvc.get("$baseUrl/${task.id.toHexString()}")
            .andExpect {
                status().is2xxSuccessful
                content().contentType(MediaType.APPLICATION_JSON)
                jsonPath("$.id", equalTo(task.id.toHexString()))
            }.andDo {
                print()
            }
    }

    @Test
    fun `task update - can mark a task as completed`() {
        val mockTaskId = ObjectId()
        val patch = TaskDTO(mockTaskId.toHexString(), isDone = true)

        whenever(mockTaskService.updateTask(patch)).thenReturn(
            Task(mockTaskId, "Test Task", "No Details", true)
        )

        mockMvc.patch(baseUrl) {
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
    fun `can delete a task`() {
        val mockTaskId = ObjectId()

        // task is deleted when no exception is thrown, which is the behaviour of mockTaskService
        mockMvc.delete("$baseUrl/${mockTaskId.toHexString()}")
            .andExpect {
                status().is2xxSuccessful
            }.andDo {
                print()
            }
    }
}