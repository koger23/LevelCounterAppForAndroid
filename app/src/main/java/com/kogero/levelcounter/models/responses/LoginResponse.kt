package com.kogero.levelcounter.models.responses

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("errorMessages")
    val errorMessages: List<String>,
    @SerializedName("token")
    val token: String)