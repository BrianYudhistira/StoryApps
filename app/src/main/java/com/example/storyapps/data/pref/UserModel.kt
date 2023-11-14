package com.example.storyapps.data.pref

data class UserModel(
    val idUser: String,
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)