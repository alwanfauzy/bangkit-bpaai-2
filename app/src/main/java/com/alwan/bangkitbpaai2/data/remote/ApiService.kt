package com.alwan.bangkitbpaai2.data.remote

import com.alwan.bangkitbpaai2.data.model.request.LoginRequest
import com.alwan.bangkitbpaai2.data.model.request.RegisterRequest
import com.alwan.bangkitbpaai2.data.model.response.BaseResponse
import com.alwan.bangkitbpaai2.data.model.response.LoginResponse
import com.alwan.bangkitbpaai2.data.model.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("register")
    fun register(@Body request: RegisterRequest): Call<BaseResponse>

    @POST("login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @Multipart
    @POST("stories")
    fun addStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<BaseResponse>

    @Multipart
    @POST("stories/guest")
    fun addGuestStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<BaseResponse>

    @GET("stories")
    fun getStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null
    ): Call<StoryResponse>
}