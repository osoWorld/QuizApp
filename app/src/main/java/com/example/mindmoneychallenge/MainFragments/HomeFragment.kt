package com.example.mindmoneychallenge.MainFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mindmoneychallenge.AdapterClass.CategoryAdapter
import com.example.mindmoneychallenge.DataAndModelClass.Category
import com.example.mindmoneychallenge.DataAndModelClass.UserSignup
import com.example.mindmoneychallenge.R
import com.example.mindmoneychallenge.Withdrawal.CoinWithdrawalFragment
import com.example.mindmoneychallenge.databinding.FragmentHomeBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {
    private val binding: FragmentHomeBinding by lazy{
        FragmentHomeBinding.inflate(layoutInflater)
    }
    private var categoryList = ArrayList<Category>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryList.add(Category(R.drawable.englishs,"English"))
        categoryList.add(Category(R.drawable.scince,"Science"))
        categoryList.add(Category(R.drawable.historyimg,"History"))
        categoryList.add(Category(R.drawable.mathmetic,"Mathematics"))

        binding.categoryRecView.layoutManager = GridLayoutManager(requireContext(),2)
        val adapter = CategoryAdapter(categoryList)
        binding.categoryRecView.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
                            val currentCoin = snapshot.value as Long
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    companion object {

    }
}