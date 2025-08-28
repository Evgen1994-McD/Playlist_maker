package com.example.playlistmaker.utils

import android.app.Dialog
import android.content.Context
import android.view.ContextThemeWrapper
import com.example.playlistmaker.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object DialogManager {
    fun showDialog(
        context: Context,
        tId: Int,
        mId: Int,
        positiveId:Int,
        negativeId:Int,
        listener: Listener,
    ) {  // передаём контекст, mId - messageId ( это сообщение) - Так как ресурсы у нас это ИНТ!!!
        val builder = MaterialAlertDialogBuilder(context)
         // мы делаем Диалоговое окно при попытке сбросить. ПОзитив баттон - согласиться, негатив - отменить
        var dialog: Dialog? =
            null // типа инициализировали диалог, изначально он равен null, а ниже мы используем его
        builder.setTitle(tId)
        builder.setMessage(mId)
        builder.setPositiveButton(positiveId) { _, _ ->
            // суть - _,_ ->   - нижние подчёркивания используются для того, если мы не используем переданные переменные. Тут передаются определенные значения. Если нам они не нужны, используем подчёркивания. А так это ОнКликЛистенер типа.
            listener.onClick()
            dialog?.dismiss()

        }
        builder.setNegativeButton(negativeId) { _, _ ->
            dialog?.dismiss()  // Просто отменяем диалог если не согласны
        }
        dialog = builder.create()
        dialog.show() // показываем диалог, иначе его не будет видно

    }
    interface Listener {
        fun onClick()  // мы создали Интерфейс с функцией Он клик, это будет наш кликер :D
    }

}