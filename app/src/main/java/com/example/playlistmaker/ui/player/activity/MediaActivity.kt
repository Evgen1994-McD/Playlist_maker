package com.example.playlistmaker.ui.player.activity

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.App
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.example.playlistmaker.ui.player.viewModel.MediaPlayerCommand
import com.example.playlistmaker.ui.player.viewModel.MediaViewModel
import com.example.playlistmaker.ui.search.viewModel.SearchViewModel
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class MediaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMediaBinding // делаю байдинг
    private val viewModel by viewModel<MediaViewModel>()


    companion object { // компаньон медиаплеера
        private const val noAlbum = "No Album"

    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_media)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

viewModel.setIntent(intent) // передал интент во вью модел
//
//        val factory = MediaViewModel.CustomViewModelFactory(
//            Creator.provideSwitchThemeUseCase(),// Делаю вью модел фактори
//            Creator.provideFavoriteInteractor(),
//            Creator.provideMediaInteractor(),
//            App.instance,
//            intent
//        )
//        viewModel = ViewModelProvider(this, factory)[MediaViewModel::class.java]

viewModel.addListeners() // добавил листенеры
        binding.toolbar.setNavigationOnClickListener {  //назад в Майнактивити
            finish()
        }

viewModel.controlThemeInOtherWindows()
            viewModel.intentGetExtraBind()
            viewModel.getLiveData.observe(this){ newState ->
              if (newState.isPlaying ==false) {
                  binding.play.isEnabled
                  binding.play.makeVisible()
                  binding.pause.makeInvisible()
              }else if (newState.isPlaying == true){
                  binding.play.isEnabled = true
                  binding.play.makeInvisible()
                  binding.pause.makeVisible()
              }
                when{
   !newState.trackName.isEmpty() && !newState.collectionName.contains(noAlbum) -> {
       binding.tvGenre.text = newState.primaryGenreName
        binding.tvCountry.text = newState.country
        binding.tvAlbum.text = newState.collectionName
        binding.tvTime.text = newState.trackTimeMillis
        binding.tvYear.text = newState.releaseDate
            binding.tvAlbum.makeVisible()// убираем поле альбом если нет альбома
            binding.tvAlbum.text = newState.collectionName// убираем поле альбом если нет альбома
        binding.tvGroup.text = newState.artistName
        binding.tvTrackName.text = newState.trackName

       binding.progressTime.text = newState.progress
       val options = RequestOptions().centerCrop()//опции для Glide
       val radiusInDP = 8f
       val radiusInPX = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, radiusInDP, resources.displayMetrics)
        Glide.with(binding.imMine.context).load(newState.artworkUrl100).apply(options)
            .placeholder(R.drawable.ph_media_312).error(R.drawable.ph_media_312)
            .transform(RoundedCorners(radiusInPX.toInt()))
            .into(binding.imMine)
}
                    newState.collectionName.contains(noAlbum) -> {
                                    binding.tvAlbum.makeGone()// убираем поле альбом если нет альбома
            binding.tvAlbumLeft.makeGone()// убираем поле альбом если нет альбома
                    }
              !newState.progress.isEmpty()  -> binding.progressTime.text = newState.progress
                }
            }


        binding.play.setOnClickListener {
            viewModel.mediaCommander(MediaPlayerCommand.Play)
            viewModel.startUpdateProgress()
        }
        binding.pause.setOnClickListener {
            viewModel.mediaCommander(MediaPlayerCommand.Pause)
viewModel.stopUpdateProgress()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) { // Сохраняем факт видимости аудиоплеера
        super.onSaveInstanceState(outState)// Сохраняем факт видимости аудиоплеера
        outState.putBoolean("isAudioPlayerVisible", true) // Сохраняем факт видимости аудиоплеера
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) { // Восстановим активность
        super.onRestoreInstanceState(savedInstanceState) // Восстановим активность
        if (savedInstanceState.containsKey("isAudioPlayerVisible") && savedInstanceState.getBoolean(
                "isAudioPlayerVisible"
            )
        ) {
            showAudioPlayerScreen()// Отображаем экран аудиоплеера

        }
    }

    fun showAudioPlayerScreen() { // Восстановим активность
        val intent = Intent(this, MediaActivity::class.java) // Восстановим активность
        startActivity(intent) // Восстановим активность
    }


    private fun View.makeGone() {
        this.visibility = View.GONE // функция для вью гон
    }

    private fun View.makeVisible() {
        this.visibility = View.VISIBLE // функция для вью визибл
    }

    private fun View.makeInvisible() {
        this.visibility = View.INVISIBLE // функция для вью инвизибл
    }

    override fun onPause() { //пауза когда сворачиваем
        super.onPause()
        viewModel.mediaCommander(MediaPlayerCommand.Pause)
        viewModel.stopUpdateProgress()

    }

    override fun onDestroy() { // закрываем плеер при завершении работы
        super.onDestroy()
       viewModel.reliesePlayer()
        viewModel.getLiveData.removeObservers(this) // отключил обсерверы от медиа


    }



}