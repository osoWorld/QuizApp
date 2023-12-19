package com.example.mindmoneychallenge.Quiz

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.mindmoneychallenge.DataAndModelClass.EnglishQuestion
import com.example.mindmoneychallenge.DataAndModelClass.UserSignup
import com.example.mindmoneychallenge.R
import com.example.mindmoneychallenge.Result.ResultsActivity
import com.example.mindmoneychallenge.Withdrawal.CoinWithdrawalFragment
import com.example.mindmoneychallenge.databinding.ActivityQuizBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class QuizActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityQuizBinding.inflate(layoutInflater)
    }
    private lateinit var englishQuestionList: ArrayList<EnglishQuestion>
    private var currentEnglishQuestion: Int = 0
    private var score: Int = 0
    private var doc: String? = null

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val progressDialogue = ProgressDialog(this)
        progressDialogue.setMessage("Loading Questions")
        progressDialogue.show()

        //To refresh Button Background
        binding.option1.background = resources.getDrawable(R.drawable.options_btn_bg)
        binding.option2.background = resources.getDrawable(R.drawable.options_btn_bg)
        binding.option3.background = resources.getDrawable(R.drawable.options_btn_bg)
        binding.option4.background = resources.getDrawable(R.drawable.options_btn_bg)

        Firebase.database.reference.child("Users").child(Firebase.auth.currentUser!!.uid)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val user = snapshot.getValue<UserSignup>()
                        binding.userNameHome.text = user?.name
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
                            val currentCoin = snapshot.value as Long
                            binding.homeCoinAmount.text = currentCoin.toString()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                }
            )

        englishQuestionList = ArrayList<EnglishQuestion>()

        val image = intent.getIntExtra("categoryImg", 0)
        val catText = intent.getStringExtra("questionType")

        if (catText.equals("English")) {
            doc = "question1"
        } else if (catText.equals("History")) {
            doc = "Question"
        } else if (catText.equals("Mathematics")) {
            doc = "Questions"
        } else if (catText.equals("Science")) {
            doc = "question"
        }

        Firebase.firestore.collection("Questions").document(catText.toString())
            .collection(doc!!).get().addOnSuccessListener { questionData ->

                englishQuestionList.clear()

                for (data in questionData.documents) {
                    val englishQuestion: EnglishQuestion? =
                        data.toObject(EnglishQuestion::class.java)
                    englishQuestion?.let { englishQuestionList.add(it) }
                }
                if (englishQuestionList.isNotEmpty()) {
                    progressDialogue.dismiss()
                    binding.question.text = englishQuestionList[currentEnglishQuestion].question
                    binding.option1.text = englishQuestionList[currentEnglishQuestion].option1
                    binding.option2.text = englishQuestionList[currentEnglishQuestion].option2
                    binding.option3.text = englishQuestionList[currentEnglishQuestion].option3
                    binding.option4.text = englishQuestionList[currentEnglishQuestion].option4
                } else {
                    Toast.makeText(this, "No Question to show", Toast.LENGTH_SHORT).show()
                }
            }

        binding.scienceImg.setImageResource(image)

        binding.homeCoin.setOnClickListener {
            val bottomSheetDialog: BottomSheetDialogFragment = CoinWithdrawalFragment()
            bottomSheetDialog.show(this.supportFragmentManager, "Coin")
            bottomSheetDialog.enterTransition
        }
        binding.homeCoinAmount.setOnClickListener {
            val bottomSheetDialog: BottomSheetDialogFragment = CoinWithdrawalFragment()
            bottomSheetDialog.show(this.supportFragmentManager, "Coin")
            bottomSheetDialog.enterTransition
        }

        binding.option1.setOnClickListener {
            binding.option1.background = resources.getDrawable(R.drawable.select_opt_bg)
            binding.option2.background = resources.getDrawable(R.drawable.options_btn_bg)
            binding.option3.background = resources.getDrawable(R.drawable.options_btn_bg)
            binding.option4.background = resources.getDrawable(R.drawable.options_btn_bg)
        }
        binding.option2.setOnClickListener {
            binding.option1.background = resources.getDrawable(R.drawable.options_btn_bg)
            binding.option2.background = resources.getDrawable(R.drawable.select_opt_bg)
            binding.option3.background = resources.getDrawable(R.drawable.options_btn_bg)
            binding.option4.background = resources.getDrawable(R.drawable.options_btn_bg)
        }
        binding.option3.setOnClickListener {
            binding.option1.background = resources.getDrawable(R.drawable.options_btn_bg)
            binding.option2.background = resources.getDrawable(R.drawable.options_btn_bg)
            binding.option3.background = resources.getDrawable(R.drawable.select_opt_bg)
            binding.option4.background = resources.getDrawable(R.drawable.options_btn_bg)
        }
        binding.option4.setOnClickListener {
            binding.option1.background = resources.getDrawable(R.drawable.options_btn_bg)
            binding.option2.background = resources.getDrawable(R.drawable.options_btn_bg)
            binding.option3.background = resources.getDrawable(R.drawable.options_btn_bg)
            binding.option4.background = resources.getDrawable(R.drawable.select_opt_bg)
        }
        binding.nextButton.setOnClickListener {
            if (binding.option1.background.constantState!! == resources.getDrawable(R.drawable.select_opt_bg).constantState) {
                nextQuestionAndScoreUpdate(binding.option1.text.toString())
            } else if (binding.option2.background.constantState == resources.getDrawable(R.drawable.select_opt_bg).constantState) {
                nextQuestionAndScoreUpdate(binding.option2.text.toString())
            } else if (binding.option3.background.constantState == resources.getDrawable(R.drawable.select_opt_bg).constantState) {
                nextQuestionAndScoreUpdate(binding.option3.text.toString())
            } else if (binding.option4.background.constantState == resources.getDrawable(R.drawable.select_opt_bg).constantState) {
                nextQuestionAndScoreUpdate(binding.option4.text.toString())
            } else {
                //DO NOTHING
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun nextQuestionAndScoreUpdate(buttonTxt: String) {

        if (buttonTxt == englishQuestionList[currentEnglishQuestion].answer) {
            score += 10
        }

        val percentage = (score / englishQuestionList.size * 10) * 100
        currentEnglishQuestion++
        if (currentEnglishQuestion < englishQuestionList.size) {
            binding.question.text = englishQuestionList[currentEnglishQuestion].question
            binding.option1.text = englishQuestionList[currentEnglishQuestion].option1
            binding.option2.text = englishQuestionList[currentEnglishQuestion].option2
            binding.option3.text = englishQuestionList[currentEnglishQuestion].option3
            binding.option4.text = englishQuestionList[currentEnglishQuestion].option4

            //To refresh Button Background
            binding.option1.background = resources.getDrawable(R.drawable.options_btn_bg)
            binding.option2.background = resources.getDrawable(R.drawable.options_btn_bg)
            binding.option3.background = resources.getDrawable(R.drawable.options_btn_bg)
            binding.option4.background = resources.getDrawable(R.drawable.options_btn_bg)
        } else {
            Toast.makeText(this, "All Questions Completed", Toast.LENGTH_SHORT).show()
            startActivity(
                Intent(this, ResultsActivity::class.java)
                    .putExtra("total", percentage)
            )
        }
    }
}