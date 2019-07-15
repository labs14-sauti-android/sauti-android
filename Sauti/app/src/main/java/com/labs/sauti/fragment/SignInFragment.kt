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
import com.labs.sauti.model.LoginResponse
import com.labs.sauti.sp.SessionSp
import com.labs.sauti.view_model.LoginViewModel
import kotlinx.android.synthetic.main.fragment_sign_in.*
import java.lang.RuntimeException
import javax.inject.Inject

class SignInFragment : Fragment() {

    @Inject
    lateinit var loginViewModelFactory: LoginViewModel.Factory

    private lateinit var loginViewModel: LoginViewModel

    @Inject
    lateinit var sessionSp: SessionSp

    private var onSignInCompletedListener: OnSignInCompletedListener? = null

    companion object {
        fun newInstance(): SignInFragment {
            return SignInFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }

        (context!!.applicationContext as SautiApp).getLoginComponent().inject(this)
        loginViewModel = ViewModelProviders.of(this, loginViewModelFactory).get(LoginViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // success
        loginViewModel.getLoginResponseLiveData().observe(this, Observer<LoginResponse> {
            sessionSp.setAccessToken(it.accessToken ?: "")
            sessionSp.setExpiresIn(it.expiresIn ?: 0)
            sessionSp.setLoggedInAt(System.currentTimeMillis() / 1000L)

            Toast.makeText(context, "Login successful", Toast.LENGTH_LONG).show()

            onSignInCompletedListener!!.onSignInCompleted()

            activity!!.supportFragmentManager.popBackStack()
        })

        // error
        loginViewModel.getErrorLiveData().observe(this, Observer {
            Toast.makeText(context, "Login failed. ${it.errorDescription}", Toast.LENGTH_LONG).show()
        })

        // button
        b_sign_in.setOnClickListener {
            loginViewModel.login(et_username.text.toString(), et_password.text.toString())
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is OnSignInCompletedListener) {
            onSignInCompletedListener = context
        } else {
            throw RuntimeException("Context must implement OnSignInCompletedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()

        onSignInCompletedListener = null
    }

    // TODO implement EventBus library
    interface OnSignInCompletedListener {
        fun onSignInCompleted()
    }
}
