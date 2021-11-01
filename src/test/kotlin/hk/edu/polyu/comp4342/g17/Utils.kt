package hk.edu.polyu.comp4342.g17

import com.fasterxml.jackson.databind.ObjectMapper

fun asJsonString(obj: Any): String {
    return ObjectMapper().writeValueAsString(obj)
}