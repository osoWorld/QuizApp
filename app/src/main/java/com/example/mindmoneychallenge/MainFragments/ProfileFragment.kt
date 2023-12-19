package com.example.mindmoneychallenge.MainFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mindmoneychallenge.DataAndModelClass.UserSignup
import com.example.mindmoneychallenge.R
import com.example.mindmoneychallenge.databinding.FragmentProfileBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {
    private val binding: FragmentProfileBinding by lazy {
        FragmentProfileBinding.inflate(layoutInflater)
    }

    private var isExpand = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding.profilePInfoCard.setOnClickListener {

            isExpand = !isExpand

            if (isExpand) {
                binding.profilePersonalConstraintLayout.visibility = View.VISIBLE
                binding.upDownArrow.setImageResource(R.drawable.downarrow)
            } else {
                binding.profilePersonalConstraintLayout.visibility = View.GONE
                binding.upDownArrow.setImageResource(R.drawable.arrowup)
            }
        }

        Firebase.database.reference.child("Users").child(Firebase.auth.currentUser!!.uid)
            .addListenerForSingleValueEvent(
                object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val user = snapshot.getValue<UserSignup>()
                        binding.profilePersonalName.text = user?.name
                        binding.profilePersonalEmail.text = user?.email
                        binding.profilePersonalPassword.text = user?.password
                        binding.profilePersonalAge.text = user?.age
                        binding.userNameProfile.text = user?.name
                    }

                    override fun onCancelled(error: DatabaseError) {
//                        TODO("Not yet implemented")
                    }

                }
            )
        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {

    }
}