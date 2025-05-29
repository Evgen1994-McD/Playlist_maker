package com.example.playlistmaker.ui.settings.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.settings.OpenUrlUseCase
import com.example.playlistmaker.domain.settings.SendSuppEmailUseCase
import com.example.playlistmaker.domain.settings.ShareAppUseCase
import com.example.playlistmaker.domain.settings.SwitchThemeUseCase
import com.example.playlistmaker.ui.search.viewModel.SearchScreenState

class SettingsViewModel(
    private val switchThemeUseCase: SwitchThemeUseCase,
    private val shareAppUseCase: ShareAppUseCase,
    private val sendToSuppUse : SendSuppEmailUseCase,
    private val openUriUseCase : OpenUrlUseCase
): ViewModel() {
companion object{



}

    fun shareApp(context: Context) {  // Метод - интент для отправки сообщений
        shareAppUseCase.shareApp(context)

    }


     fun openUrlInDefaultBrowser(context: Context) {
         val url = context.getString(R.string.Url_userasset)
        openUriUseCase.openUrlInDefaultBrowser(context, url)
    }


     fun sendSuppEmail(context: Context) {  // Приватный метод для письма в поддержку
        val myEmail = context.getString(R.string.address)
        val subject = context.getString(R.string.subject)
        val body = context.getString(R.string.body)

        sendToSuppUse.sendSuppEmail(context, myEmail, subject, body)
    }




    class CustomViewModelSettingsFactory(
        private val switchThemeUseCase: SwitchThemeUseCase,
        private val shareAppUseCase: ShareAppUseCase,
        private val sendToSuppUse : SendSuppEmailUseCase,
        private val openUriUseCase : OpenUrlUseCase

    ) : ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return when {
                modelClass.isAssignableFrom(SettingsViewModel::class.java) -> SettingsViewModel(
                    switchThemeUseCase,
                    shareAppUseCase,
                    sendToSuppUse,
                    openUriUseCase
                ) as T

                else -> throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }

    var currentThemeLiveData = MutableLiveData<Boolean>()


    init {

        currentThemeLiveData.value = switchThemeUseCase.controlThemeInOtherWindows()

    }

    val getLiveData: LiveData<Boolean> get() = currentThemeLiveData

    fun controlThemeInOtherWindows(){
        switchThemeUseCase.controlThemeInOtherWindows()
    }

    fun controlTHemeBySwitcher(theme : Boolean){
        switchThemeUseCase.switchThemeModeBySettings(theme)

    }

}




