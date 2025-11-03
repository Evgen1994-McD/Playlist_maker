package com.example.playlistmaker.ui

import android.os.Build
import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.playlistmaker.presentation.settings.viewModel.SettingsViewModel

private val DarkColorScheme = darkColorScheme(

    background = Color(0xFF1A1B22),
    onSurface = Color(0xFFE6E1E5)
)

private val LightColorScheme = lightColorScheme(
    background = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1A1B22)
)


@Composable
fun PlaylistMakerTheme(
    viewModel: SettingsViewModel,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {

    val context = LocalContext.current


    val themeMode = viewModel.getLiveData.observeAsState()
    Log.d("theme", "theme in mode $themeMode")





    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (themeMode.value==true) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        themeMode.value==true -> DarkColorScheme
        else -> LightColorScheme
    }


    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}