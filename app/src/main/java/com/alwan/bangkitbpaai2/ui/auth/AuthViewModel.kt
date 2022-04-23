package com.alwan.bangkitbpaai2.ui.auth

import android.util.Log
import androidx.lifecycle.*
import com.alwan.bangkitbpaai2.data.Resource
import com.alwan.bangkitbpaai2.data.model.request.LoginRequest
import com.alwan.bangkitbpaai2.data.model.request.RegisterRequest
import com.alwan.bangkitbpaai2.data.model.response.BaseResponse
import com.alwan.bangkitbpaai2.data.model.response.LoginResponse
import com.alwan.bangkitbpaai2.data.remote.RetrofitConfig
import com.alwan.bangkitbpaai2.util.UserPreferences
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel(private val pref: UserPreferences) : ViewModel() {
    private val _authInfo = MutableLiveData<Resource<String>>()
    val authInfo: LiveData<Resource<String>> = _authInfo

    fun login(email: String, password: String) {
        _authInfo.postValue(Resource.Loading())
        val client = RetrofitConfig.apiInstance.login(LoginRequest(email, password))

        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResult = response.body()?.loginResult?.token

                    loginResult?.let { saveUserKey(it) }
                    _authInfo.postValue(Resource.Success(loginResult))
                } else {
                    val errorResponse = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        BaseResponse::class.java
                    )
                    _authInfo.postValue(Resource.Error(errorResponse.message))
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e(
                    AuthViewModel::class.java.simpleName,
                    "onFailure login"
                )
                _authInfo.postValue(Resource.Error(t.message))
            }
        })
    }


    fun register(name: String, email: String, password: String) {
        _authInfo.postValue(Resource.Loading())
        val client = RetrofitConfig.apiInstance.register(RegisterRequest(name, email, password))

        client.enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if (response.isSuccessful) {
                    val message = response.body()?.message.toString()
                    _authInfo.postValue(Resource.Success(message))
                } else {
                    val errorResponse = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        BaseResponse::class.java
                    )
                    _authInfo.postValue(Resource.Error(errorResponse.message))
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                Log.e(
                    AuthViewModel::class.java.simpleName,
                    "onFailure register"
                )
                _authInfo.postValue(Resource.Error(t.message))
            }
        })
    }

    fun logout() = deleteUserKey()

    fun getUserKey() = pref.getUserKey().asLiveData()

    private fun saveUserKey(key: String) {
        viewModelScope.launch {
            pref.saveUserKey(key)
        }
    }

    private fun deleteUserKey() {
        viewModelScope.launch {
            pref.deleteUserKey()
        }
    }
}