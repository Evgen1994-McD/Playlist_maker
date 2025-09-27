package com.example.playlistmaker.ui.player.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import com.example.playlistmaker.R
import kotlin.math.min

@SuppressLint("AppCompatCustomView")
class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    @AttrRes defStyleAttr:Int = 0,
    @StyleRes defStyleRes: Int = 0,
) : ImageView(context, attr, defStyleAttr, defStyleRes) {
    private val minViewSize = resources.getDimensionPixelSize(R.dimen.play_button_min_size)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Расчёт ширины
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val contentWidth = when (widthMode) {
            // Если ограничений на ширину нет —
            // берём минимальное значение
            MeasureSpec.UNSPECIFIED -> minViewSize

            // Если нужно указать точное значение ширины —
            // берём это значение
            MeasureSpec.EXACTLY -> widthSize

            // Если можно указать не более widthSize —
            // берём максимальную возможную ширину
            MeasureSpec.AT_MOST -> widthSize

            else -> error("Неизвестный режим ширины ($widthMode)")
        }

        // Расчёт высоты
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val contentHeight = when (heightMode) {
            // Если ограничений на высоту нет —
            // берём минимальное значение
            MeasureSpec.UNSPECIFIED -> minViewSize

            // Если нужно указать точное значение высоты —
            // берём это значение
            MeasureSpec.EXACTLY -> heightSize

            // Если можно указать не более heightSize —
            // берём максимальную возможную высоту
            MeasureSpec.AT_MOST -> heightSize

            else -> error("Неизвестный режим высоты ($heightMode)")
        }

        // Берём минимальное значение — либо ширину, либо высоту,
        // чтобы сформировать квадрат.
        val size = min(contentWidth, contentHeight)

        // Устанавливаем посчитанные размеры
        setMeasuredDimension(size, size)
    }





}

