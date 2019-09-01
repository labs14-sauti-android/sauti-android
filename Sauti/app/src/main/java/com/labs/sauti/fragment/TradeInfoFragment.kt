package com.labs.sauti.fragment



import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionManager
import com.labs.sauti.R
import com.labs.sauti.model.trade_info.TradeInfo
import com.labs.sauti.SautiApp
import com.labs.sauti.adapter.ProceduresAdapter
import com.labs.sauti.adapter.TradeInfoAdapter
import com.labs.sauti.helper.SimpleDividerItemDecoration
import com.labs.sauti.model.trade_info.BorderAgency
import com.labs.sauti.model.trade_info.Procedure
import com.labs.sauti.model.trade_info.RequiredDocument
import com.labs.sauti.view_model.TradeInfoViewModel
import kotlinx.android.synthetic.main.fragment_trade_info.*
import javax.inject.Inject


//TODO: Add a clicklistener to the items in required documents in class Lance wants that clickable

class TradeInfoFragment : Fragment(), TradeInfoSearchFragment.OnTradeInfoSearchCompletedListener,
OnFragmentFullScreenStateChangedListener{

    private var onFragmentFullScreenStateChangedListener: OnFragmentFullScreenStateChangedListener? = null

    @Inject
    lateinit var tradeInfoViewModelFactory: TradeInfoViewModel.Factory

    private lateinit var tradeInfoViewModel: TradeInfoViewModel

    var tradeInfoFocus : TradeInfo? = null
    lateinit var firstTradeInfo: TradeInfo
    lateinit var secondTradeInfo: TradeInfo

    lateinit var tradeInfoAdapter : TradeInfoAdapter
    lateinit var proceduresAdapter: ProceduresAdapter





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
        return inflater.inflate(R.layout.fragment_trade_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tiv_trade_info_recent_first

        tradeInfoViewModel.getSearchTradeInfoRecents().observe(this, Observer {

            if(it.isNullOrEmpty()) {
                t_push_to_view.visibility = View.GONE
                tiv_trade_info_recent_first.visibility = View.GONE
                tiv_trade_info_recent_second.visibility = View.GONE
                t_trade_info_no_recent.visibility = View.VISIBLE
            }

            if(it.size == 1){
                t_push_to_view.visibility = View.VISIBLE
                tiv_trade_info_recent_first.visibility = View.VISIBLE
                tiv_trade_info_recent_second.visibility = View.INVISIBLE
                t_trade_info_no_recent.visibility = View.GONE

                tiv_trade_info_recent_first.consumeTradeInfo(it[0])
                firstTradeInfo = it[0]
                tiv_trade_info_recent_first.setOnClickListener(tradeClickListener)
            }

            if(it.size == 2) {
                t_push_to_view.visibility = View.VISIBLE
                tiv_trade_info_recent_first.visibility = View.VISIBLE
                tiv_trade_info_recent_second.visibility = View.VISIBLE
                t_trade_info_no_recent.visibility = View.GONE

                tiv_trade_info_recent_first.consumeTradeInfo(it[0])
                tiv_trade_info_recent_second.consumeTradeInfo(it[1])

                firstTradeInfo = it[0]
                secondTradeInfo = it[1]

                tiv_trade_info_recent_first.setOnClickListener(tradeClickListener)
                tiv_trade_info_recent_second.setOnClickListener(tradeClickListener)
            }
        })

        tradeInfoViewModel.searchRecentTradeInfo()

        fab_trade_info_search.setOnClickListener{
            openTradeInfoSearchFragment()
        }

    }

    val tradeClickListener: View.OnClickListener = View.OnClickListener {
        when(it.id) {
            R.id.tiv_trade_info_recent_first->{
                when (tradeInfoFocus) {
                    null->{
                        TransitionManager.beginDelayedTransition(fl_fragment_container_trade_info)
                        addTIDetailsLL(firstTradeInfo)
                        cl_expanded_trade_info.visibility = View.VISIBLE
                        tradeInfoFocus = firstTradeInfo

                    }
                    firstTradeInfo->{
                        TransitionManager.beginDelayedTransition(fl_fragment_container_trade_info)
                        cl_expanded_trade_info.visibility = View.GONE
                        tradeInfoFocus = null
                    }
                    secondTradeInfo->{
                        addTIDetailsLL(firstTradeInfo)
                        tradeInfoFocus = firstTradeInfo
                    }
                }
            }
            R.id.tiv_trade_info_recent_second->{
                when (tradeInfoFocus) {
                    null->{
                        TransitionManager.beginDelayedTransition(fl_fragment_container_trade_info)
                        addTIDetailsLL(secondTradeInfo)
                        cl_expanded_trade_info.visibility = View.VISIBLE
                        tradeInfoFocus = secondTradeInfo
                    }
                    firstTradeInfo->{
                        addTIDetailsLL(secondTradeInfo)
                        tradeInfoFocus = secondTradeInfo

                    }
                    secondTradeInfo->{
                        TransitionManager.beginDelayedTransition(fl_fragment_container_trade_info)
                        cl_expanded_trade_info.visibility = View.GONE
                        tradeInfoFocus = null
                    }
                }
            }
        }
    }

    companion object {

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

    override fun OnTradeInfoSearchCompleted(tradeInfo: TradeInfo) {

        addTIDetailsLL(tradeInfo)
        cl_expanded_trade_info.visibility = View.VISIBLE
        tradeInfoFocus = tradeInfo
        tradeInfoViewModel.searchRecentTradeInfo()
    }

    fun addTIDetailsLL(tradeInfo: TradeInfo) {


        //Border Procedures
        rv_trade_info_border_procedures.visibility = View.GONE

        //Regulated Goods
        l_trade_info_left_list.visibility = View.GONE
        l_trade_info_right_list.visibility = View.GONE

        //Required Documents
        t_trade_info_sub_header.visibility = View.GONE
        rv_trade_info_required_documents.visibility = View.GONE
        i_trade_info_divider_top.visibility = View.GONE
        i_trade_info_divider_bottom.visibility = View.GONE

        when(tradeInfo.tradeinfoTopic) {
            "Border Procedures"->{
                t_trade_info_header.text = tradeInfo.tradeinfoTopic
                t_trade_info_header.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                t_trade_info_sub_header.text = tradeInfo.tradeinfoTopicExpanded

                t_trade_info_sub_header.visibility = View.VISIBLE
                rv_trade_info_border_procedures.visibility = View.VISIBLE
                rv_trade_info_border_procedures.setHasFixedSize(true)
                rv_trade_info_border_procedures.layoutManager = LinearLayoutManager(context)

                if(::proceduresAdapter.isInitialized){
                    if(tradeInfo != tradeInfoFocus) {
                        val list = mutableListOf<Procedure>()
                        list.addAll(tradeInfo.tradeInfoProcedure!!)

                        proceduresAdapter.updateContents(list)
                    }

                } else {
                    proceduresAdapter = ProceduresAdapter(tradeInfo.tradeInfoProcedure!!)
                    rv_trade_info_border_procedures.adapter = proceduresAdapter
                }


            }
            "Required Documents"->{
                t_trade_info_header.text = tradeInfo.tradeinfoTopic
                t_trade_info_header.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                t_trade_info_sub_header.text = tradeInfo.tradeinfoTopicExpanded

                i_trade_info_divider_top.visibility = View.VISIBLE
                i_trade_info_divider_bottom.visibility = View.VISIBLE
                t_trade_info_sub_header.visibility = View.VISIBLE
                rv_trade_info_required_documents.visibility = View.VISIBLE

                rv_trade_info_required_documents.setHasFixedSize(true)

                if(::tradeInfoAdapter.isInitialized) {
                    if(tradeInfo != tradeInfoFocus) {
                        val list = mutableListOf<Any>()
                        list.addAll(tradeInfo.tradeInfoDocs!!)

                        tradeInfoAdapter.updateContents(list)
                    }
                } else {
                    val list = mutableListOf<Any>()
                    list.addAll(tradeInfo.tradeInfoDocs!!)

                    tradeInfoAdapter = TradeInfoAdapter(list) {
                        if (it is RequiredDocument) {
                            val tradeInfoDetailsFragment =
                                TradeInfoDetailsFragment
                                    .newInstance(it.docTitle, it.docDescription)
                            tradeInfoDetailsFragment.show(childFragmentManager, "reqDocs")
                        }

                        if(it is BorderAgency) {
                            val tradeinfoDetailsFragment =
                                TradeInfoDetailsFragment
                                    .newInstance(it.agencyName, it.agencyDescription)
                            tradeinfoDetailsFragment.show(childFragmentManager, "bordAgencies")
                        }
                    }

                    rv_trade_info_required_documents.adapter = tradeInfoAdapter
                }

                if(rv_trade_info_required_documents.itemDecorationCount == 0) {
                    rv_trade_info_required_documents
                        .addItemDecoration(SimpleDividerItemDecoration(context!!))

                }
            }

            "Border Agencies"->{
                t_trade_info_header.text = tradeInfo.tradeinfoTopic
                t_trade_info_header.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                t_trade_info_sub_header.text = tradeInfo.tradeinfoTopicExpanded

                i_trade_info_divider_top.visibility = View.VISIBLE
                i_trade_info_divider_bottom.visibility = View.VISIBLE
                t_trade_info_sub_header.visibility = View.VISIBLE
                rv_trade_info_required_documents.visibility = View.VISIBLE

                rv_trade_info_required_documents.setHasFixedSize(true)

                if(::tradeInfoAdapter.isInitialized) {
                    if(tradeInfo != tradeInfoFocus) {
                        val list = mutableListOf<Any>()
                        list.addAll(tradeInfo.tradeInfoAgencies!!)

                        tradeInfoAdapter.updateContents(list)
                    }
                } else {
                    val list = mutableListOf<Any>()
                    list.addAll(tradeInfo.tradeInfoAgencies!!)

                    tradeInfoAdapter = TradeInfoAdapter(list) {
                        if (it is RequiredDocument) {
                            val tradeInfoDetailsFragment =
                                TradeInfoDetailsFragment
                                    .newInstance(it.docTitle, it.docDescription)
                            tradeInfoDetailsFragment.show(childFragmentManager, "reqDocs")
                        }

                        if(it is BorderAgency) {
                            val tradeinfoDetailsFragment =
                                TradeInfoDetailsFragment
                                    .newInstance(it.agencyName, it.agencyDescription)
                            tradeinfoDetailsFragment.show(childFragmentManager, "bordAgencies")
                        }
                    }

                    rv_trade_info_required_documents.adapter = tradeInfoAdapter
                }

                if(rv_trade_info_required_documents.itemDecorationCount == 0) {
                    rv_trade_info_required_documents
                        .addItemDecoration(SimpleDividerItemDecoration(context!!))

                }
            }

            "Regulated Goods" ->{
                l_trade_info_left_list.removeAllViews()
                l_trade_info_right_list.removeAllViews()
                l_trade_info_left_list.visibility = View.VISIBLE
                l_trade_info_right_list.visibility = View.VISIBLE
                t_trade_info_header.visibility = View.VISIBLE

                t_trade_info_header.text = tradeInfo.tradeinfoTopicExpanded
                t_trade_info_header.paintFlags = Paint.UNDERLINE_TEXT_FLAG


                val half = (tradeInfo.tradeinfoList!!.size) / 2


                for (i in 0 until (tradeInfo.tradeinfoList!!.size)) {
                    val textView = TextView(context)
                    TextViewCompat.setTextAppearance(textView, R.style.CardViewRecentDetailsListTextStyling)
                    textView.text = "- ${tradeInfo.tradeinfoList!![i]}"
                    textView.setOnClickListener {
                        //TODO: Add a child fragment explaining what that doc is when clicked.
                    }

                    when {
                        i > half -> l_trade_info_right_list.addView(textView)
                        i == half -> l_trade_info_left_list.addView(textView)
                        else -> l_trade_info_left_list.addView(textView)
                    }
                }
            }

        }


    }


    override fun onFragmetFullScreenStateChanged(isFullScreen: Boolean) {
        onFragmentFullScreenStateChangedListener?.onFragmetFullScreenStateChanged(isFullScreen)
    }

    private fun openTradeInfoSearchFragment() {
        val tradeInfoSearchFragment = TradeInfoSearchFragment.newInstance()

        childFragmentManager.beginTransaction()
            .add(R.id.fl_fragment_container_trade_info, tradeInfoSearchFragment)
            .addToBackStack(null)
            .commit()
    }
}
