package com.iqbal.submission.data.local.model

data class UserModel(
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)