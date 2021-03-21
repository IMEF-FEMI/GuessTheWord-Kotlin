package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    companion object {
        //        Time when the game is over
        private const val DONE = 0L

        //        countdown time interval
        private const val ONE_SECOND = 1000L

        private const val COUNTDOWN_TIME = 60000L
    }


    //countdown time
    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() = _currentTime
    //    String version of the current time
    val currentTimeString = Transformations.map(currentTime){
        time-> DateUtils.formatElapsedTime(time)
    }

    private val timer: CountDownTimer


    //current word
    private val _word = MutableLiveData<String>()
    val word: LiveData<String> get() = _word

    //current score
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    //   end game event trigger
    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean> get() = _eventGameFinish

    private lateinit var wordList: MutableList<String>
    private fun resetList() {
        wordList = mutableListOf(

                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()

    }

    init {

        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = millisUntilFinished / ONE_SECOND
            }

            override fun onFinish() {
                _currentTime.value = DONE
                onGameFinish()
            }

        }
        timer.start()
        _word.value = ""
        _score.value = 0

        resetList()
        nextWord()
        Log.i("GameViewModel", "GameViewModel Created")
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        if (wordList.isEmpty()) {
            resetList()
        } else {
            _word.value = wordList.removeAt(0)
        }

    }

    /** Methods for buttons presses **/
    fun onSkip() {
        _score.value = (score.value)?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = (score.value)?.plus(1)
        nextWord()
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }

    fun onGameFinish() {
        _eventGameFinish.value = true
    }

    fun onGameFinishComplete() {
        _eventGameFinish.value = false
    }


}