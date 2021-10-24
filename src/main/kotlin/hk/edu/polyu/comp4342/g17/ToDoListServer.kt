package hk.edu.polyu.comp4342.g17

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication


@SpringBootApplication
open class ToDoListServer

fun main(args: Array<String>) {
    SpringApplication.run(ToDoListServer::class.java, *args)
}