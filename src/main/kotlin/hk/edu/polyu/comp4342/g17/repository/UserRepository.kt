package hk.edu.polyu.comp4342.g17.repository

import hk.edu.polyu.comp4342.g17.model.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository: MongoRepository<User, String> {
    fun findByUsername(username: String): User?
}