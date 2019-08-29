package com.labs.sauti.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.labs.sauti.R
import com.labs.sauti.model.trade_info.BorderAgency
import com.labs.sauti.model.trade_info.RequiredDocument
import kotlinx.android.synthetic.main.required_document_agencies_element_layout.view.*

class TradeInfoAdapter(
    private val list: MutableList<Any>,
    private val listener: (Any)-> Unit
) : RecyclerView.Adapter<TradeInfoAdapter.ViewHolder>() {

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.required_document_agencies_element_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], listener)
    }

    fun updateContents(newData: MutableList<Any>) {
        list.clear()
        list.addAll(newData)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

        private val name : TextView = view.rv_tv_document_agency_name
        private val plusSign : TextView = view.rv_tv_plus

        fun bind(data: Any, listener: (Any) -> Unit) = with(itemView) {
            plusSign.text = context.getString(R.string.list_plus)

            if(data is RequiredDocument) {
                name.text =  """${(adapterPosition + 1)}:${data.docTitle}"""
            }

            if(data is BorderAgency) {
                name.text = """${(adapterPosition + 1)}:${data.agencyName}"""
            }

            setOnClickListener{listener(data)}
        }
    }

}