package com.example.playlistmaker.presentation.media.fragments

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistTracksBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.media.OnItemLongClickListener
import com.example.playlistmaker.presentation.media.PlaylistTrackAdapter
import com.example.playlistmaker.presentation.media.viewmodel.PlaylistTracksViewModel
import com.example.playlistmaker.presentation.search.listener.OnTrackClickListener
import com.example.playlistmaker.utils.DialogManager
import com.example.playlistmaker.utils.declineNoun
import com.example.playlistmaker.utils.getPlaylistIdFromArguments
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistTracksFragment : Fragment() {

    private val viewModel: PlaylistTracksViewModel by viewModel() {
        parametersOf(
            getPlaylistIdFromArguments(PLAYLISTID)
        )
    }

    private lateinit var binding: FragmentPlaylistTracksBinding
    private lateinit var bottomSheetContainer: LinearLayout
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    companion object {

        const val PLAYLISTID = "ID1"

        const val REPLACE_LISTID = "listId"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistTracksBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomSheetMenu()

        clickers()
        observeForPlaylistTracks()
        viewModel.getPlayListState()


    }


    private fun clickers() = with(binding) {
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        overlay.setOnClickListener {
            overlay.makeInvisible()
            bottomSheetMenu.makeGone()
        }

        btMenu.setOnClickListener {
            bottomSheetMenu.makeVisible()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            overlay.makeVisible()
        }


    }

    private fun bottomSheetMenu() {
        bottomSheetContainer = binding.bottomSheetMenu
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


    private fun observeForPlaylistTracks() = with(binding) {
        viewModel.getPlaylistTracks1LiveData.observe(viewLifecycleOwner) { state ->

            sharePlaylist.setOnClickListener {
                if (state.size != 0) {
                    viewModel.generateAndSharePlayList(
                        state.name,
                        state.title,
                        state.tracks,
                        requireContext()
                    )
                } else snackBarNoSharing()
            }

            btShare.setOnClickListener {
                if (state.size != 0) {
                    viewModel.generateAndSharePlayList(
                        state.name,
                        state.title,
                        state.tracks,
                        requireContext()
                    )
                } else snackBarNoSharing()

            }

            deletePlaylist.setOnClickListener {
                DialogManager.showDialog(
                    requireContext(),
                    requireContext().getString(R.string.delete_playlist_dialogue) + " «${state.name}»?",
                    "",
                    R.string.track_dialogue_positive,
                    R.string.track_dialogue_negative,
                    binding.overlay,
                    object : DialogManager.Listener {
                        override fun onClick() {
                            lifecycleScope.launch {
                                viewModel.deleteTracksOfDeletedPlayList()

                                delay(300)
                                findNavController().popBackStack()
                            }
                        }
                    }


                )
            }

            replacePlaylistInfo.setOnClickListener {
                val bundle = Bundle().apply {

                    state.listId?.let { it1 -> putInt(REPLACE_LISTID, it1) }
                }
                findNavController().navigate(R.id.action_playlistTracksFragment_to_replacePlayListFragment, bundle)
            }

            showTracks(state.tracks)


            val options = RequestOptions()
                .centerCrop()
                .transform().centerCrop()

            timeToMinutes(state.time)
            tvName1.text = state.name
            tvText.text = state.title


            val oneForm = getString(R.string.track1)
            val twoForm = getString(R.string.track3)
            val fiveAndMoreForm = getString(R.string.track2)

            tvSize1.text = declineNoun(state.size ?: 0, oneForm, twoForm, fiveAndMoreForm)


            Glide.with(imMine.context)
                .load(state.image?.toUri())
                .apply(options)
                .placeholder(R.drawable.ic_placeholder_45)
                .error(R.drawable.ic_placeholder_45)
                .into(imMine)

            val displayMetrics = resources.displayMetrics
            val screenHeightInDp = displayMetrics.heightPixels / displayMetrics.densityDpi * 320f
            val peekHeightPercentage = (screenHeightInDp * 0.2f).toInt() // 10%
val btStandartContainer = bottomSheet
            val btStBeh = BottomSheetBehavior.from(btStandartContainer)
            btStBeh.peekHeight = peekHeightPercentage


            installBottomSheetMenu(
                state.size,
                state.name, state.image
            )


        }
    }

    private fun timeToMinutes(time: String) {

        val oneForm = getString(R.string.minute1)
        val twoForm = getString(R.string.minute2)
        val fiveAndMoreForm = getString(R.string.minute3)

        binding.tvTime.text = declineNoun(time.toInt() ?: 0, oneForm, twoForm, fiveAndMoreForm)


    }

    private fun getTrackIntentAndStart(track: Track, context: Context) {
        val bundle = Bundle().apply {
            putSerializable("track", track)

        }
        findNavController().navigate(R.id.playerFragment, bundle)

    }


    private fun showTracks(tracks: List<Track>) {
        with(binding) {
            val reversedTracks = tracks.reversed()
            rcView.layoutManager = LinearLayoutManager(requireContext())
            rcView.adapter = PlaylistTrackAdapter(reversedTracks, object : OnTrackClickListener {
                override fun onTrackClicked(track: Track) {
                    getTrackIntentAndStart(track, requireContext())
                }
            }, object : OnItemLongClickListener {
                override fun onItemLongClick(track: Track) {
                    overlay.makeVisible()
                    DialogManager.showDialog(
                        requireContext(),
                        requireContext().getString(R.string.track_dialogue_title),
                        "",
                        R.string.track_dialogue_positive,
                        R.string.track_dialogue_negative,
                        binding.overlay,
                        object : DialogManager.Listener {
                            override fun onClick() {
                                overlay.makeGone()
                                viewModel.deleteTrackOfPlaylistById(track.trackId)
                                /*
                                Удалили трек из плейлиста по Id
                                 */
                            }

                        })

                }

            })
            rcView.makeVisible()
            rcView.makeVisible()


        }
    }

    private fun installBottomSheetMenu(size: Int, name: String, imageUri: String) {
        with(binding) {

            val options = RequestOptions().centerCrop()
            val radiusInDP = 2f
            val radiusInPX = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                radiusInDP,
                requireContext().resources.displayMetrics
            )
            val oneForm = requireContext().getString(R.string.track1)
            val twoForm = requireContext().getString(R.string.track3)
            val fiveAndMoreForm = requireContext().getString(R.string.track2)

            albumInfo.text = declineNoun(size, oneForm, twoForm, fiveAndMoreForm)
            albumName.text = name




            Glide.with(albumImage.context)
                .load(imageUri?.toUri())
                .transform(RoundedCorners(radiusInPX.toInt()))
                .apply(options)
                .placeholder(R.drawable.ic_placeholder_45)
                .error(R.drawable.ic_placeholder_45)
                .into(albumImage)

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


    private fun snackBarNoSharing() {
        Snackbar.make(
            requireView(),
            getString(R.string.no_possible_share_playlist),
            Snackbar.LENGTH_SHORT
        ).show()
    }


}