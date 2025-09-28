package com.example.playlistmaker.ui.player.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.toRect
import com.example.playlistmaker.R
import kotlin.math.min

class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {



    private var playImage: Drawable? = null
    private var pauseImage: Drawable? = null
     var isPlaying = false
    private lateinit var playRect: RectF
    private lateinit var pauseRect: RectF

    init {
        // Получаем атрибуты из XML
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PlaybackButtonView)
        playImage = typedArray.getDrawable(R.styleable.PlaybackButtonView_playImage)
        pauseImage = typedArray.getDrawable(R.styleable.PlaybackButtonView_pauseImage)
        typedArray.recycle()

//        // Устанавливаем начальное изображение
        setImage(playImage)

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        changeState(isPlaying)
        // Отрисовка текущего изображения
        val currentImage = if (isPlaying) pauseImage else playImage
        if (currentImage != null) {
            currentImage.bounds = if (isPlaying) pauseRect.toRect() else playRect.toRect()
            currentImage.draw(canvas)
        }
    }

    private fun setImage(drawable: Drawable?) {
        if (drawable != null) {
            setBackground(drawable)

        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action){
            MotionEvent.ACTION_DOWN -> {
                if (isPlaying) {
           isPlaying = false
                }
                else {
                    isPlaying = true
                }

            }

            MotionEvent.ACTION_UP -> {
            changeState(isPlaying)
            }

        }
        return super.onTouchEvent(event)

    }

    fun changeState(isPlaying : Boolean){
        if (isPlaying) {


            setImage(pauseImage)
            // Запускаем воспроизведение
        } else {
            setImage(playImage)
            // Приостанавливаем воспроизведение
        }
    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // Рассчитываем размеры и положение изображений
        val padding = 0f // Отступы
        val imageSize = min(w, h) - 2 * padding

        // Координаты для изображения "Играть"
        playRect = RectF(
            padding,
            padding,
            padding + imageSize,
            padding + imageSize
        )

        // Координаты для изображения "Пауза"
        pauseRect = RectF(
            padding,
            padding,
            padding + imageSize,
            padding + imageSize
        )
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val size = min(measuredWidth, measuredHeight)
        setMeasuredDimension(size, size)
    }


}

