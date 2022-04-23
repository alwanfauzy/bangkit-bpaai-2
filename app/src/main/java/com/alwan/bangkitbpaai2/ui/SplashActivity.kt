package com.alwan.bangkitbpaai2.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.alwan.bangkitbpaai2.databinding.ActivitySplashBinding
import com.alwan.bangkitbpaai2.ui.auth.AuthViewModel
import com.alwan.bangkitbpaai2.ui.auth.LoginActivity
import com.alwan.bangkitbpaai2.ui.main.MainActivity
import com.alwan.bangkitbpaai2.util.UserPreferences
import com.alwan.bangkitbpaai2.util.ViewModelFactory

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")
    private var _binding: ActivitySplashBinding? = null
    private val binding get() = _binding!!
    private var mShouldFinish = false
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()

        Handler(Looper.getMainLooper()).postDelayed({
            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this,
                    binding.splashLogo,
                    "logoLogin"
                )

            authViewModel.getUserKey().observe(this){
                if(it.isNullOrEmpty()){
                    startActivity(Intent(this, LoginActivity::class.java), optionsCompat.toBundle())
                }else{
                    startActivity(Intent(this, MainActivity::class.java), optionsCompat.toBundle())
                }
            }
            mShouldFinish = true
        }, SPLASH_DELAY)
    }

    private fun setupViewModel() {
        val pref = UserPreferences.getInstance(dataStore)
        authViewModel = ViewModelProvider(this, ViewModelFactory(pref))[AuthViewModel::class.java]
    }

    override fun onStop() {
        super.onStop()
        if (mShouldFinish)
            finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val SPLASH_DELAY = 2000L
    }
}