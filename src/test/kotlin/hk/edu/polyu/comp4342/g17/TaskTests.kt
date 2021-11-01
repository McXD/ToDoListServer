package hk.edu.polyu.comp4342.g17

import hk.edu.polyu.comp4342.g17.dto.TaskDTO
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import java.util.*
import kotlin.test.assertEquals


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskTests {
    @LocalServerPort var port: Int = 0
    @Autowired lateinit var testRestTemplate: TestRestTemplate

    companion object {
        lateinit var baseUrl: String
    }

    @BeforeAll
    fun init() {
        baseUrl = "http://localhost:$port/api/v1"
    }

    @Test
    fun `should create a task`() {
        val requestBody = TaskDTO(null, "MyTask", "This is a test task", Date())
        val response = testRestTemplate.postForObject("$baseUrl/tasks", requestBody, TaskDTO::class.java)

        assertEquals(requestBody, response, "Response should be the same as request")
    }
}