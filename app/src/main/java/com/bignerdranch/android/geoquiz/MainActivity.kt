package com.bignerdranch.android.geoquiz

import android.app.Activity
import android.content.ClipData.newIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders


private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val REQUEST_CODE_CHEAT = 0




class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var cheatButton: Button
    private lateinit var questionTextView: TextView


    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.previous_button)
        cheatButton = findViewById(R.id.cheat_button)
        questionTextView = findViewById(R.id.question_text_view)


        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
            cheatButton.isEnabled = false
            trueButton.isEnabled = true
            trueButton.isClickable = false
            falseButton.isEnabled = false
            falseButton.isClickable = false
        }
        falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
            cheatButton.isEnabled = false
            trueButton.isEnabled = false
            trueButton.isClickable = false
            falseButton.isEnabled = true
            falseButton.isClickable = false
        }
        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
            val answerCode = quizViewModel.checkAnswer()
            if (answerCode == 0){
                println("havent answer")
                cheatButton.isEnabled = true
                trueButton.isEnabled = true
                trueButton.isClickable = true
                falseButton.isEnabled = true
                falseButton.isClickable = true;
            }else if (answerCode == 1) {
                cheatButton.isEnabled = false
                println("wrong answer")
                val correctAnswer = quizViewModel.currentQuestionAnswer
                if (correctAnswer) {
                    trueButton.isEnabled = false
                    trueButton.isClickable = false
                    falseButton.isEnabled = true
                    falseButton.isClickable = false
                }
                else{
                    trueButton.isEnabled = true
                    trueButton.isClickable = false
                    falseButton.isEnabled = false
                    falseButton.isClickable = false
                }
                // display wrong message

            }else if (answerCode == 2) {
                cheatButton.isEnabled = false
                println("right answer")
                val correctAnswer = quizViewModel.currentQuestionAnswer
                if (correctAnswer) {
                    trueButton.isEnabled = true
                    trueButton.isClickable = false
                    falseButton.isEnabled = false
                    falseButton.isClickable = false
                }else {
                    trueButton.isEnabled = false
                    trueButton.isClickable = false
                    falseButton.isEnabled = true
                    falseButton.isClickable = false
                }
                // display right message

            }
        }
        prevButton.setOnClickListener {
            quizViewModel.moveToPrev()
            updateQuestion()
            val answerCode = quizViewModel.checkAnswer()
            if (answerCode == 0){
                println("havent answer")
                cheatButton.isEnabled = true
                trueButton.isEnabled = true
                trueButton.isClickable = true
                falseButton.isEnabled = true
                falseButton.isClickable = true;
            }else if (answerCode == 1) {
                cheatButton.isEnabled = false
                println("wrong answer")
                val correctAnswer = quizViewModel.currentQuestionAnswer
                if (correctAnswer) {
                    trueButton.isEnabled = false
                    trueButton.isClickable = false
                    falseButton.isEnabled = true
                    falseButton.isClickable = false
                }
                else{
                    trueButton.isEnabled = true
                    trueButton.isClickable = false
                    falseButton.isEnabled = false
                    falseButton.isClickable = false
                }
                // disney wrong message

            }else if (answerCode == 2) {
                cheatButton.isEnabled = false
                println("right answer")
                val correctAnswer = quizViewModel.currentQuestionAnswer
                if (correctAnswer) {
                    trueButton.isEnabled = true
                    trueButton.isClickable = false
                    falseButton.isEnabled = false
                    falseButton.isClickable = false
                }else {
                    trueButton.isEnabled = false
                    trueButton.isClickable = false
                    falseButton.isEnabled = true
                    falseButton.isClickable = false
                }
                // display right message
            }
        }
        cheatButton.setOnClickListener {
            // Start CheatActivity
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)


        }
        updateQuestion()

    }
    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int,
                                  data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            quizViewModel.isCheater =
                data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }
    



    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }
    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }


    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }
        if (userAnswer == correctAnswer){
            quizViewModel.updateAnswer(2)
        }else{
            quizViewModel.updateAnswer(1)
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
            .show()
    }

}