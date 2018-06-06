package com.ecosnap.Model

import java.io.Serializable

class UserProfile (
    val firstName: String = "",
    val lastName: String = "",
    val email: String = ""
) : Serializable