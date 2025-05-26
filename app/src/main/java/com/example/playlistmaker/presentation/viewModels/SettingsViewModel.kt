package com.example.playlistmaker.presentation.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.OpenUrlUseCase
import com.example.playlistmaker.domain.api.SendSuppEmailUseCase
import com.example.playlistmaker.domain.api.ShareAppUseCase

class SettingsViewModel(
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
        private val shareAppUseCase: ShareAppUseCase,
        private val sendToSuppUse : SendSuppEmailUseCase,
        private val openUriUseCase : OpenUrlUseCase

    ) : ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return when {
                modelClass.isAssignableFrom(SettingsViewModel::class.java) -> SettingsViewModel(
                    shareAppUseCase,
                    sendToSuppUse,
                    openUriUseCase
                ) as T

                else -> throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }


}




