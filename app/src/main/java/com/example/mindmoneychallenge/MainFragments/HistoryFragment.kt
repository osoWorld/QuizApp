package com.example.mindmoneychallenge.MainFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mindmoneychallenge.AdapterClass.HistoryAdapter
import com.example.mindmoneychallenge.DataAndModelClass.History
import com.example.mindmoneychallenge.DataAndModelClass.UserSignup
import com.example.mindmoneychallenge.R
import com.example.mindmoneychallenge.Withdrawal.CoinWithdrawalFragment
import com.example.mindmoneychallenge.databinding.FragmentHistoryBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.util.Collections

class HistoryFragment : Fragment() {
    private val binding: FragmentHistoryBinding by lazy {
        FragmentHistoryBinding.inflate(layoutInflater)
    }

    private var historyList = ArrayList<History>()
    private lateinit var adapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Firebase.database.reference.child("PlayerCoinHistory").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(
                object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        historyList.clear()
                        var historyList1 = ArrayList<History>()
                        for (datasnapshot in snapshot.children){
                            var data = datasnapshot.getValue(History::class.java)
                            historyList1.add(data!!)

                        }
                        historyList1.reverse()
                        historyList.addAll(historyList1)
                        adapter.notifyDataSetChanged()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                }
            )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.historyRecView.layoutManager = LinearLayoutManager(requireContext())
        adapter = HistoryAdapter(historyList)

        binding.historyRecView.adapter = adapter

        binding.homeCoin.setOnClickListener {
            val bottomSheetDialog : BottomSheetDialogFragment = CoinWithdrawalFragment()
            bottomSheetDialog.show(requireActivity().supportFragmentManager,"Coin")
            bottomSheetDialog.enterTransition
        }
        binding.homeCoinAmount.setOnClickListener {
            val bottomSheetDialog : BottomSheetDialogFragment = CoinWithdrawalFragment()
            bottomSheetDialog.show(requireActivity().supportFragmentManager,"Coin")
            bottomSheetDialog.enterTransition
        }

        Firebase.database.reference.child("Users").child(Firebase.auth.currentUser!!.uid)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var user = snapshot.getValue<UserSignup>()
                        binding.userNameHome.text = user?.name
//                        binding.profilePersonalEmail.text = user?.email
//                        binding.profilePersonalPassword.text = user?.password
//                        binding.profilePersonalAge.text = user?.age
                    }

                    override fun onCancelled(error: DatabaseError) {
//                        TODO("Not yet implemented")
                    }

                }
            )

        Firebase.database.reference.child("PlayerCoin").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            var currentCoin = snapshot.value as Long
                            binding.homeCoinAmount.text = currentCoin.toString()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                }
            )
        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {

    }
}