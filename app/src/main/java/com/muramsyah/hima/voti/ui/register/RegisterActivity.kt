package com.muramsyah.hima.voti.ui.register

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding2.widget.RxTextView
import com.muramsyah.hima.voti.R
import com.muramsyah.hima.voti.core.data.Resource
import com.muramsyah.hima.voti.core.domain.model.Mahasiswa
import com.muramsyah.hima.voti.databinding.ActivityRegisterBinding
import com.muramsyah.hima.voti.ui.login.LoginActivity
import io.reactivex.Observable
import org.koin.android.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private val viewModel: RegisterViewModel by viewModel()

    private var registerActivityBinding: ActivityRegisterBinding? = null
    private val binding get() = registerActivityBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerActivityBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authentication()
        binding.tvLogin.setOnClickListener(this)
        binding.btnRegister.setOnClickListener(this)
    }

    @SuppressLint("CheckResult")
    private fun authentication() {
        val nimStream = RxTextView.textChanges(binding.edtNim)
            .skipInitialValue()
            .filter { it.isNotEmpty() }
            .map { nim ->
                nim.toString().take(2).toInt() < 18 || nim.toString().take(2).toInt() > 21 || nim.length > 10 || nim.length < 10
            }
        nimStream.subscribe {
            showAlert(1, it)
        }

        val name = RxTextView.textChanges(binding.edtName)
            .skipInitialValue()
            .filter { it.isNotEmpty() }
            .map { name ->
                name.length < 3 || name.length > 25
            }
        name.subscribe {
            showAlert(2, it)
        }

        val email = RxTextView.textChanges(binding.edtEmail)
            .skipInitialValue()
            .filter { it.isNotEmpty() }
            .map { email ->
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
        email.subscribe {
            showAlert(5, it)
        }

        val password = RxTextView.textChanges(binding.edtPassword)
            .skipInitialValue()
            .filter { it.isNotEmpty() }
            .map { password ->
                password.length < 7
            }
        password.subscribe {
            showAlert(6, it)
        }

        val coPassword = Observable.merge(
            RxTextView.textChanges(binding.edtPassword)
                .skipInitialValue()
                .filter { it.isNotEmpty() }
                .map { password ->
                    password.toString() != binding.edtCoPassword.text.toString()
                },
            RxTextView.textChanges(binding.edtCoPassword)
                .skipInitialValue()
                .filter { it.isNotEmpty() }
                .map { coPassword ->
                    coPassword.toString() != binding.edtPassword.text.toString()
                }
        )
        coPassword.subscribe {
            showAlert(7, it)
        }

        val invalidFieldsStream = Observable.combineLatest(
            nimStream,
            name,
            email,
            password,
            coPassword,
            { t1, t2, t3, t4, t5 ->
                !t1 && !t2 && !t3 && !t4 && !t5
            }
        )
        invalidFieldsStream.subscribe { isValid ->
            binding.btnRegister.isEnabled = isValid
        }

        val dummyForce = arrayOf("2018", "2019", "2020", "2021")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, dummyForce)
        binding.edtForce.setAdapter(adapter)

        val dummySemester = arrayOf(1, 3, 5)
        val adapterSmt = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, dummySemester)
        binding.edtSemester.setAdapter(adapterSmt)
    }

    private fun showAlert(code: Int, isShowAlert: Boolean) {
        when (code) {
            1 -> binding.tilNim.error           = if(isShowAlert) resources.getString(R.string.title_alert_nim) else null
            2 -> binding.tilName.error          = if(isShowAlert) resources.getString(R.string.title_alert_nama) else null
            3 -> binding.tilForce.error         = if(isShowAlert) resources.getString(R.string.title_alert_angkatan) else null
            4 -> binding.tilSemester.error      = if(isShowAlert) resources.getString(R.string.title_alert_semester) else null
            5 -> binding.tilEmail.error         = if(isShowAlert) resources.getString(R.string.title_alert_email) else null
            6 -> binding.tilPassword.error      = if(isShowAlert) resources.getString(R.string.title_alert_password) else null
            7 -> binding.tilCoPassword.error    = if(isShowAlert) resources.getString(R.string.title_alert_confirm_password) else null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        registerActivityBinding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_register -> {

                if (TextUtils.isEmpty(binding.edtForce.text)) {
                    showAlert(3, true)
                } else if (TextUtils.isEmpty(binding.edtSemester.text)) {
                    showAlert(4, true)
                } else {
                    val mahasiswa = Mahasiswa(
                        binding.edtNim.text.toString().trim(),
                        binding.edtName.text.toString().trim(),
                        binding.edtForce.text.toString().trim().toInt(),
                        binding.edtSemester.text.toString().toInt(),
                        binding.edtEmail.text.toString().trim()
                    )

                    viewModel.registerNewAccount(mahasiswa, binding.edtPassword.text.toString().trim())
                        .observe(this, { resource ->
                            when (resource) {
                                is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                                is Resource.Success -> {
                                    binding.progressBar.visibility = View.GONE
                                    Toast.makeText(
                                        this,
                                        resources.getString(R.string.title_succes_register_account),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                is Resource.Error -> {
                                    binding.progressBar.visibility = View.GONE
                                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        })
                }
            }
            R.id.tv_login -> {
                viewModel.logoutUser()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
    }
}