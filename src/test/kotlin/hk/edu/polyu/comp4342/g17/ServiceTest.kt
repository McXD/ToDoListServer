package hk.edu.polyu.comp4342.g17

import hk.edu.polyu.comp4342.g17.dto.TaskListDTO
import hk.edu.polyu.comp4342.g17.model.TaskList
import hk.edu.polyu.comp4342.g17.repository.TaskListRepository
import hk.edu.polyu.comp4342.g17.repository.TaskRepository
import hk.edu.polyu.comp4342.g17.service.PersistentTaskService
import org.bson.types.ObjectId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.mockito.ArgumentMatchers.any as anyArg

/**
 * Test [PersistentTaskService] with mock repositories.
 */
class ServiceTest {
    private lateinit var taskService: PersistentTaskService

    @Mock lateinit var taskRepository: TaskRepository
    @Mock lateinit var taskListRepository: TaskListRepository

    private val dummyList = TaskList(ObjectId(), "", "")

    @BeforeEach
    fun setUpEach() {
        MockitoAnnotations.openMocks(this)
        taskService = PersistentTaskService(taskRepository, taskListRepository)
    }

    @Test
    fun `should create a task list`() {
        whenever(taskListRepository.insert(anyArg(TaskList::class.java))).thenReturn(dummyList)

        val taskListDef = TaskListDTO(title = "Title")
        taskService.createListFor("user1", taskListDef)

        verify(taskListRepository).insert(anyArg(TaskList::class.java))
    }
}