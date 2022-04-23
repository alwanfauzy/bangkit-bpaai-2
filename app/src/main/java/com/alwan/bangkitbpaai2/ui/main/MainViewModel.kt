package com.alwan.bangkitbpaai2.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.alwan.bangkitbpaai2.data.Resource
import com.alwan.bangkitbpaai2.data.local.StoryDao
import com.alwan.bangkitbpaai2.data.local.StoryDatabase
import com.alwan.bangkitbpaai2.data.model.Story
import com.alwan.bangkitbpaai2.data.model.response.BaseResponse
import com.alwan.bangkitbpaai2.data.model.response.StoryResponse
import com.alwan.bangkitbpaai2.data.remote.RetrofitConfig
import com.alwan.bangkitbpaai2.util.UserPreferences
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: UserPreferences, application: Application) :
    AndroidViewModel(application) {
    private var storyDao: StoryDao? = null
    private var storyDB: StoryDatabase? = StoryDatabase.getDatabase(application)
    private val _stories = MutableLiveData<Resource<ArrayList<Story>>>()
    val stories: LiveData<Resource<ArrayList<Story>>> = _stories

    init {
        storyDao = storyDB?.storyDao()
    }

    suspend fun getStories() {
        _stories.postValue(Resource.Loading())
        val client =
            RetrofitConfig.apiInstance.getStories(token = "Bearer ${pref.getUserKey().first()}")

        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {

                if (response.isSuccessful) {
                    response.body()?.let {
                        val listStory = it.listStory

                        viewModelScope.launch {
                            storyDao?.deleteAll()
                            listStory.forEach { story ->
                                storyDao?.insert(story)
                            }
                        }
                        _stories.postValue(Resource.Success(ArrayList(listStory)))
                    }
                } else {
                    val errorResponse = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        BaseResponse::class.java
                    )
                    _stories.postValue(Resource.Error(errorResponse.message))
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                Log.e(
                    MainViewModel::class.java.simpleName,
                    "onFailure getStories"
                )
                _stories.postValue(Resource.Error(t.message))
            }
        })
    }
}