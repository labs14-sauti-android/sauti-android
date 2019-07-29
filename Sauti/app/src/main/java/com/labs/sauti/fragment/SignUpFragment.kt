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

import com.labs.sauti.R
import com.labs.sauti.SautiApp
import com.labs.sauti.model.SignUpRequest
import com.labs.sauti.view_model.AuthenticationViewModel
import kotlinx.android.synthetic.main.fragment_sign_in.*
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

        authenticationViewModel.getSignUpResponseLiveData().observe(this, Observer{
            Toast.makeText(context, "Sign up successful", Toast.LENGTH_LONG).show()

            activity!!.supportFragmentManager.popBackStack()

            vs_sign_up.displayedChild = 0
        })

        authenticationViewModel.getErrorLiveData().observe(this, Observer {
            // TODO better error showing
            Toast.makeText(context, "Sign up failed ${it.errorDescription}", Toast.LENGTH_LONG).show()

            vs_sign_up.displayedChild = 0
        })

        b_sign_up.setOnClickListener {
            vs_sign_up.displayedChild = 1
            signUp()
        }

        t_sign_in.setOnClickListener {
            activity!!.supportFragmentManager.popBackStack()
            openSignInListener?.openSignIn()
        }
    }

    private fun signUp() {
        // TODO check validity

        val passwordStr = et_password.text.toString()
        val confirmPasswordStr = et_confirm_password.text.toString()

        if (passwordStr != confirmPasswordStr) {
            et_password.error = "Password do not match"
            et_confirm_password.error = "Password do not match"
            return
        }

        // TODO not completed yet, eg. Role
        authenticationViewModel.signUp(SignUpRequest(et_name.text.toString(), et_username.text.toString(), passwordStr))
    }

    override fun onAttach(context: Context?) {
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
