package com.alwan.bangkitbpaai2.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.alwan.bangkitbpaai2.data.Resource
import com.alwan.bangkitbpaai2.databinding.ActivityRegisterBinding
import com.alwan.bangkitbpaai2.util.UserPreferences
import com.alwan.bangkitbpaai2.util.ViewModelFactory
import com.alwan.bangkitbpaai2.util.closeKeyboard

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")
    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupView()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.imgBackRegister -> finish()
            binding.btnRegister -> {
                val name = binding.editNameRegister.text.toString()
                val email = binding.editEmailRegister.text.toString()
                val password = binding.editPasswordRegister.text.toString()

                if (binding.editEmailRegister.error == null && binding.editPasswordRegister.error == null) {
                    closeKeyboard(this)
                    authViewModel.register(name, email, password)
                }
            }
        }
    }

    private fun setupView() {
        with(binding) {
            imgBackRegister.setOnClickListener(this@RegisterActivity)
            btnRegister.setOnClickListener(this@RegisterActivity)
        }
    }

    private fun setupViewModel() {
        val pref = UserPreferences.getInstance(dataStore)
        authViewModel = ViewModelProvider(this, ViewModelFactory(pref))[AuthViewModel::class.java]

        authViewModel.authInfo.observe(this) {
            when (it) {
                is Resource.Success -> {
                    showLoading(false)
                    Toast.makeText(this, it.data, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finishAffinity()
                }
                is Resource.Loading -> showLoading(true)
                is Resource.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }
            }
        }
    }

    private fun showLoading(state: Boolean) {
        binding.spinRegister.visibility = if (state) View.VISIBLE else View.GONE
        binding.btnRegister.isEnabled = !state
    }
}