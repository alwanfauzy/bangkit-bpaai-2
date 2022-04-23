package com.alwan.bangkitbpaai2.ui.addstory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alwan.bangkitbpaai2.data.Resource
import com.alwan.bangkitbpaai2.data.model.response.BaseResponse
import com.alwan.bangkitbpaai2.data.remote.RetrofitConfig
import com.alwan.bangkitbpaai2.util.UserPreferences
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStoryViewModel(private val pref: UserPreferences) : ViewModel() {
    private val _uploadInfo = MutableLiveData<Resource<String>>()
    val uploadInfo: LiveData<Resource<String>> = _uploadInfo

    suspend fun upload(
        imageMultipart: MultipartBody.Part,
        description: RequestBody,
        asGuest: Boolean = false
    ) {
        _uploadInfo.postValue(Resource.Loading())
        val client = if (asGuest) RetrofitConfig.apiInstance.addGuestStory(
            imageMultipart,
            description
        ) else RetrofitConfig.apiInstance.addStory(
            token = "Bearer ${pref.getUserKey().first()}",
            imageMultipart,
            description
        )

        client.enqueue(object : Callback<BaseResponse> {
            override fun onResponse(
                call: Call<BaseResponse>,
                response: Response<BaseResponse>
            ) {
                if (response.isSuccessful) {
                    _uploadInfo.postValue(Resource.Success(response.body()?.message))
                } else {
                    val errorResponse = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        BaseResponse::class.java
                    )
                    _uploadInfo.postValue(Resource.Error(errorResponse.message))
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                Log.e(
                    AddStoryViewModel::class.java.simpleName,
                    "onFailure upload"
                )
                _uploadInfo.postValue(Resource.Error(t.message))
            }
        })
    }
}