package com.naufalprakoso.billreminder.ui.main.paid

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.naufalprakoso.billreminder.R
import com.naufalprakoso.billreminder.database.AppDatabase
import com.naufalprakoso.billreminder.database.DbWorkerThread
import com.naufalprakoso.billreminder.database.entity.Bill
import com.naufalprakoso.billreminder.ui.bill.detail.BillDetailActivity
import com.naufalprakoso.billreminder.utils.BILL_ID
import kotlinx.android.synthetic.main.fragment_bill_paid.view.*

class BillPaidFragment : Fragment() {

    private val bills = arrayListOf<Bill>()

    private var db: AppDatabase? = null
    private lateinit var dbWorkerThread: DbWorkerThread
    private val handler = Handler()

    private lateinit var adapter: BillPaidAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bill_paid, container, false)

        view.rv_bills?.setHasFixedSize(true)
        view.rv_bills?.layoutManager = LinearLayoutManager(context)
        view.rv_bills?.adapter = adapter

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (activity != null) {
            dbWorkerThread = DbWorkerThread("dbWorkerThread")
            dbWorkerThread.start()

            if (context != null) {
                db = AppDatabase.getInstance(context!!)

                adapter =
                    BillPaidAdapter(context!!) { bill ->
                        val intent = Intent(context, BillDetailActivity::class.java)
                        intent.putExtra(BILL_ID, bill)
                        startActivity(intent)
                    }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        getBillData()
    }

    private fun getBillData() {
        bills.clear()
        val task = Runnable {
            val billData = db?.billDao()?.getPaidBills()
            handler.post {
                billData?.let { bills.addAll(it) }
                if (bills.isEmpty()) {
                    view?.tv_no_data?.visibility = View.VISIBLE
                    view?.rv_bills?.visibility = View.GONE
                } else {
                    view?.tv_no_data?.visibility = View.GONE
                    view?.rv_bills?.visibility = View.VISIBLE
                }
                adapter.setBills(bills)
                adapter.notifyDataSetChanged()
            }
        }
        dbWorkerThread.postTask(task)
    }

    companion object {
        fun newInstance(): Fragment {
            return BillPaidFragment()
        }
    }

}
