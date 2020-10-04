package com.naufalprakoso.billreminder.ui.main.paid

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.naufalprakoso.billreminder.database.entity.Bill
import com.naufalprakoso.billreminder.databinding.FragmentBillPaidBinding
import com.naufalprakoso.billreminder.ui.bill.detail.BillDetailActivity
import com.naufalprakoso.billreminder.utils.BILL_ID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BillPaidFragment : Fragment() {
    private lateinit var adapter: BillPaidAdapter
    private lateinit var binding: FragmentBillPaidBinding

    private val billViewModel: BillPaidViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBillPaidBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (activity != null && context != null) {
            initAdapter()
            binding.rvBills.setHasFixedSize(true)
            binding.rvBills.layoutManager = LinearLayoutManager(context)
            binding.rvBills.adapter = adapter

            billViewModel.getBills().observe(viewLifecycleOwner, Observer {
                renderBillData(it)
            })
        }
    }

    private fun initAdapter() {
        adapter = BillPaidAdapter(requireContext()) { bill ->
            val intent = Intent(context, BillDetailActivity::class.java)
            intent.putExtra(BILL_ID, bill)
            startActivity(intent)
        }
    }

    private fun renderBillData(bills: List<Bill>) {
        if (bills.isEmpty()) {
            binding.tvNoData.visibility = View.VISIBLE
            binding.rvBills.visibility = View.GONE
        } else {
            binding.tvNoData.visibility = View.GONE
            binding.rvBills.visibility = View.VISIBLE
        }
        setBillData(bills)
    }

    private fun setBillData(bills: List<Bill>) {
        adapter.setBills(bills)
        adapter.notifyDataSetChanged()
    }

    companion object {
        fun newInstance(): Fragment {
            return BillPaidFragment()
        }
    }

}
