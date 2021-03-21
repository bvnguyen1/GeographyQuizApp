package com.bignerdranch.android.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"
class QuizViewModel : ViewModel() {

    var currentIndex = 0
    var isCheater = false
    private val questionBank = listOf(
        Question(R.string.question_colosseum, true),
        Question(R.string.question_coffee, false),
        Question(R.string.question_continent, false),
        Question(R.string.question_vaticancity, true),
        Question(R.string.question_nile, false),
        Question(R.string.question_venezuela, true),
        Question(R.string.question_frozen, false),
        Question(R.string.question_greatwall, true),
        Question(R.string.question_pyramid, true),
        Question(R.string.question_us, false))

    val answerArray = IntArray(questionBank.size)

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer
    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId
    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }
    fun moveToPrev() {
        currentIndex -= 1
        if (currentIndex < 0)
            currentIndex = questionBank.size - 1
    }

    fun checkAnswer(): Int {
        return answerArray[currentIndex]
    }
    fun updateAnswer(userAnswer: Int){
        answerArray[currentIndex] = userAnswer
    }



}