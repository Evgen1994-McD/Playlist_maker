package com.example.playlistmaker.ui.settings.fragments

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R

@Composable
fun SettingsScreen(){

Column(
    modifier = Modifier
        .fillMaxSize()
) {
    Row {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .height(61.dp)
                .padding(vertical = 21.dp)
                .padding(start = 16.dp),
            text = stringResource(R.string.dark_theme),
            fontSize = 16.sp,
            fontFamily = FontFamily(
                Font(
                    R.font.ys_display_regular,
                    weight = FontWeight.Normal
                )
            )
        )
    }


}



}








@Preview(showSystemUi = true)
@Composable
fun SettingsPreview(){
    SettingsScreen()
}