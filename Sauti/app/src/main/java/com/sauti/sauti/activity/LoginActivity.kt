package com.sauti.sauti.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.sauti.sauti.R
import com.sauti.sauti.SautiApp
import com.sauti.sauti.model.LoginResponse
import com.sauti.sauti.sp.SessionSp
import com.sauti.sauti.view_model.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var loginViewModelFactory: LoginViewModel.Factory

    private lateinit var loginViewModel: LoginViewModel

    @Inject
    lateinit var sessionSp: SessionSp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // inject
        val loginComponent = (applicationContext as SautiApp).getLoginComponent()
        loginComponent.inject(this)

        loginViewModel = ViewModelProviders.of(this, loginViewModelFactory).get(LoginViewModel::class.java)

        // login success
        loginViewModel.getLoginResponseLiveData().observe(this, Observer<LoginResponse> {

            sessionSp.setAccessToken(it.accessToken ?: "")
            sessionSp.setExpiresIn(it.expiresIn ?: 0)
            sessionSp.setLoggedInAt(System.currentTimeMillis() / 1000L)

            Snackbar.make(a_login_ll_root, "Login successful", Snackbar.LENGTH_LONG).show()
        })

        // error
        loginViewModel.getErrorLiveData().observe(this, Observer {

            Snackbar.make(a_login_ll_root, "Login failed. ${it.message}", Snackbar.LENGTH_LONG).show()
        })

        // login button
        a_login_b_login.setOnClickListener {
            loginViewModel.login(a_login_et_username.text.toString(), a_login_et_password.text.toString())
        }
    }
}
