package com.example.playlistmaker.ui.player.fragments

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.domain.models.PlayList
import com.example.playlistmaker.services.MusicService
import com.example.playlistmaker.ui.media.onPlaylistClickListener
import com.example.playlistmaker.ui.player.viewModel.PlayerViewModel
import com.example.playlistmaker.utils.getTrackFromArguments
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val PLAY = "PLAY"
private const val TRACK = "track"
private const val ARTIST = "artistName"
private const val TRACKNAME = "trackName"



class PlayerFragment : Fragment() {
    private lateinit var adapter: PlayListAdapter
    private lateinit var currentTrackId: String
    private lateinit var binding: FragmentPlayerBinding // делаю байдинг
    private val viewModel: PlayerViewModel by viewModel { parametersOf(getTrackFromArguments()) }
    private lateinit var bottomSheetContainer: LinearLayout
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private val noAlbum = "No Album"
    private var isVisible = true
    private var serviceIsBound = false
    private lateinit var musicService : MusicService

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicServiceBinder
            viewModel.setAudioPlayerControl(binder.getService())
            musicService = binder.getService()
            serviceIsBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            viewModel.removeAudioPlayerControl()
            serviceIsBound = false
        }
    }
    // Описали обработчик разрешения
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Если выдали разрешение — запускаем сервис.
            bindMusicService()
        } else {
            // Иначе просто покажем ошибку
            Toast.makeText(requireContext(), "Can't start foreground service!", Toast.LENGTH_LONG).show()
        }
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
        bindMusicService()
        clicker()
        viewModel.getAllPlaylist()
        displayPlayLists()
        viewModel.intentGetExtraBind()
        composeTrack()

        // На версии Android 13 и выше — сначала запросим разрешение
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            // На версиях ниже Android 13 —
            // можно сразу стартовать сервис.
            bindMusicService()
        }


        viewModel.observePlayerState().observe(viewLifecycleOwner) {playerState ->
            binding.progressTime.text = playerState.progress
            if (playerState.buttonText == PLAY) {
                        binding.play.isPlaying = false
                        binding.play.changeState(false)
                    } else {
                        binding.play.isPlaying = true
                        binding.play.changeState(true)
                    }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) { // Сохраняем факт видимости аудиоплеера
        super.onSaveInstanceState(outState)// Сохраняем факт видимости аудиоплеера
        outState.putBoolean("isAudioPlayerVisible", true) // Сохраняем факт видимости аудиоплеера
    }
    override fun onResume() {
        super.onResume()
        viewModel.intentGetExtraBind()
isVisible = true
        if (serviceIsBound) {
            (musicService as? MusicService)?.setShouldShowNotification(false)
        }

    }

    override fun onPause() { //пауза когда сворачиваем
        super.onPause()
        isVisible = false
        if (serviceIsBound) {
            (musicService as? MusicService)?.setShouldShowNotification(true)
        }

    }



    private fun composeTrack() {
        viewModel.getLiveData.observe(viewLifecycleOwner) { newState ->
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
            viewModel.onPlayerButtonClicked()

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


    override fun onDestroy() { // закрываем плеер при завершении работы
        super.onDestroy()
        unbindMusicService()
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
    private fun bindMusicService() {
        val intent = Intent(requireContext(), MusicService::class.java).apply {
            val track = getTrackFromArguments()
            putExtra(TRACK, track?.previewUrl)
            putExtra(ARTIST, track?.artistName)
            putExtra(TRACKNAME, track?.trackName)
        }
        requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)

    }
    private fun unbindMusicService() {
        requireContext().unbindService(serviceConnection)
    }

}






