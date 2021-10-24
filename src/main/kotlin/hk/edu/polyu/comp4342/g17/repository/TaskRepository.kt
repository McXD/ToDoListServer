package hk.edu.polyu.comp4342.g17.repository

import hk.edu.polyu.comp4342.g17.model.Task
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.math.BigInteger

@Repository
interface TaskRepository: MongoRepository<Task, BigInteger>