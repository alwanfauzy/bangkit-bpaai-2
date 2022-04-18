package com.alwan.bangkitbpaai1.util

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alwan.bangkitbpaai1.ui.addstory.AddStoryViewModel
import com.alwan.bangkitbpaai1.ui.auth.AuthViewModel
import com.alwan.bangkitbpaai1.ui.main.MainViewModel

class ViewModelFactory(private val pref: UserPreferences) :
    ViewModelProvider.NewInstanceFactory() {
    private lateinit var mApplication: Application

    fun setApplication(application: Application){
        mApplication = application
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(pref) as T
        }
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(pref, mApplication) as T
        }
        if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)) {
            return AddStoryViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}