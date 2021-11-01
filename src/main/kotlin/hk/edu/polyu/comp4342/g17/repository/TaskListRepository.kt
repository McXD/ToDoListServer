package hk.edu.polyu.comp4342.g17.repository

import hk.edu.polyu.comp4342.g17.model.TaskList
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import java.math.BigInteger

interface TaskListRepository: MongoRepository<TaskList, ObjectId> {
    fun findByUsername(username: String): List<TaskList>
}