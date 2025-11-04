package com.example.playlistmaker.presentation.settings.fragments

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
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
import com.example.playlistmaker.presentation.theme.ThemeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    themeViewModel: ThemeViewModel,
    onShareClick:()-> Unit,
    onSupportClick:()-> Unit,
    onUserAssetClick:()-> Unit
) {
    val themeMode = themeViewModel.themeMode.observeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.settings_name), // или ваш ресурс для заголовка
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 22.sp,
                        fontFamily = FontFamily(
                            Font(
                                R.font.ys_display_medium,
                                weight = FontWeight.Bold
                            )
                        )
                    )
                },

                colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
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
                        .padding(start = 16.dp)
                      ,
                    color = MaterialTheme.colorScheme.onSurface,

                    text = stringResource(R.string.dark_theme),
                    fontSize = 16.sp,
                    fontFamily = FontFamily(
                        Font(
                            R.font.ys_display_regular,
                            weight = FontWeight.Normal
                        )
                    )
                )
                Switch(
                    checked = themeMode.value == true,
                    onCheckedChange = { themeViewModel.updateTheme(it) },
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
                    .padding(start = 16.dp)
                    .clickable( onClick = {
                        onShareClick()},
                        indication = rememberRipple(), // Явно указываем Material3 ripple
                        interactionSource = remember { MutableInteractionSource() }
                    ),

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
                    .padding(start = 16.dp)
                    .clickable(onClick = {
                        onSupportClick()},
                        indication = rememberRipple(), // Явно указываем Material3 ripple
                        interactionSource = remember { MutableInteractionSource() }
                    ),

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
                    .padding(start = 16.dp)
                    .clickable(
                        onClick = { onUserAssetClick() },
                        indication = rememberRipple(), // Явно указываем Material3 ripple
                        interactionSource = remember { MutableInteractionSource() }
                    ),

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

}





//
//@Preview(showSystemUi = true)
//@Composable
//fun SettingsPreview(){
//    SettingsScreen(false
//    )
//}