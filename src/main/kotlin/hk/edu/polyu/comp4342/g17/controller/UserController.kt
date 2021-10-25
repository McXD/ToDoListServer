package hk.edu.polyu.comp4342.g17.controller

import hk.edu.polyu.comp4342.g17.config.security.CustomUserDetails
import hk.edu.polyu.comp4342.g17.config.security.MongoUserDetailsManager
import hk.edu.polyu.comp4342.g17.dto.UserDTO
import hk.edu.polyu.comp4342.g17.model.User
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.AbstractPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val udm: MongoUserDetailsManager,
    private val passwordEncoder: PasswordEncoder,
) {
    @GetMapping
    fun getUser(): UserDTO {
        val username = SecurityContextHolder.getContext().authentication.name
        val userDetails = udm.loadUserByUsername(username) as CustomUserDetails
        return UserDTO(userDetails.username, userDetails.password)
    }

    @PostMapping
    fun registerUser(@RequestBody userDTO: UserDTO) {
        udm.createUser(CustomUserDetails(User(userDTO.username, passwordEncoder.encode(userDTO.password))))
    }
}