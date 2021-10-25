package hk.edu.polyu.comp4342.g17.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/v1/test")
class TestController {
    companion object {
        val log: Logger = LoggerFactory.getLogger(TestController::class.java)
    }

    @GetMapping
    fun test(request: HttpServletRequest) {
        log.info("Received request from ${request.remoteAddr}")
    }
}