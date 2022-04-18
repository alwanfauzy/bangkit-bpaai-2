package com.alwan.bangkitbpaai1.data.model.request

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email")
    val email: String?,
    @SerializedName("password")
    val password: String?,
)
