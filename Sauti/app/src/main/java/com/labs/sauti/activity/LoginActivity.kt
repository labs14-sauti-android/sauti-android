package com.labs.sauti.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.labs.sauti.R
import com.labs.sauti.SautiApp
import com.labs.sauti.model.LoginResponse
import com.labs.sauti.sp.SessionSp
import com.labs.sauti.view_model.LoginViewModel
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

            Toast.makeText(this, "Login successful", Toast.LENGTH_LONG).show()

            finish()
        })

        // error
        loginViewModel.getErrorLiveData().observe(this, Observer {

            Toast.makeText(this, "Login failed. ${it.message}", Toast.LENGTH_LONG).show()
        })

        // login button
        a_login_b_login.setOnClickListener {
            loginViewModel.login(a_login_et_username.text.toString(), a_login_et_password.text.toString())
        }
    }
}
