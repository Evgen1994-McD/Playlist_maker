package com.example.playlistmaker.presentation.media.fragments

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.PlayList
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.media.viewmodel.FavoriteFragmentViewModel
import com.example.playlistmaker.presentation.media.viewmodel.PlaylistFragmentViewModel
import com.example.playlistmaker.presentation.search.fragment.TrackItem
import com.example.playlistmaker.utils.declineNoun
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaScreen(
    onAddPlayListClick: () -> Unit,
    onTrackClick: (Track) -> Unit,
    onPlayListClick: (PlayList) -> Unit,
    favoriteFragmentViewModel: FavoriteFragmentViewModel,
    playlistFragmentViewModel: PlaylistFragmentViewModel
) {

    val trackList = favoriteFragmentViewModel.favoriteTracks.observeAsState()
    val playListList = playlistFragmentViewModel.getLiveData.observeAsState()

    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 2 })
    // Используем pagerState.currentPage напрямую, так как это уже State<Int>
    val selectedTabIndex = pagerState.currentPage

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.media_name), // или ваш ресурс для заголовка
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

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.background,
                indicator = {tabPos ->
                    TabRowDefaults.PrimaryIndicator(
                        modifier = Modifier
                        .tabIndicatorOffset(tabPos[selectedTabIndex]),
                        color = MaterialTheme.colorScheme.onSurface,
                        height = 2.dp,
                        width = 148.dp
                    )
                }
            ) {
                Tab(
                    selected = selectedTabIndex == 0,
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.outline,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(0)
                        }
                    },
                    text = { Text(text = stringResource(R.string.tab1txt),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = FontFamily(Font(
                            R.font.ys_display_medium,
                            weight = FontWeight.Normal
                        ))
                        ) },
                )

                Tab(
                    selected = selectedTabIndex == 1,
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.outline,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    },
                    text = { Text(text = stringResource(R.string.tab2txt),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = FontFamily(Font(
                            R.font.ys_display_medium,
                            weight = FontWeight.Normal
                        ))
                        ) },
                )
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { page ->
                when (page) {
                    0 ->
                        FavoriteFragmentScreen(
                            trackList = trackList.value ?: emptyList<Track>(),
                            onTrackClick
                        )


                    1 ->
                        PlaylistFragmentScreen(
                            playListList.value ?: emptyList(),
                            onPlayListClick,
                            onAddPlayListClick
                        )

                }
            }
        }
    }
}

@Composable
fun FavoriteFragmentScreen(
    trackList: List<Track>,
    onTrackClick: (Track) -> Unit
) {
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
}

@Composable
fun PlaylistFragmentScreen(
    playListList: List<PlayList>,
    onPlayListClick: (PlayList) -> Unit,
    onAddPlayListClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {

        Box(
            modifier = Modifier
                .padding(top = 24.dp)
                .size(
                    width = 133.dp,
                    height = 36.dp
                )
                .background(
                    color = MaterialTheme.colorScheme.onSurface,
                    shape = RoundedCornerShape(54.dp)
                )

                .clickable(
                    onClick = { onAddPlayListClick() },
                    indication = null, // Без визуального эффекта
                    interactionSource = remember { MutableInteractionSource() }
                ),
            contentAlignment = Alignment.Center,

            )
        {
            Text(
                text = stringResource(R.string.new_playlist),
                color = MaterialTheme.colorScheme.background,
                fontSize = 14.sp,
                fontFamily = FontFamily(
                    Font(
                        R.font.ys_display_medium,
                        weight = FontWeight.Normal
                    )
                )
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
                .padding(horizontal = 8.dp),


            ) {
            items(
                playListList.size,
                key = { index -> "playList$index" },
                contentType = { "playList" }) { index ->
                val playlist = playListList[index]
                if (playlist != null) {
                    PlayListItem(
                        playlist,
                        onPlayListClick
                    )
                }

            }


        }
    }
}


@Composable
fun PlayListItem(
    playList: PlayList,
    onPlayListClick: (PlayList) -> Unit,
) {
    val oneForm = stringResource(R.string.track1)
    val twoForm = stringResource(R.string.track3)
    val fiveAndMoreForm = stringResource(R.string.track2)

    val playListSize = declineNoun(playList.size, oneForm, twoForm, fiveAndMoreForm)

    val context = LocalContext.current

    // Проверяем, есть ли изображение
    val hasImage = !playList.image.isNullOrEmpty()

    // Создаем ImageRequest только если есть изображение
    val imageRequest = remember(playList.image) {
        if (hasImage) {
            ImageRequest.Builder(context)
                .data(playList.image)
                .memoryCacheKey(playList.image)
                .diskCacheKey(playList.image)
                .crossfade(true)
                .placeholder(R.drawable.ph_media_312) // Добавляем placeholder в ImageRequest
                .error(R.drawable.ph_media_312) // Добавляем error в ImageRequest
                .build()
        } else {
            null
        }
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .width(160.dp)
            .clickable(
                onClick = { onPlayListClick(playList) },
                indication = null,
                interactionSource = remember { MutableInteractionSource() })
    ) {
        // Если нет изображения, показываем плейсхолдер напрямую
        if (!hasImage || imageRequest == null) {
            Box(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .size(160.dp)
                    .clip(RoundedCornerShape(8.dp))

                ,
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.foundation.Image(
                    painter = painterResource(id = R.drawable.ph_media_312),
                    contentDescription = null,
                    modifier = Modifier.size(160.dp),
                    contentScale = ContentScale.Fit
                )
            }
        } else {
            SubcomposeAsyncImage(
                model = imageRequest,
                contentDescription = null,
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .size(160.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                        ,
                        contentAlignment = Alignment.Center
                    ) {
                        androidx.compose.foundation.Image(
                            painter = painterResource(id = R.drawable.ph_media_312),
                            contentDescription = null,
                            modifier = Modifier.size(160.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                },
                error = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                        ,
                        contentAlignment = Alignment.Center
                    ) {
                        androidx.compose.foundation.Image(
                            painter = painterResource(id = R.drawable.ph_media_312),
                            contentDescription = "Playlist placeholder",
                            modifier = Modifier.size(160.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            )
        }

        Box(
            modifier = Modifier
                .width(160.dp),
            contentAlignment = Alignment.CenterStart

        ) {
            Text(
                text = playList.name,
                fontFamily = FontFamily(
                    Font(
                        R.font.ys_display_regular,
                        weight = FontWeight.Normal
                    )
                ),
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis

            )
        }

        Box(
            modifier = Modifier
                .width(160.dp),
            contentAlignment = Alignment.CenterStart

        ) {
            Text(
                text = playListSize,
                fontFamily = FontFamily(
                    Font(
                        R.font.ys_display_regular,
                        weight = FontWeight.Normal
                    )
                ),
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis

            )


        }


    }

}
