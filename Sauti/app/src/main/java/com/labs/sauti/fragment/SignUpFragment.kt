package com.labs.sauti.fragment


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar

import com.labs.sauti.R
import com.labs.sauti.SautiApp
import com.labs.sauti.model.authentication.SignUpRequest
import com.labs.sauti.view_model.AuthenticationViewModel
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up.et_password
import kotlinx.android.synthetic.main.fragment_sign_up.et_username
import javax.inject.Inject

class SignUpFragment : Fragment() {

    private var openSignInListener: OpenSignInListener? = null

    @Inject
    lateinit var authenticationViewModelFactory: AuthenticationViewModel.Factory

    private lateinit var authenticationViewModel: AuthenticationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (context!!.applicationContext as SautiApp).getAuthenticationComponent().inject(this)
        authenticationViewModel = ViewModelProviders.of(this, authenticationViewModelFactory).get(AuthenticationViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // error
        authenticationViewModel.getErrorLiveData().observe(this, Observer {
            Snackbar.make(view, it, Snackbar.LENGTH_SHORT).show()
        })

        // sign up
        authenticationViewModel.getSignUpViewState().observe(this, Observer {
            if (it.isLoading) {
                vs_sign_up_loading.displayedChild = 1
            } else {
                vs_sign_up_loading.displayedChild = 0

                if (it.isSuccess) {
                    Snackbar.make(view, "Sign up successful", Snackbar.LENGTH_SHORT).show()
                    activity!!.supportFragmentManager.popBackStack()
                    openSignInListener?.openSignIn()
                }
            }
        })

        // sign up click
        b_sign_up.setOnClickListener {
            signUp()
        }

        // sign in click
        t_sign_in.setOnClickListener {
            activity!!.supportFragmentManager.popBackStack()
            openSignInListener?.openSignIn()
        }
    }

    private fun signUp() {
        val usernameStr = et_username.text.toString()
        if (usernameStr.length < 8 || usernameStr.length > 64) {
            et_username.error = "Username must be 8-64 characters long"
            return
        }

        val passwordStr = et_password.text.toString()
        val confirmPasswordStr = et_confirm_password.text.toString()

        if (passwordStr != confirmPasswordStr) {
            et_password.error = "Password do not match"
            et_confirm_password.error = "Password do not match"
            return
        }

        if (passwordStr.length < 8 || passwordStr.length > 64) {
            et_password.error = "Password must be 8-64 characters long"
            return
        }

        authenticationViewModel.signUp(
            SignUpRequest(
                et_username.text.toString(),
                passwordStr,
                et_phone_number.text.toString()
            )
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OpenSignInListener) {
            openSignInListener = context
        } else {
            throw RuntimeException("Context must implement OpenSignInListener")
        }
    }

    override fun onDetach() {
        super.onDetach()

        openSignInListener = null
    }

    companion object {
        fun newInstance() =
            SignUpFragment()
    }

    interface OpenSignInListener {
        fun openSignIn()
    }

}
