package com.example.playlistmaker.ui.player.fragments

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
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
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerFragment : Fragment() {
    private lateinit var adapter: PlayListAdapter
    private lateinit var currentTrackId: String
    private lateinit var binding: FragmentPlayerBinding // делаю байдинг

    private val viewModel: PlayerViewModel by viewModel { parametersOf(getTrackFromArguments()) }
    private lateinit var bottomSheetContainer: LinearLayout
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
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

        clicker()

        viewModel.getAllPlaylist()
        displayPlayLists()


        /*
        Сохраняю трек в таблицу треков, далее бандлом отправляю Id на фрагмент создания плейлиста,
        Там запишу этот Id в результате создания нового плейлиста
         */




        viewModel.addListeners() // добавил листенеры

        viewModel.intentGetExtraBind()

        composeTrack()


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
            when {
                !newState.trackName.isEmpty() && !newState.collectionName.contains(noAlbum) -> {

                    saveAndDeleteFavoriteTrack(newState.isLike)
                    binding.play.isPlaying = newState.isPlaying
                    binding.play.changeState(newState.isPlaying)

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
                    Glide.with(binding.imMine.context)
                        .load(newState.artworkUrl100)
                        .apply(options)
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

    private fun clicker() {


        val bottomView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }


        binding.addOnPlaylist.setOnClickListener {
            binding.bottomSheet.isVisible = true
            binding.overlay.isVisible = true
            bottomView.isVisible = false

            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }

        binding.overlay.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.btNewPlaylist.setOnClickListener {
            currentTrackId = viewModel.getLiveData.value?.trackId.toString()

            findNavController().navigate(R.id.addPlayListFragment)
        }

        binding.play.setOnClickListener {
            if (binding.play.isPlaying) {
            viewModel.mediaCommander(PlayerCommand.Play)
            viewModel.startUpdateProgress()
            } else {
                viewModel.mediaCommander(PlayerCommand.Pause)
                viewModel.stopUpdateProgress()
            }
        }



        bottomSheetContainer = binding.bottomSheet
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)



        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
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


    private fun saveAndDeleteFavoriteTrack(isLike: Boolean) {
        if (isLike) {
            binding.dislike.visibility = View.VISIBLE
            binding.like.visibility = View.INVISIBLE

        } else {
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

    private fun displayPlayLists() = with(binding) {
        rcView.layoutManager = LinearLayoutManager(requireContext())
        adapter = PlayListAdapter(listener = object : onPlaylistClickListener {
            override fun onPlaylistClicked(playList: PlayList) {
                if (viewModel.compareTracksIds(playList)) {
                    Snackbar.make(
                        requireView(),
                        getString(R.string.add_not) + " ${playList.name}",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    viewModel.insertTrackInPlaylistsTable(playList)

                    // Подождите обновления состояния и отобразите SnackBar

                    viewModel.getLiveData.observe(viewLifecycleOwner) { state ->
                        if (state.isSuccess) {
                            Snackbar.make(
                                requireView(),
                                getString(R.string.add_in) + " ${playList.name}",
                                Snackbar.LENGTH_SHORT
                            ).show()

                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                        }
                    }

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






