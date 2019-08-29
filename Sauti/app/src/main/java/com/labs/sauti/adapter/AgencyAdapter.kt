package com.labs.sauti.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.labs.sauti.R
import com.labs.sauti.model.trade_info.BorderAgency
import kotlinx.android.synthetic.main.required_document_agencies_element_layout.view.*

class AgencyAdapter(
    private val agencies: MutableList<BorderAgency> = mutableListOf(),
    private val listener: (BorderAgency) -> Unit
) : RecyclerView.Adapter<AgencyAdapter.ViewHolder>() {

    override fun getItemCount(): Int = agencies.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(agencies[position], listener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.required_document_agencies_element_layout,
                parent,
                false
            )
        )
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        private val agencyName : TextView = view.rv_tv_document_agency_name
        private val plusSign : TextView = view.rv_tv_plus


        fun bind(data : BorderAgency, listener: (BorderAgency) -> Unit) = with(itemView) {
            agencyName.text = """${(adapterPosition + 1)}:${data.agencyName}"""
            plusSign.text = context.getString(R.string.list_plus)
            setOnClickListener { listener(data) }
        }
    }


}