package com.ecosnap.Model

import java.io.Serializable

class UserProfile (
    val firstName: String = "",
    val lastName: String = "",
    val email: String = ""
) : Serializable {
    var descr: String = "Recycling newbie here! It’s about time that I started to care."
    var imgpath: String = ""
}