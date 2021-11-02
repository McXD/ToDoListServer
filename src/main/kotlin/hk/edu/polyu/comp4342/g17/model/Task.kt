package hk.edu.polyu.comp4342.g17.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "task")
data class Task(
    @Id val id: ObjectId,
    var title: String,
    var detail: String,
    var isDone: Boolean = false,
)