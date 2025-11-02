package com.example.playlistmaker.presentation.settings.fragments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.settings.viewModel.SettingsViewModel

@Composable
fun SettingsScreen(

    viewModel: SettingsViewModel,

){
val themeMode = viewModel.getLiveData.observeAsState()
Column(
    modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(61.dp)
            .padding(vertical = 21.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier
                .padding(start = 16.dp),
            color = MaterialTheme.colorScheme.onSurface
            ,

            text = stringResource(R.string.dark_theme),
            fontSize = 16.sp,
            fontFamily = FontFamily(
                Font(
                    R.font.ys_display_regular,
                    weight = FontWeight.Normal
                )
            )
        )
        Switch(checked = themeMode.value == true,
            onCheckedChange = { isChecked ->
                val theme = if (isChecked) true else false
                viewModel.controlTHemeBySwitcher(theme)
            },
            modifier = Modifier
                .padding(end = 6.dp),
            colors = SwitchDefaults.colors(
                uncheckedThumbColor = colorResource(R.color.yp_grey),
                checkedThumbColor = colorResource(R.color.thumbTint_on),
                checkedTrackColor = colorResource(R.color.trackTint_on),
                uncheckedTrackColor = colorResource(R.color.trackTint_off),
                checkedBorderColor = colorResource(R.color.trackTint_on),
                uncheckedBorderColor = colorResource(R.color.trackTint_off),


                )
        )

    }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(61.dp)
            .padding(vertical = 21.dp)
            .padding(end = 12.dp)
            .padding(start = 16.dp),

        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.onSurface,

            text = stringResource(R.string.share_name),
            fontSize = 16.sp,
            fontFamily = FontFamily(
                Font(
                    R.font.ys_display_regular,
                    weight = FontWeight.Normal
                )
            )
        )
        Icon(
            tint = MaterialTheme.colorScheme.onSurface,

            painter = painterResource(R.drawable.ic_share_24),
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
        )


    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(61.dp)
            .padding(vertical = 21.dp)
            .padding(end = 12.dp)
            .padding(start = 16.dp),

        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            color = MaterialTheme.colorScheme.onSurface,

            modifier = Modifier,
            text = stringResource(R.string.support_name),
            fontSize = 16.sp,
            fontFamily = FontFamily(
                Font(
                    R.font.ys_display_regular,
                    weight = FontWeight.Normal
                )
            )
        )
        Icon(
            tint = MaterialTheme.colorScheme.onSurface,

            painter = painterResource(R.drawable.ic_support_24),
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
        )


    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(61.dp)
            .padding(vertical = 21.dp)
            .padding(end = 12.dp)
            .padding(start = 16.dp),

        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            color = MaterialTheme.colorScheme.onSurface,

            modifier = Modifier,
            text = stringResource(R.string.user_assets_name),
            fontSize = 16.sp,
            fontFamily = FontFamily(
                Font(
                    R.font.ys_display_regular,
                    weight = FontWeight.Normal
                )
            )
        )
        Icon(
            tint = MaterialTheme.colorScheme.onSurface,

            painter = painterResource(R.drawable.ic_userasset_24),
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
        )


    }








}

}







//
//@Preview(showSystemUi = true)
//@Composable
//fun SettingsPreview(){
//    SettingsScreen(false
//    )
//}