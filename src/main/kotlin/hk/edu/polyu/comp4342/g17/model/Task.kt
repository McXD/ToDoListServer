package hk.edu.polyu.comp4342.g17.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigInteger
import java.util.*

@Document(collection = "task")
data class Task(
    var title: String,
    var detail: String?,
    var due: Date?,
)