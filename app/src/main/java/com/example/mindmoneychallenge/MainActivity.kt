package com.example.mindmoneychallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.mindmoneychallenge.MainFragments.HistoryFragment
import com.example.mindmoneychallenge.MainFragments.HomeFragment
import com.example.mindmoneychallenge.MainFragments.ProfileFragment
import com.example.mindmoneychallenge.MainFragments.SpinFragment
import com.example.mindmoneychallenge.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeFragment())

        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home_nav -> replaceFragment(HomeFragment())
                R.id.spin_nav -> replaceFragment(SpinFragment())
                R.id.history_nav -> replaceFragment(HistoryFragment())
                R.id.profile_nav -> replaceFragment(ProfileFragment())

                else -> {}
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout,fragment)
            .commit()
    }
}