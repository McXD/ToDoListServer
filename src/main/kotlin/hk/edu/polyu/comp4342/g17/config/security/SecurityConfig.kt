package hk.edu.polyu.comp4342.g17.config.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.provisioning.UserDetailsManager

@EnableWebSecurity
open class SecurityConfig: WebSecurityConfigurerAdapter() {
    @Autowired lateinit var mongoUserDetailsManager: MongoUserDetailsManager

    override fun configure(http: HttpSecurity?) {
        getHttp().run {
            csrf().disable()
            formLogin().successHandler { _, _, _ -> } // 1. use form login 2. do not redirect
            authorizeRequests().antMatchers("/api/v1/lists/**").hasRole("user")
            authorizeRequests().antMatchers("/api/v1/tasks/**").hasRole("user")
        }
    }

    override fun configure(web: WebSecurity?) {
        web!!.run {
            ignoring().run {
                antMatchers("/api/v1/test/**") // endpoints for connection testing
                antMatchers(HttpMethod.POST, "/api/v1/users") // account registration
            }
        }
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth!!.userDetailsService(mongoUserDetailsManager)
    }

    @Bean open fun passwordEncoder() = BCryptPasswordEncoder()
}