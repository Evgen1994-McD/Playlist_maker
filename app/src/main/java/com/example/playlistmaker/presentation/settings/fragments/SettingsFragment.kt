package com.example.playlistmaker.presentation.settings.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.settings.viewModel.SettingsViewModel
import com.example.playlistmaker.presentation.theme.ThemeViewModel
import com.example.playlistmaker.ui.PlaylistMakerTheme
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by activityViewModel()
    private val themeViewModel: ThemeViewModel by activityViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            // Обязательно: стратегия уничтожения композиции
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnLifecycleDestroyed(lifecycleOwner = this@SettingsFragment))

            setContent {
                val themeMode = themeViewModel.themeMode.observeAsState()
                PlaylistMakerTheme(themeMode as State<Boolean>){

                    SettingsScreen(
                        viewModel,
                        themeViewModel,
                        onShareClick = {
                            shareApp()
                                       },
                        onSupportClick = {
                            sendSuppEmail()
                                         },
                        onUserAssetClick = {
                            openUrlInDefaultBrowser()
                                           },
                    )

                }
            }
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)





    }

    fun shareApp() {  // Метод - интент для отправки сообщений
        viewModel.shareApp()

    }

    private fun sendSuppEmail() {  // Приватный метод для письма в поддержку
        viewModel.sendSuppEmail()
    }

    private fun openUrlInDefaultBrowser() {
        viewModel.openUrlInDefaultBrowser()
    }

    override fun onDestroy() { // закрываем плеер при завершении работы
        super.onDestroy()
        themeViewModel.themeMode.removeObservers(this)

    }


}


