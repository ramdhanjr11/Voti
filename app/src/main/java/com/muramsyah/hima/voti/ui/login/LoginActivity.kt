package com.muramsyah.hima.voti.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import android.nfc.NfcAdapter.EXTRA_DATA
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jakewharton.rxbinding2.widget.RxTextView
import com.muramsyah.hima.voti.R
import com.muramsyah.hima.voti.core.data.Resource
import com.muramsyah.hima.voti.databinding.ActivityLoginBinding
import com.muramsyah.hima.voti.ui.home.HomeActivity
import com.muramsyah.hima.voti.ui.register.RegisterActivity
import io.reactivex.Observable
import org.koin.android.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private val viewModel: LoginViewModel by viewModel()

    private var loginActivityBinding: ActivityLoginBinding? = null
    private val binding get() = loginActivityBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginActivityBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authentication()
        binding.btnLogin.setOnClickListener(this)
        binding.tvRegister.setOnClickListener(this)
    }

    @SuppressLint("CheckResult")
    private fun authentication() {
        val email = RxTextView.textChanges(binding.edtEmail)
            .skipInitialValue()
            .filter { it.isNotEmpty() }
            .map { email ->
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
        email.subscribe {
            showAlert(1, it)
        }

        val password = RxTextView.textChanges(binding.edtPassword)
            .skipInitialValue()
            .filter { it.isNotEmpty() }
            .map { password ->
                password.length < 7
            }
        password.subscribe {
            showAlert(2, it)
        }

        val invalidFieldsStream = Observable.combineLatest(
            email,
            password,
            { email, password ->
                !email && !password
            }
        )
        invalidFieldsStream.subscribe { isValid ->
            binding.btnLogin.isEnabled = isValid
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_login -> {
                val email = binding.edtEmail.text.toString().trim()
                val password = binding.edtPassword.text.toString().trim()

                binding.progressBar.visibility = View.VISIBLE
                viewModel.signInAccount(email, password).observe(this, { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            binding.progressBar.visibility = View.GONE
                            val intent = Intent(this, HomeActivity::class.java)
                            intent.putExtra(HomeActivity.EXTRA_DATA, resource.data)
                            startActivity(intent)
                            finish()

                        }
                        is Resource.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
            R.id.tv_register -> {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun showAlert(code: Int, isShowAlert: Boolean) {
        when (code) {
            1 -> binding.tilEmail.error = if(isShowAlert) resources.getString(R.string.title_alert_email) else null
            2 -> binding.tilPassword.error = if(isShowAlert) resources.getString(R.string.title_alert_password) else null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        loginActivityBinding = null
    }

    override fun onResume() {
        super.onResume()
        if (Firebase.auth.currentUser != null) {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }
}