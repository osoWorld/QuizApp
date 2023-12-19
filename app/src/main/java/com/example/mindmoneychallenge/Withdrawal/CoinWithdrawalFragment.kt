package com.example.mindmoneychallenge.Withdrawal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.mindmoneychallenge.DataAndModelClass.History
import com.example.mindmoneychallenge.R
import com.example.mindmoneychallenge.databinding.FragmentCoinWithdrawalBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CoinWithdrawalFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentCoinWithdrawalBinding
    var currentCoin = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCoinWithdrawalBinding.inflate(layoutInflater, container, false)

        Firebase.database.reference.child("PlayerCoin").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            currentCoin = snapshot.value as Long

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                }
            )

        binding.transfereeBtn.setOnClickListener {
            val amount: String = binding.enterAmount.text.toString()

            if (amount.toDouble()<=currentCoin){
                Firebase.database.reference.child("PlayerCoin").child(Firebase.auth.currentUser!!.uid)
                    .setValue(currentCoin - amount.toDouble()).addOnSuccessListener {
                        Toast.makeText(context,"Transaction Complete",Toast.LENGTH_SHORT).show()

                        var timeMillis = System.currentTimeMillis().toString()
                        val historyModel = History(timeMillis,amount,true)
                        Firebase.database.reference.child("PlayerCoinHistory").child(Firebase.auth.currentUser!!.uid)
                            .push()
                            .setValue(historyModel)
                    }
            }else {
                Toast.makeText(context,"Coins not Enough",Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    companion object {

    }
}