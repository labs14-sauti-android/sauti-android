package com.labs.sauti.fragment

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.labs.sauti.R
import kotlinx.android.synthetic.main.fragment_trade_info_details.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TradeInfoDetailsFragment : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var title: String? = null
    private var content: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

            title = it.getString(ARG_PARAM1)
            content = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_trade_info_details, container, false)
    }

/*    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog

    }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog.window!!.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(context!!, android.R.color.transparent)))

        t_trade_info_details_title.text = title
        t_trade_info_details_content.text = content


/*        c_trade_info_details.setOnClickListener{
            dismiss()
        }*/
    }

/*    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(parentFragment is OnFragmentFullScreenStateChangedListener) {
            onFragmentFullScreenStateChangedListener = parentFragment as OnFragmentFullScreenStateChangedListener
            onFragmentFullScreenStateChangedListener!!.onFragmetFullScreenStateChanged(true)
        }  else {
            throw RuntimeException("parentFragment must implement OnFragmentFullScreenStateChangedListener")
        }
    }*/

/*    override fun onDetach() {
        super.onDetach()

        onFragmentFullScreenStateChangedListener?.onFragmetFullScreenStateChanged(false)
        onFragmentFullScreenStateChangedListener = null
    }*/

    companion object {
        @JvmStatic
/*        fun newInstance(param1: String, param2: String) =
            TradeInfoDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }*/
        fun newInstance(title: String, content: String) =
            TradeInfoDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, title)
                    putString(ARG_PARAM2, content)
                }
            }
     }
}

