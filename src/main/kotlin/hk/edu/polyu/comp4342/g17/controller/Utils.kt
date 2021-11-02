package hk.edu.polyu.comp4342.g17.controller

import org.springframework.security.core.context.SecurityContextHolder

fun getUsername(): String = SecurityContextHolder.getContext().authentication.name