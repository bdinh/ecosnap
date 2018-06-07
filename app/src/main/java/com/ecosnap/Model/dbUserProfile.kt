package com.ecosnap.Model

import java.io.Serializable

class UserProfile (
    var firstName: String = "",
    var lastName: String = "",
    var email: String = ""
) : Serializable {
    var descr: String = "Recycling newbie here! It’s about time that I started to care."
    var imgpath: String = ""
}