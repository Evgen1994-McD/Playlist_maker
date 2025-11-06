package com.example.playlistmaker.presentation.media.fragments

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.search.fragment.TrackItem
import kotlinx.collections.immutable.ImmutableList

@Composable
fun FavoriteFragmentScreen(
    trackList: ImmutableList<Track>,
    onTrackClick: (Track) -> Unit
) {
    if (trackList.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp),


            ) {
            items(trackList) { track ->
                TrackItem(
                    track,
                    onTrackClick
                )

            }


        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.ph_nothing_to_show_120), null,
                modifier = Modifier
                    .padding(
                        top = 106.dp,
                        bottom = 16.dp
                    )
                    .size(120.dp)

            )

            Text(
                text = stringResource(R.string.no_media),
                fontSize = 19.sp,
                fontFamily = FontFamily(
                    Font(
                        R.font.ys_display_medium,
                        weight = FontWeight.Medium,
                    )
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

