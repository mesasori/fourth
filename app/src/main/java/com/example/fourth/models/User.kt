package com.example.fourth.models

class User(
    val id: String = "",
    var firstName: String = "",
    var surname: String = "",
    var email: String = "",
    var image: String = "",
    var mobile: Long = 0,
    val profileCompleted: Int = 0,
    val dateOfBirth: String = ""
)