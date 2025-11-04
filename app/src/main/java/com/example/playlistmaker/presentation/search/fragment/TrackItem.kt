package com.example.playlistmaker.presentation.search.fragment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.PlaylistMakerTheme

@Composable
fun TrackItem(
    track: Track,
//    onClick:()-> Unit
){

    val context = LocalContext.current
    // Использовать remember для тяжелых вычислений
    val imageRequest = remember(track.artworkUrl100) {
        ImageRequest.Builder(context)
            .data(track.artworkUrl100)
            .memoryCacheKey(track.artworkUrl100)
            .diskCacheKey(track.artworkUrl100)
            .crossfade(true) // Плавная анимация
            .build()
    }

    Row(modifier = Modifier
        .fillMaxWidth()
        .height(61.dp)
        .background(color = MaterialTheme.colorScheme.background),
        verticalAlignment = Alignment.CenterVertically,

    )
    {



        SubcomposeAsyncImage(
model = imageRequest,
            contentDescription = track.trackName,
            modifier = Modifier
                .padding(start = 13.dp)
                .padding(vertical = 8.dp)
                .size(45.dp),
            contentScale = ContentScale.Crop,
            loading = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ph_media_312),
                        contentDescription = "Planet placeholder",
                        modifier = Modifier.size(45.dp),
                        tint = MaterialTheme.colorScheme.surfaceBright
                    )
                }
            },
            error = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ph_media_312),
                        contentDescription = "Planet placeholder",
                        modifier = Modifier.size(45.dp),
                        tint = MaterialTheme.colorScheme.surfaceBright

                    )
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(start = 8.dp),
            horizontalAlignment = Alignment.Start

        ) {
            Text(text = track.trackName,

                fontSize = 16.sp,

                fontFamily = FontFamily(Font(
                    R.font.ys_display_regular,
                    weight = FontWeight.Bold
                )),
              overflow = TextOverflow.Ellipsis,
                maxLines = 1,
color = MaterialTheme.colorScheme.onSurface
                )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = track.artistName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,

                    fontFamily = FontFamily(Font(
                        R.font.ys_display_regular,
                        weight = FontWeight.Normal
                    )),
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant


                )
                Icon(painter = painterResource(R.drawable.ic_dot_13), null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                Text(text = track.trackTimeMillis,
                    maxLines = 1,
                    fontFamily = FontFamily(Font(
                        R.font.ys_display_regular,
                        weight = FontWeight.Normal
                    )),
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }



        }

        Icon(painter = painterResource(R.drawable.ic_userasset_24), null,
            modifier = Modifier
                .padding(end = 12.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
                )



    }



}




@Preview(showSystemUi = false, showBackground = true)
@Composable
fun PreviewTrackItem(){
val testTrack = Track(
    "112211",
    "Fill Nawe dsadasd  asda sd aasd asd asd asd asd ",
    "Jason",
    "3:54",
    "",
    "",
    "2025",
    "Rock",
    "Usa",
    "21",
    false
)
    val testState = remember { mutableStateOf(true) }
    PlaylistMakerTheme(testState) {
        TrackItem(testTrack)

    }


}