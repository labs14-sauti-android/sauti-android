package com.labs.sauti.fragment



import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.labs.sauti.R
import com.labs.sauti.model.trade_info.TradeInfo
import kotlinx.android.synthetic.main.fragment_trade_info.*
import com.labs.sauti.SautiApp
import com.labs.sauti.activity.BaseActivity
import com.labs.sauti.view_model.TradeInfoViewModel
import javax.inject.Inject


//TODO: Add a clicklistener to the items in required documents in class Lance wants that clickable

class TradeInfoFragment : Fragment(), TradeInfoSearchFragment.onTradeInfoSearchCompletedListener,
OnFragmentFullScreenStateChangedListener{


    private var onFragmentFullScreenStateChangedListener: OnFragmentFullScreenStateChangedListener? = null

    @Inject
    lateinit var tradeInfoViewModelFactory: TradeInfoViewModel.Factory

    private lateinit var tradeInfoViewModel: TradeInfoViewModel

    var tiDetailsIsVisible = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        context?.let {
            (it.applicationContext as SautiApp).getTradeInfoComponent().inject(this)
            tradeInfoViewModel= ViewModelProviders.of(this, tradeInfoViewModelFactory).get(TradeInfoViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trade_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeTradeInfoViewModel()






        b_trade_info_search.setOnClickListener{
            openTradeInfoSearchFragment()
        }

    }


    fun observeTradeInfoViewModel() {

        //Border Procedures
        // Category -> Product -> Going Where -> Origin Made -> Value
        //fun loadFirstSp

    }


    fun addTIDetailsLL(tradeInfo: TradeInfo) {
        l_tradeinfo_left_list.removeAllViews()
        l_tradeinfo_right_list.removeAllViews()
        var half = (tradeInfo.tradeinfoList.size) / 2


        for (i in 0 until (tradeInfo.tradeinfoList.size)) {
            //TODO: Change language so left LL will have one more if odd number of elements.
            val textView = TextView(context)
            TextViewCompat.setTextAppearance(textView, R.style.CardViewRecentDetailsListTextStyling)
            textView.text = tradeInfo.tradeinfoList[i]
            textView.setOnClickListener {
                //TODO: Add a child fragment explaining what that doc is when clicked.
            }

            when {
                i > half -> l_tradeinfo_right_list.addView(textView)
                i == half -> l_tradeinfo_left_list.addView(textView)
                else -> l_tradeinfo_left_list.addView(textView)
            }
        }
    }


    companion object {

        private const val MAX_RECENT_ITEMS = 2

        @JvmStatic
        fun newInstance() =
            TradeInfoFragment()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is OnFragmentFullScreenStateChangedListener) {
            onFragmentFullScreenStateChangedListener = context
        } else {
            throw RuntimeException("Context must implement OnFragmentFullScreenStateChangedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        onFragmentFullScreenStateChangedListener = null
    }

    override fun onTradeInfoSearchCompleted(tradeInfo: TradeInfo) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFragmetFullScreenStateChanged(isFullScreen: Boolean) {
        onFragmentFullScreenStateChangedListener?.onFragmetFullScreenStateChanged(isFullScreen)
    }

    private fun openTradeInfoSearchFragment() {
        val tradeInfoSearchFragment = TradeInfoSearchFragment.newInstance()

        childFragmentManager.beginTransaction()
            .add(R.id.fl_fragment_container_trade_info, tradeInfoSearchFragment)
            .addToBackStack(null)?.commit()
    }
    //TODO: Must remove - Testing to see layout. 
}

/* //TODO Reimplement animated views.
tiv_recent_first.setOnClickListener(object : View.OnClickListener {
            var visible: Boolean = tiDetailsIsVisible

            override fun onClick(v: View) {
                t_trade_info_header.text = testTIbanned.tradeinfoTopicExpanded
                addTIDetailsLL(testTIbanned)

                //1. Check if the view is visible
                //2. If not visibile make visible
                if(!tiDetailsIsVisible) {
                    TransitionManager.beginDelayedTransition(cl_fragment_container_trade_info)
                    visible = !visible

                    cl_expanded_trade_info.visibility = if (visible) View.VISIBLE else View.GONE
                    tiDetailsIsVisible = true
                } else {
                    TransitionManager.beginDelayedTransition(cl_fragment_container_trade_info)
                }
            }
        })

        //TODO: Logic cleanup. Needs Animations!
        tiv_recent_second.setOnClickListener(object : View.OnClickListener {
            var visible: Boolean = tiDetailsIsVisible

            override fun onClick(v: View) {
                t_trade_info_header.text = testTIdocuments.tradeinfoTopicExpanded
                addTIDetailsLL(testTIdocuments)

                if(!tiDetailsIsVisible) {
                    TransitionManager.beginDelayedTransition(cl_fragment_container_trade_info)
                    visible = !visible
                    cl_expanded_trade_info.visibility = if (visible) View.VISIBLE else View.GONE
                    tiDetailsIsVisible = true
                } else {
                    TransitionManager.beginDelayedTransition(cl_fragment_container_trade_info)
                }

            }
        })
 */


//tiv_recent_first.setOnClickListener{
//
//    tiv_recent_first.animate()
//        .translationY(tomove)
//        .setListener(object : AnimatorListenerAdapter() {
//
//            override fun onAnimationEnd(animation: Animator) {
//                super.onAnimationEnd(animation)
//                cl_expanded_tradeinfo.visibility = View.VISIBLE
//                cl_expanded_tradeinfo.alpha = 1.0f
//            }
//        })
//}


/*

//TODO: Remove dummy data creation
        testTIbanned = TradeInfo(
            0,
            "These commodities are banned:",
            "These commodities are banned and cannot legally cross the border:",
            listOf(
                "-Air Zinc",
                "-Batteries",
                "-Batteries",
                "-Khanga, kikoi, kitenge",
                "-Lithium",
                "-Maize",
                "-Manganese Dioxide",
                "-Matches",
                "-Mercuric Oxide",
                "-Rice",
                "-Silver Oxide",
                "-Sugar",
                "-Tobacco",
                "-Used clothing",
                "-Wheat"
            )
        )

        //TODO: Remove dummy data creation
        testTIdocuments = TradeInfo(
            1,
            "Required Documents:",
            "Required Documents...expanded",
            listOf(
                "1. Import ",
                "2. Valid Invoice",
                "3. Simplified Certificate Of Origin (SCOO)",
                "4. National ID",
                "5. Something"
            )
        )
 */
