package com.example.playlistmaker.utils

import android.app.Dialog
import android.content.Context
import android.view.ContextThemeWrapper
import android.view.View
import com.example.playlistmaker.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object DialogManager {
    fun showDialog(
        context: Context,
        tId: String,
        mId: String,
        positiveId: Int,
        negativeId: Int,
        overlay: View?,
        listener: Listener
    ) {
        // Оборачиваем контекст с нашим кастомным стилем
        val styledContext = ContextThemeWrapper(context, R.style.MyDialogTheme)
        val builder = MaterialAlertDialogBuilder(styledContext)

        var dialog: Dialog? = null
        builder.setTitle(tId)
        builder.setMessage(mId)
        builder.setPositiveButton(positiveId) { _, _ ->
            listener.onClick()
            dialog?.dismiss()
        }
        builder.setNegativeButton(negativeId) { _, _ ->
            overlay?.visibility = View.GONE
            dialog?.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }

    interface Listener {
        fun onClick()
    }
}