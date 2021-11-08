package hk.edu.polyu.comp4342.g17.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "task")
data class Task(
    @Id val id: ObjectId,
    val listId: ObjectId, // TODO: id or a full list? Full list may be very inefficient, use ref first.
    var title: String,
    var detail: String?,
    var isDone: Boolean = false,
)