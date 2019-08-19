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
import com.labs.sauti.view_model.AuthenticationViewModel
import kotlinx.android.synthetic.main.fragment_sign_in.*
import javax.inject.Inject

class SignInFragment : Fragment() {

    private var openSignUpListener: OpenSignUpListener? = null

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

        // error
        authenticationViewModel.getErrorLiveData().observe(this, Observer {
            // TODO better error showing
            Toast.makeText(context, "Sign in failed. ${it.message}", Toast.LENGTH_LONG).show()
        })

        // success
        authenticationViewModel.getSignInViewState().observe(this, Observer {
            if (it.isLoading) {
                vs_sign_in.displayedChild = 1
            } else {
                vs_sign_in.displayedChild = 0

                if (it.isSuccess) {
                    Toast.makeText(context, "Sign in successful", Toast.LENGTH_LONG).show()
                    onSignInCompletedListener!!.onSignInCompleted()
                    activity!!.supportFragmentManager.popBackStack()
                }
            }
        })

        // sign in click
        b_sign_in.setOnClickListener {
            signIn()
        }

        // sign up click
        t_sign_up.setOnClickListener {
            openSignUp()
        }
    }

    private fun signIn() {
        // TODO check validity
        authenticationViewModel.signIn(et_username.text.toString(), et_password.text.toString())
    }

    private fun openSignUp() {
        activity!!.supportFragmentManager.popBackStack()
        openSignUpListener?.openSignUp()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnSignInCompletedListener) {
            onSignInCompletedListener = context
        } else {
            throw RuntimeException("Context must implement OnSignInCompletedListener")
        }

        if (context is OpenSignUpListener) {
            openSignUpListener = context
        } else {
            throw RuntimeException("Context must implement OpenSignUpListener")
        }
    }

    override fun onDetach() {
        super.onDetach()

        onSignInCompletedListener = null
        openSignUpListener = null
    }

    // TODO implement EventBus library
    interface OnSignInCompletedListener {
        fun onSignInCompleted()
    }

    interface OpenSignUpListener {
        fun openSignUp()
    }
}
