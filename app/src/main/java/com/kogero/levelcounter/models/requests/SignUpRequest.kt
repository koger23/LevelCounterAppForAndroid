package com.kogero.levelcounter.models.requests

class SignUpRequest(
    val fullName: String,
    val userName: String,
    val email: String,
    val password: String,
    val verifyPassword: String,
    val gender: String
)