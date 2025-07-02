package com.example.playlistmaker.utils

import android.os.Build
import androidx.fragment.app.Fragment
import com.example.playlistmaker.domain.models.Track

fun Fragment.getTrackFromArguments(): Track? {
    return arguments.let { bundle ->
        if (Build.VERSION.SDK_INT >= 33) {
            bundle?.getSerializable("track", Track::class.java)

        } else {
            bundle?.getSerializable("track") as Track
        }

        /*
        Экстеншен функция для получения трека на фрагменте из бандл
         */


    }
}