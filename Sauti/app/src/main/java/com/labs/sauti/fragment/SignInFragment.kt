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
import com.labs.sauti.view_model.AuthenticationViewModel
import kotlinx.android.synthetic.main.fragment_sign_in.*
import java.lang.RuntimeException
import javax.inject.Inject

class SignInFragment : Fragment() {

    @Inject
    lateinit var authenticationViewModelFactory: AuthenticationViewModel.Factory

    private lateinit var authenticationViewModel: AuthenticationViewModel

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

        (context!!.applicationContext as SautiApp).getAuthenticationComponent().inject(this)
        authenticationViewModel = ViewModelProviders.of(this, authenticationViewModelFactory).get(AuthenticationViewModel::class.java)
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
        authenticationViewModel.getLoginResponseLiveData().observe(this, Observer<LoginResponse> {
            Toast.makeText(context, "Sign in successful", Toast.LENGTH_LONG).show()

            onSignInCompletedListener!!.onSignInCompleted()

            activity!!.supportFragmentManager.popBackStack()
        })

        // error
        authenticationViewModel.getErrorLiveData().observe(this, Observer {
            Toast.makeText(context, "Sign in failed. ${it.errorDescription}", Toast.LENGTH_LONG).show()
        })

        // button
        b_sign_in.setOnClickListener {
            authenticationViewModel.signIn(et_username.text.toString(), et_password.text.toString())
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
