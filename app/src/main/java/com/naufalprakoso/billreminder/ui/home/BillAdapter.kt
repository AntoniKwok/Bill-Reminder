package com.naufalprakoso.billreminder.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.naufalprakoso.billreminder.R
import com.naufalprakoso.billreminder.database.entity.Bill
import kotlinx.android.synthetic.main.item_bill.view.*

class BillAdapter : RecyclerView.Adapter<BillAdapter.ViewHolder>() {

    private val bills = arrayListOf<Bill>()

    fun setBills(bills: List<Bill>) {
        this.bills.addAll(bills)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_bill, parent, false))

    override fun getItemCount(): Int = bills.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(bills[position])
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        fun bindItem(bill: Bill) {
            itemView.tv_title.text = bill.title
            itemView.tv_amount.text = bill.amount.toString()
            itemView.tv_content.text = bill.content
            itemView.cb_paid.isChecked = bill.paid.toBoolean()

            itemView.cb_paid.setOnClickListener {
                println("${bill.title} has been paid")
            }
        }
    }

}