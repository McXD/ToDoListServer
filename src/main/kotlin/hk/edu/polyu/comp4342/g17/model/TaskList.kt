package hk.edu.polyu.comp4342.g17.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document


// Basic task meta data?
@Document("task_list")
data class TaskList(
    @Id val id: ObjectId,
    val title: String, // persistent mapping error
    val username: String, // belong to
    @DBRef val tasks: MutableList<Task> = mutableListOf()
) {
    fun addTask(task: Task) {
        tasks.add(task)
    }

    fun deleteTask(task: Task): Boolean {
        return tasks.remove(task)
    }
}