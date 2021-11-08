package hk.edu.polyu.comp4342.g17.controller

import hk.edu.polyu.comp4342.g17.asJsonString
import hk.edu.polyu.comp4342.g17.dto.TaskDTO
import hk.edu.polyu.comp4342.g17.dto.TaskListDTO
import hk.edu.polyu.comp4342.g17.id
import hk.edu.polyu.comp4342.g17.model.Task
import hk.edu.polyu.comp4342.g17.model.TaskList
import hk.edu.polyu.comp4342.g17.objectMapper
import hk.edu.polyu.comp4342.g17.service.TaskService
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
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
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
    fun `can fetch all TaskLists for a user`() {
        val taskList1 = TaskList(id(), "List 1", "user1")
        val taskList2 = TaskList(id(), "List 2", "user1")

        val task_1_1 = Task(id(), taskList1.id,"Task 1 in List 1", "No Details")
        val task_1_2 = Task(id(), taskList1.id,"Task 2 in List 1", "No Details")
        val task_2_1 = Task(id(), taskList2.id, "Task 1 in List 2", "No Details")
        val task_2_2 = Task(id(), taskList2.id, "Task 2 in List 2", "No Details")

        taskList1.addTask(task_1_1)
        taskList1.addTask(task_1_2)
        taskList2.addTask(task_2_1)
        taskList2.addTask(task_2_2)

        whenever(mockTaskService.getAllTaskListsForUser("user1")).thenReturn(
            listOf(taskList1, taskList2)
        )

        mockMvc.get(baseUrl)
            .andExpect {
                status().is2xxSuccessful
                content().contentType(MediaType.APPLICATION_JSON)
                jsonPath("$.length()", equalTo(2))
                jsonPath("$[0].tasks.length()", equalTo(2))
                jsonPath("$[1].tasks.length()", equalTo(2))
            }.andDo {
                print()
            }
    }

    @Test
    fun `can create an empty list`() {
        val title = "My Test List"
        val objectId = id()
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
        }.andDo {
            print()
        }
    }

    /**
     * DELETE $baseUrl/{taskListId}
     */
    @Test
    fun `can delete the given list`() {
        val mockTaskListId = id()

        // If no exception is thrown, the deletion is successful
        mockMvc.delete("$baseUrl/${mockTaskListId.toHexString()}")
            .andExpect {
                status().is2xxSuccessful
            }.andDo {
                print()
            }
    }

    fun makeTasKList(): TaskListDTO {
        val returnedJsonString = mockMvc.post(baseUrl) {
            contentType = MediaType.APPLICATION_JSON
            content = asJsonString(
                TaskListDTO(title = "Just A Test List")
            )
        }.andReturn().response.contentAsString

        return objectMapper.readValue(returnedJsonString, TaskListDTO::class.java)
    }
}