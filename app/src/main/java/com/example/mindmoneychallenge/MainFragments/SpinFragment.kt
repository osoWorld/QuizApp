package com.example.mindmoneychallenge.MainFragments

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.mindmoneychallenge.DataAndModelClass.History
import com.example.mindmoneychallenge.DataAndModelClass.UserSignup
import com.example.mindmoneychallenge.Withdrawal.CoinWithdrawalFragment
import com.example.mindmoneychallenge.databinding.FragmentSpinBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class SpinFragment : Fragment() {
    private lateinit var binding: FragmentSpinBinding
    private lateinit var timer: CountDownTimer
    private val itemTitles = arrayOf("100", "Try Again", "500", "Try Again", "200", "Try Again")
    var currentChance = 0L
    var currentCoin = 0L
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = FragmentSpinBinding.inflate(inflater, container, false)

        Firebase.database.reference.child("Users").child(Firebase.auth.currentUser!!.uid)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var user = snapshot.getValue<UserSignup>()
                        binding.userNameHome.text = user?.name
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                }
            )

        Firebase.database.reference.child("PlayChance").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            currentChance = snapshot.value as Long
                            binding.spinChanceText.text = (snapshot.value as Long).toString()
                        } else {
                            val temp = 0
                            binding.spinChanceText.text = temp.toString()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                }
            )
        Firebase.database.reference.child("PlayerCoin").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            currentCoin = snapshot.value as Long
                            binding.homeCoinAmount.text = currentCoin.toString()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                }
            )

        return binding.root
    }

    private fun showResult(itemTitle: String, spin: Int) {
        if (spin % 2 == 0) {
            var winCoin = itemTitle.toInt()
            Firebase.database.reference.child("PlayerCoin").child(Firebase.auth.currentUser!!.uid)
                .setValue(winCoin + currentCoin)

            var timeMillis = System.currentTimeMillis().toString()
            val historyModel = History(timeMillis,winCoin.toString(),false)
            Firebase.database.reference.child("PlayerCoinHistory").child(Firebase.auth.currentUser!!.uid)
                .push()
                .setValue(historyModel)
            binding.homeCoinAmount.text = (winCoin + currentCoin).toString()
        }
        Toast.makeText(requireContext(), itemTitle, Toast.LENGTH_SHORT).show()
        currentChance -= 1
        Firebase.database.reference.child("PlayChance").child(Firebase.auth.currentUser!!.uid)
            .setValue(currentChance)
        binding.spinBtn.isEnabled = true
        binding.spinBtn.visibility = View.VISIBLE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homeCoin.setOnClickListener {
            val bottomSheetDialog: BottomSheetDialogFragment = CoinWithdrawalFragment()
            bottomSheetDialog.show(requireActivity().supportFragmentManager, "Coin")
            bottomSheetDialog.enterTransition
        }
        binding.homeCoinAmount.setOnClickListener {
            val bottomSheetDialog: BottomSheetDialogFragment = CoinWithdrawalFragment()
            bottomSheetDialog.show(requireActivity().supportFragmentManager, "Coin")
            bottomSheetDialog.enterTransition
        }

        binding.spinBtn.setOnClickListener {
            binding.spinBtn.isEnabled = false

            binding.spinBtn.visibility = View.GONE

            if (currentChance > 0) {
                val spin = java.util.Random().nextInt(6)
                val degrees = 50f * spin

                timer = object : CountDownTimer(
                    5000,
                    50
                ) {
                    var rotation = 0f

                    override fun onTick(millisUntilFinished: Long) {
                        rotation += 5f

                        if (rotation >= degrees) {
                            rotation = degrees
                            timer.cancel()
                            showResult(itemTitles[spin], spin)
                        }
                        binding.wheel.rotation = rotation
                    }

                    override fun onFinish() {}
                }.start()
            } else {
                Toast.makeText(requireContext(), "Out of Chance", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {

    }
}