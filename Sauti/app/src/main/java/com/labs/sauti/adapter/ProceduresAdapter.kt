package com.labs.sauti.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.labs.sauti.R
import com.labs.sauti.model.trade_info.Procedure
import kotlinx.android.synthetic.main.border_procedures_element_layout.view.*

class ProceduresAdapter(
    private val procedures: MutableList<Procedure> = mutableListOf()
) : RecyclerView.Adapter<ProceduresAdapter.ViewHolder>() {

    override fun getItemCount(): Int = procedures.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(procedures[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.border_procedures_element_layout,
                parent,
                false
            )
        )
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val bpStep : TextView = view.rv_tv_border_procedures_step
        private val bpStepDescrip : TextView = view.rv_tv_border_procedures_desc

        fun bind(data : Procedure) {
            bpStep.text = data.step.toString() + ":"
            bpStepDescrip.text = data.description

        }
    }

    fun updateContents(newData: MutableList<Procedure>) {
        procedures.clear()
        procedures.addAll(newData)
        notifyDataSetChanged()
    }

}