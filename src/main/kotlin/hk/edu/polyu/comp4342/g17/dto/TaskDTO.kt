package hk.edu.polyu.comp4342.g17.dto

import java.math.BigInteger
import java.util.*

data class TaskDTO(
    var title: String = "",
    var details: String? = "",
    var due: Date? = null
)