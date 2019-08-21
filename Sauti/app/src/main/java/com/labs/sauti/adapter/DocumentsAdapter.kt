package com.labs.sauti.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.labs.sauti.R
import com.labs.sauti.model.trade_info.RequiredDocument
import kotlinx.android.synthetic.main.required_document_agencies_element_layout.view.*

class DocumentsAdapter(
    private val docs: MutableList<RequiredDocument> = mutableListOf(),
    private val listener: (RequiredDocument) -> Unit
) : RecyclerView.Adapter<DocumentsAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(docs[position], listener)
    }


    override fun getItemCount(): Int = docs.size

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

        private val docName : TextView = view.rv_tv_document_agency_name


        fun bind(data : RequiredDocument, listener: (RequiredDocument) -> Unit) = with(itemView) {
            docName.text = """${(adapterPosition + 1)}:${data.docTitle}"""
            setOnClickListener { listener(data) }
        }
    }
}