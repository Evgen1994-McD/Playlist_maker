package com.example.playlistmaker.ui.player.fragments

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.ui.player.activity.PlayerActivity
import com.example.playlistmaker.ui.player.activity.PlayerActivity.Companion
import com.example.playlistmaker.ui.player.viewModel.PlayerCommand
import com.example.playlistmaker.ui.player.viewModel.PlayerViewModel
import com.example.playlistmaker.ui.player.viewModel.PlayerViewModelDepricate
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerFragment : Fragment() {

    private lateinit var binding: FragmentPlayerBinding // делаю байдинг
    private  val viewModel: PlayerViewModel by activityViewModel { parametersOf(requireActivity().intent) }


    companion object { // компаньон медиаплеера
         private const val noAlbum = "No Album"

    }





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null &&
            savedInstanceState.containsKey("isAudioPlayerVisible") &&
            savedInstanceState.getBoolean("isAudioPlayerVisible")
        ) {
            showAudioPlayerScreen()
        }





        viewModel.addListeners() // добавил листенеры
        viewModel.controlThemeInOtherWindows()
        viewModel.intentGetExtraBind()

        viewModel.getLiveData.observe(viewLifecycleOwner){ newState ->
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
            viewModel.mediaCommander(PlayerCommand.Play)
            viewModel.startUpdateProgress()
        }
        binding.pause.setOnClickListener {
            viewModel.mediaCommander(PlayerCommand.Pause)
            viewModel.stopUpdateProgress()
        }



    }

    override fun onSaveInstanceState(outState: Bundle) { // Сохраняем факт видимости аудиоплеера
        super.onSaveInstanceState(outState)// Сохраняем факт видимости аудиоплеера
        outState.putBoolean("isAudioPlayerVisible", true) // Сохраняем факт видимости аудиоплеера
    }



    fun showAudioPlayerScreen() { // Восстановим активность
        val intent = Intent(requireContext(), PlayerActivity::class.java) // Восстановим активность
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
        viewModel.mediaCommander(PlayerCommand.Pause)
        viewModel.stopUpdateProgress()

    }

    override fun onDestroy() { // закрываем плеер при завершении работы
        super.onDestroy()
        viewModel.reliesePlayer()
        viewModel.getLiveData.removeObservers(this) // отключил обсерверы от медиа


    }




    }


