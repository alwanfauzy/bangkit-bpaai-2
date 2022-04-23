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
import com.alwan.bangkitbpaai2.R
import com.alwan.bangkitbpaai2.data.Resource
import com.alwan.bangkitbpaai2.databinding.ActivityLoginBinding
import com.alwan.bangkitbpaai2.ui.main.MainActivity
import com.alwan.bangkitbpaai2.util.UserPreferences
import com.alwan.bangkitbpaai2.util.ViewModelFactory
import com.alwan.bangkitbpaai2.util.closeKeyboard

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var authViewModel: AuthViewModel
    private var mShouldFinish = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupView()
    }

    private fun setupView() {
        with(binding) {
            btnRegisterLogin.setOnClickListener(this@LoginActivity)
            btnLogin.setOnClickListener(this@LoginActivity)
        }
    }

    private fun setupViewModel() {
        val pref = UserPreferences.getInstance(dataStore)
        authViewModel = ViewModelProvider(this, ViewModelFactory(pref))[AuthViewModel::class.java]

        authViewModel.authInfo.observe(this) {
            when (it) {
                is Resource.Success -> {
                    showLoading(false)
                    startActivity(Intent(this, MainActivity::class.java))
                    mShouldFinish = true
                }
                is Resource.Loading -> showLoading(true)
                is Resource.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }
            }
        }
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

    override fun onClick(v: View?) {
        when (v) {
            binding.btnRegisterLogin -> startActivity(Intent(this, RegisterActivity::class.java))
            binding.btnLogin -> {
                if (canLogin()) {
                    val email = binding.editEmail.text.toString()
                    val password = binding.editPassword.text.toString()

                    closeKeyboard(this)
                    authViewModel.login(email, password)
                } else {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.check_input),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun canLogin() =
        binding.editEmail.error == null && binding.editPassword.error == null && !binding.editEmail.text.isNullOrEmpty() && !binding.editPassword.text.isNullOrEmpty()

    private fun showLoading(state: Boolean) {
        binding.spinLogin.visibility = if (state) View.VISIBLE else View.GONE
        binding.btnLogin.isEnabled = !state
    }
}