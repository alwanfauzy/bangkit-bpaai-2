package com.alwan.bangkitbpaai2.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.alwan.bangkitbpaai2.R
import com.alwan.bangkitbpaai2.databinding.ActivitySettingBinding
import com.alwan.bangkitbpaai2.ui.auth.AuthViewModel
import com.alwan.bangkitbpaai2.ui.auth.LoginActivity
import com.alwan.bangkitbpaai2.util.UserPreferences
import com.alwan.bangkitbpaai2.util.ViewModelFactory

class SettingActivity : AppCompatActivity(), View.OnClickListener {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")
    private var _binding: ActivitySettingBinding? = null
    private val binding get() = _binding!!
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupViewModel()
        setupView()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupView() {
        with(binding) {
            tvLanguageSetting.setOnClickListener(this@SettingActivity)
            tvLogoutSetting.setOnClickListener(this@SettingActivity)
        }
    }

    private fun setupViewModel() {
        val pref = UserPreferences.getInstance(dataStore)
        authViewModel = ViewModelProvider(this, ViewModelFactory(pref))[AuthViewModel::class.java]
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.tvLanguageSetting -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
            binding.tvLogoutSetting -> {
                authViewModel.logout()
                startActivity(Intent(this, LoginActivity::class.java))
                finishAffinity()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> true
        }
    }
}