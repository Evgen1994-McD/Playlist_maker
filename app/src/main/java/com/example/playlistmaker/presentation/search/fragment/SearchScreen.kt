package com.example.playlistmaker.presentation.search.fragment

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.search.viewModel.SearchScreenState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import com.example.playlistmaker.presentation.search.viewModel.SearchViewModel
import com.example.playlistmaker.ui.Black_1A1B22
import com.example.playlistmaker.ui.Blue_3772E7

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onSearchTextChanged: (String)-> Unit,
    onRetryClick:(String)-> Unit,
    loadSearchHistory:()-> Unit,
    onTrackClick: (Track) -> Unit
) {
    // Получаем сохраненный поисковый запрос из ViewModel
    val savedQuery = viewModel.currentSearchQuery.observeAsState(initial = "")
    
    // Используем сохраненный запрос как начальное значение
    var text by remember { mutableStateOf(savedQuery.value) }
    
    // Синхронизируем локальное состояние с состоянием ViewModel при возврате на экран
    // Обновляем только если локальное значение пустое, а сохраненное - нет (сигнал возврата)
    LaunchedEffect(Unit) {
        // Синхронизируем при первом монтировании композиции
        if (text.isEmpty() && savedQuery.value.isNotEmpty()) {
            text = savedQuery.value
        }
    }
    
    // Также отслеживаем изменения savedQuery (например, при восстановлении из savedInstanceState)
    LaunchedEffect(savedQuery.value) {
        if (text.isEmpty() && savedQuery.value.isNotEmpty()) {
            text = savedQuery.value
        }
    }
    
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()
    val state = viewModel.getLiveData.observeAsState()
    val focusManager = LocalFocusManager.current // Добавить
    val keyboardController = LocalSoftwareKeyboardController.current // Добавить



    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.search_toolbar), // или ваш ресурс для заголовка
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
                .padding(paddingValues)
                .fillMaxWidth()
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            BasicTextField(
                value = text,
                onValueChange = { newText -> 
                    text = newText
                    // Обновляем запрос в ViewModel
                    viewModel.updateSearchQuery(newText)
                    onSearchTextChanged(newText)
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceTint,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .height(36.dp)
                    .focusRequester(focusRequester)
                    .onFocusChanged { focus ->
                        if (focus.isFocused && text.isEmpty()) {
                            loadSearchHistory()
                        }
                        if(focus.isFocused){
                            keyboardController?.show()
                        }
                    },
                decorationBox = { innerTextField ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_hintsearch_16),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(start = 13.dp)
                                .width(18.dp)
                                .height(16.dp),
                            tint = MaterialTheme.colorScheme.surface
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp, end = 8.dp)
                        ) {
                            if (text.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.search),
                                    color = MaterialTheme.colorScheme.surface,
                                    modifier = Modifier
                                )
                            }
                            innerTextField()
                        }
                        if (text.isNotEmpty()) {
                            Icon(
                                painter = painterResource(R.drawable.ic_clear_16),
                                contentDescription = "Clear",
                                modifier = Modifier
                                    .padding(end = 13.dp)
                                    .size(16.dp)
                                    .clickable(
                                        onClick = {
                                            text = ""
                                            viewModel.updateSearchQuery("")
                                            onSearchTextChanged("")
                                            focusManager.clearFocus() // Убрать фокус
                                            keyboardController?.hide() // Скрыть клавиатуру
                                        },
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ),
                                tint = MaterialTheme.colorScheme.surface
                            )
                        }
                    }
                },
                textStyle = TextStyle(
                    color = Black_1A1B22,
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(
                        R.font.ys_display_regular
                    ))
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                singleLine = true
            )

            when (state.value) {
                is SearchScreenState.Loading ->if (text.isNotEmpty()){DisplayProgressBar()}
                is SearchScreenState.SearchResults -> if (text.isNotEmpty()){
                    DisplayTracks(
                        ((state.value) as SearchScreenState.SearchResults).data,
                        onTrackClick = onTrackClick
                    )
                }
                is SearchScreenState.History -> if (text.isEmpty()){
                    DisplayTracks(
                        ((state.value) as SearchScreenState.History).history,
                        onTrackClick = onTrackClick)
                    }
                is SearchScreenState.ErrorNotFound -> if (text.isNotEmpty()){DisplayPhNotFound()}
                is SearchScreenState.ErrorNoEnternet ->if (text.isNotEmpty()) {
                    DisplayPhNotEnternet(
                        onRetryClick = onRetryClick,
                        searchText = text,
                    )
                }
                else -> null
            }


        }


    }


}

@Composable
fun DisplayProgressBar() {
    CircularProgressIndicator(
        modifier = Modifier
            .padding(top = 140.dp)
            .size(44.dp),
        color = Blue_3772E7
    )
}


@Composable
fun DisplayTracks(trackList: List<Track>,
                  onTrackClick:(Track)->Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),


    ) {
        items(trackList) { track ->
            TrackItem(track,
                onTrackClick)

        }


    }
}

@Composable
fun DisplayPhNotFound() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(
                R.drawable.ph_nothing_to_show_120

            ), null,
            modifier = Modifier
                .padding(
                    top = 102.dp,
                    bottom = 16.dp
                )
                .size(120.dp)
        )
        Text(
            text = stringResource(R.string.msg_nothing_to_show),
            modifier = Modifier,
            fontSize = 19.sp,
            fontFamily = FontFamily(
                Font(
                    R.font.ys_display_medium,
                    weight = FontWeight.Normal
                )
            ),
            color = MaterialTheme.colorScheme.onSurface
        )

    }
}


@Composable
fun DisplayPhNotEnternet(searchText: String,
                         onRetryClick: (String) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(
                R.drawable.ph_no_internet_120

            ), null,
            modifier = Modifier
                .padding(
                    top = 102.dp,
                    bottom = 16.dp
                )
                .size(120.dp)
        )
        Text(
            text = stringResource(R.string.msg_no_internet_top),
            modifier = Modifier,
            fontSize = 19.sp,
            fontFamily = FontFamily(
                Font(
                    R.font.ys_display_medium,
                    weight = FontWeight.Normal
                )
            ),
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = stringResource(R.string.msg_no_internet_bottom),
            modifier = Modifier
                .padding(top = 28.dp,
                    bottom = 24.dp),
            fontSize = 19.sp,
            fontFamily = FontFamily(
                Font(
                    R.font.ys_display_medium,
                    weight = FontWeight.Normal
                )
            ),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

            Box(modifier = Modifier
                .size(width = 91.dp,
                    height = 36.dp)
                .background(color = MaterialTheme.colorScheme.onSurface,
                    shape = RoundedCornerShape(54.dp))

                .clickable(onClick = { onRetryClick(searchText) },
                    indication = null, // Без визуального эффекта
                    interactionSource = remember { MutableInteractionSource() }
                )

                ,
                contentAlignment = Alignment.Center,

            ){
                Text(text = stringResource(R.string.txt_nointernet_button),
                    color = MaterialTheme.colorScheme.background,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(
                        Font(
                            R.font.ys_display_medium,
                            weight = FontWeight.Normal
                        )
                    ))
            }


    }
}


