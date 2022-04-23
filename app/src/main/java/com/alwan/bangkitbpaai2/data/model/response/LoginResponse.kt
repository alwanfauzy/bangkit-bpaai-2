package com.alwan.bangkitbpaai2.data.model.response

import com.alwan.bangkitbpaai2.data.model.Login
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("error")
    val error: Boolean?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("loginResult")
    val loginResult: Login?,
)
