package hk.edu.polyu.comp4342.g17.config.security

import hk.edu.polyu.comp4342.g17.model.User
import hk.edu.polyu.comp4342.g17.repository.UserRepository
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.provisioning.UserDetailsManager
import org.springframework.stereotype.Component

@Component
open class MongoUserDetailsManager(
    private val users: UserRepository
): UserDetailsManager {
    override fun loadUserByUsername(username: String?): UserDetails {
        return CustomUserDetails(users.findByUsername(username!!)!!)
    }

    override fun createUser(user: UserDetails?) {
        users.insert(User(user!!.username, user.password))
    }

    override fun updateUser(user: UserDetails?) {
        TODO("Not yet implemented")
    }

    override fun deleteUser(username: String?) {
        TODO("Not yet implemented")
    }

    override fun changePassword(oldPassword: String?, newPassword: String?) {
        TODO("Not yet implemented")
    }

    override fun userExists(username: String?): Boolean {
        return users.findByUsername(username!!) != null
    }
}