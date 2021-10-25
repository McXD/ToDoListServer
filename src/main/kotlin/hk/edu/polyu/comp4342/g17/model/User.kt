package hk.edu.polyu.comp4342.g17.model

import org.springframework.data.annotation.Id

data class User(
    @Id val username: String,
    val password: String
)