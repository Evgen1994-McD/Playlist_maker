package com.example.playlistmaker.utils



    fun declineNoun(count: Int, oneForm: String, twoToFourForm: String, fiveAndMoreForm: String): String {
        val lastDigit = count % 10
        val lastTwoDigits = count % 100
        return when {
            lastTwoDigits !in 11..14 && lastDigit == 1 -> "$count $oneForm"
            lastTwoDigits !in 11..14 && lastDigit in 2..4 -> "$count $twoToFourForm"
            else -> "$count $fiveAndMoreForm"
        }
    }

