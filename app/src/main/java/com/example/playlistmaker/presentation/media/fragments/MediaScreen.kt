package com.example.playlistmaker.presentation.media.fragments

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.PlayList
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.media.viewmodel.FavoriteFragmentViewModel
import com.example.playlistmaker.presentation.media.viewmodel.MediaFragmentViewModel
import com.example.playlistmaker.presentation.media.viewmodel.PlaylistFragmentViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaScreen(
    onAddPlayListClick: () -> Unit,
    onTrackClick: (Track) -> Unit,
    onPlayListClick: (PlayList) -> Unit,
    favoriteFragmentViewModel: FavoriteFragmentViewModel,
    playlistFragmentViewModel: PlaylistFragmentViewModel,
    mediaFragmentViewModel: MediaFragmentViewModel
) {
    val trackList = favoriteFragmentViewModel.favoriteTracks.observeAsState()
    val playListList = playlistFragmentViewModel.getLiveData.observeAsState()

    val scope = rememberCoroutineScope()

    val selectedTabIndex = mediaFragmentViewModel.currentTabPosition.observeAsState(0)
    val pagerState = rememberPagerState(
        pageCount = { 2 },
        initialPage = selectedTabIndex.value
    )

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
                selectedTabIndex = selectedTabIndex.value,
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.background,
                indicator = { tabPos ->
                    TabRowDefaults.PrimaryIndicator(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPos[selectedTabIndex.value]),
                        color = MaterialTheme.colorScheme.onSurface,
                        height = 2.dp,
                        width = 148.dp
                    )
                }
            ) {
                Tab(
                    selected = selectedTabIndex.value == 0,
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.outline,
                    onClick = {
                        scope.launch {
                            mediaFragmentViewModel.setCurrentTabPosition(0)
                            pagerState.animateScrollToPage(0)
                        }
                    },
                    text = {
                        Text(
                            text = stringResource(R.string.tab1txt),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = FontFamily(
                                Font(
                                    R.font.ys_display_medium,
                                    weight = FontWeight.Normal
                                )
                            )
                        )
                    },
                )

                Tab(
                    selected = selectedTabIndex.value == 1,
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.outline,
                    onClick = {
                        scope.launch {
                            mediaFragmentViewModel.setCurrentTabPosition(1)

                            pagerState.animateScrollToPage(1)
                        }
                    },
                    text = {
                        Text(
                            text = stringResource(R.string.tab2txt),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = FontFamily(
                                Font(
                                    R.font.ys_display_medium,
                                    weight = FontWeight.Normal
                                )
                            )
                        )
                    },
                )
            }

            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false,
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



