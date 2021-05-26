package com.medallia.anywhere_versioning_sampleapp.ui.activities.forms

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.medallia.anywhere_versioning_sampleapp.R
import com.medallia.anywhere_versioning_sampleapp.model.Form

class FormAdapter  constructor(
    var listener: OnFormClickListener
) :
    RecyclerView.Adapter<FormAdapter.FormViewHolder>() {

    private  var currentForm : Form? = null
    var formList: List<Form> = arrayListOf()

    inner class FormViewHolder(formView: View) : RecyclerView.ViewHolder(formView), View.OnClickListener {
        val title: TextView = formView.findViewById(R.id.formTitle_text_view)
        val id: TextView = formView.findViewById(R.id.formId_text_view)

        init {
            formView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            currentForm = formList[position]

            currentForm?.let {
                if (position != RecyclerView.NO_POSITION) {
                    listener.onFormClicked(it)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.recycle_view_item,
            parent, false)

        return FormViewHolder(itemView)
    }

    override fun getItemCount() = formList.size

    override fun onBindViewHolder(holder: FormViewHolder, position: Int) {
        currentForm = formList[position]

        currentForm?.let {
            holder.title.text = it.name
            holder.id.text = it.formId
        }

    }

    fun setForms(formList: List<Form>) {
        this.formList = formList
        notifyDataSetChanged()
    }

    interface OnFormClickListener {
        fun onFormClicked(form: Form)
    }
}