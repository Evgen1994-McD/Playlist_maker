package com.example.playlistmaker.ui.player.fragments

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.domain.models.PlayList
import com.example.playlistmaker.ui.media.onPlaylistClickListener
import com.example.playlistmaker.ui.player.viewModel.PlayerCommand
import com.example.playlistmaker.ui.player.viewModel.PlayerViewModel
import com.example.playlistmaker.utils.getTrackFromArguments
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.withContext
import org.koin.android.scope.createScope
import org.koin.android.scope.getOrCreateScope
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope

class PlayerFragment : Fragment() {
private lateinit var adapter: PlayListAdapter

    private lateinit var binding: FragmentPlayerBinding // делаю байдинг

    private val viewModel: PlayerViewModel by viewModel { parametersOf(getTrackFromArguments()) }

    /*
    by ViewModel привяжет вьюмодел к циклу жизни фрагмента
     */


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
        binding = FragmentPlayerBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



val bottomView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.getAllPlaylist()
displayPlayLists()


      val  bottomSheetContainer = view.findViewById<LinearLayout>(R.id.bottom_sheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)


        binding.addOnPlaylist.setOnClickListener {
            binding.bottomSheet.isVisible = true
            binding.overlay.isVisible = true
            bottomView.isVisible = false

            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // newState — новое состояние BottomSheet
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {

                        // загружаем рекламный баннер
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        // останавливаем трейлер
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                        bottomView.isVisible = true

                        // возобновляем трейлер
                    }
                    else -> {
                        // Остальные состояния не обрабатываем
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })



        viewModel.addListeners() // добавил листенеры

        viewModel.intentGetExtraBind()

        composeTrack()


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


    override fun onResume() {
        super.onResume()
        viewModel.intentGetExtraBind()

    }

    private fun composeTrack() {
        viewModel.getLiveData.observe(viewLifecycleOwner) { newState ->
            if (newState.isPlaying == false) {
                binding.play.isEnabled
                binding.play.makeVisible()
                binding.pause.makeInvisible()
            } else if (newState.isPlaying == true) {
                binding.play.isEnabled = true
                binding.play.makeInvisible()
                binding.pause.makeVisible()
            }
            when {
                !newState.trackName.isEmpty() && !newState.collectionName.contains(noAlbum) -> {


                    saveAndDeleteFavoriteTrack(newState.isLike)



                    binding.tvGenre.text = newState.primaryGenreName
                    binding.tvCountry.text = newState.country
                    binding.tvAlbum.text = newState.collectionName
                    binding.tvTime.text = newState.trackTimeMillis
                    binding.tvYear.text = newState.releaseDate
                    binding.tvAlbum.makeVisible()// убираем поле альбом если нет альбома
                    binding.tvAlbum.text =
                        newState.collectionName// убираем поле альбом если нет альбома
                    binding.tvGroup.text = newState.artistName
                    binding.tvTrackName.text = newState.trackName

                    binding.progressTime.text = newState.progress
                    val options = RequestOptions().centerCrop()//опции для Glide
                    val radiusInDP = 8f
                    val radiusInPX = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        radiusInDP,
                        resources.displayMetrics
                    )
                    Glide.with(binding.imMine.context).load(newState.artworkUrl100).apply(options)
                        .placeholder(R.drawable.ph_media_312).error(R.drawable.ph_media_312)
                        .transform(RoundedCorners(radiusInPX.toInt()))
                        .into(binding.imMine)
                }

                newState.collectionName.contains(noAlbum) -> {
                    binding.tvAlbum.makeGone()// убираем поле альбом если нет альбома
                    binding.tvAlbumLeft.makeGone()// убираем поле альбом если нет альбома
                }

                !newState.progress.isEmpty() -> binding.progressTime.text = newState.progress
            }
        }
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
viewModel.getPlaylistsLiveData.removeObservers(this)
        viewModel.getLiveData.removeObservers(this) // отключил обсерверы от медиа

    }


    private fun saveAndDeleteFavoriteTrack(isLike:Boolean){
        if (isLike) {
            binding.dislike.visibility = View.VISIBLE
            binding.like.visibility = View.INVISIBLE

        } else
        {
            binding.dislike.visibility = View.INVISIBLE
            binding.like.visibility = View.VISIBLE
        }



        binding.like.setOnClickListener {

            viewModel.saveTrackToFavorite()
            binding.like.visibility = View.INVISIBLE
            binding.dislike.visibility = View.VISIBLE

        }

  binding.dislike.setOnClickListener {
      binding.like.visibility = View.VISIBLE
      binding.dislike.visibility = View.INVISIBLE
      viewModel.deleteTrackFromFavorite()

        }

    }

    private fun displayPlayLists()=with(binding){
        rcView.layoutManager = LinearLayoutManager(requireContext())
        adapter = PlayListAdapter(listener = object:onPlaylistClickListener{
            override fun onPlaylistClicked(playList: PlayList) {
                if (viewModel.compareTracksIds(playList))  // проверим, есть ли трек который на экране, в плейлисте
                {
                    Snackbar.make(requireView(), "Трек уже есть в плейлисте ${playList.name}", Snackbar.LENGTH_SHORT).show()
                } else {
                    Snackbar.make(requireView(), "Трек успешно добавлен в плейлист ${playList.name}", Snackbar.LENGTH_SHORT).show()

                    viewModel.insertTrackInPlaylistsTable(playList)


                }
            }

        }, PlaylistDiffCallback())
        rcView.adapter = adapter
        rcView.makeVisible()
        viewModel.getPlaylistsLiveData.observe(viewLifecycleOwner) { playlists ->
            adapter.submitNewList(playlists)


        }

    }

    }






