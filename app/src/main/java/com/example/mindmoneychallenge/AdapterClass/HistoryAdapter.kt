package com.example.mindmoneychallenge.AdapterClass

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mindmoneychallenge.DataAndModelClass.History
import com.example.mindmoneychallenge.databinding.HistoryItemBinding
import java.sql.Date
import java.sql.Timestamp

class HistoryAdapter(private var historyList: ArrayList<History>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    class HistoryViewHolder(var binding: HistoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            HistoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = historyList.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val hisDataList = historyList[position]

        var timeStamp = Timestamp(historyList[position].dateAndTime.toLong())
        holder.binding.historyDateandTime.text = Date(timeStamp.time).toString()
        holder.binding.status.text = if (historyList[position].isWithdrawal) {
            "Money Withdrawn"
        } else {
            "Money Earned"
        }
        holder.binding.historyAmount.text = hisDataList.coinAmount
    }
}