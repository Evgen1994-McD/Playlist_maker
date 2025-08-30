package com.example.playlistmaker.ui.media.fragments

import android.content.Context
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistTracksBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.media.OnItemLongClickListener
import com.example.playlistmaker.ui.media.PlaylistTrackAdapter
import com.example.playlistmaker.ui.media.viewmodel.PlaylistTracksViewModel
import com.example.playlistmaker.ui.player.viewModel.PlayerViewModel
import com.example.playlistmaker.ui.search.adapters.TrackAdapter
import com.example.playlistmaker.ui.search.listener.OnTrackClickListener
import com.example.playlistmaker.utils.DialogManager
import com.example.playlistmaker.utils.declineNoun
import com.example.playlistmaker.utils.getPlaylistIdFromArguments
import com.example.playlistmaker.utils.getTrackFromArguments
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistTracksFragment : Fragment() {
    private val viewModel: PlaylistTracksViewModel by viewModel(){ parametersOf(getPlaylistIdFromArguments(PLAYLISTID))}
private lateinit var binding: FragmentPlaylistTracksBinding

companion object{
   const val ID = "tracksIds"
   const val PLAYLISTID = "ID1"
    val NAME =     "name"
    const val ABOUT =   "about"
    const val IMAGE =  "image"
    const val SIZE =  "size"
}
private var playlistId = 0
    private var tracksIds = ""
    private var name = ""
    private var about = ""
    private var image = ""
    private var size: Int? = 0
    private var summTime = ""

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
getArgs()

        showPlaylist()
       clickers()




        observeForPlaylistTracks()
        observeForPlaylistTime()
        viewModel.getTracksOfPlaylist(tracksIds)

    }


    private fun clickers()=with(binding){
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        overlay.setOnClickListener {

        }



    }

    private fun getArgs(){
        tracksIds = arguments?.getString(ID).toString()
        name = arguments?.getString(NAME).toString()
        about = arguments?.getString(ABOUT).toString()
        image = arguments?.getString(IMAGE).toString()
        size = arguments?.getInt(SIZE)

        playlistId = arguments?.getInt(PLAYLISTID)?.toInt() ?: 0
    }


    private fun observeForPlaylistTracks(){
        viewModel.getPlaylistTracksLiveData.observe(viewLifecycleOwner){ tracks ->
            showTracks(tracks)
        }
    }
    private fun observeForPlaylistTime(){
        viewModel.getTimeLiveData.observe(viewLifecycleOwner) { time->
            val oneForm = getString(R.string.minute1)
            val twoForm =getString(R.string.minute2)
            val fiveAndMoreForm =getString(R.string.minute3)

            binding.tvTime.text = declineNoun(time.toInt()?:0, oneForm, twoForm, fiveAndMoreForm)




        }
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
            rcView.adapter = PlaylistTrackAdapter(reversedTracks, object :OnTrackClickListener{
                override fun onTrackClicked(track: Track) {
                    getTrackIntentAndStart(track, requireContext())
                }
            }, object : OnItemLongClickListener{
                override fun onItemLongClick(track: Track) {
                    overlay.makeVisible()
                    DialogManager.showDialog(
                        requireContext(),
                        R.string.track_dialogue_title,
                        R.string.track_dialogue_message,
                        R.string.track_dialogue_positive,
                            R.string.track_dialogue_negative,
                        binding.overlay,
                        object : DialogManager.Listener{
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

    private fun showPlaylist()= with(binding){
         val options = RequestOptions()
            .centerCrop()
            .transform().centerCrop()

        tvName1.text = name
        tvText.text = about
        tvTime.text = summTime

        val oneForm = getString(R.string.track1)
        val twoForm =getString(R.string.track3)
        val fiveAndMoreForm =getString(R.string.track2)

        tvSize1.text = declineNoun(size?:0, oneForm, twoForm, fiveAndMoreForm)


        Glide.with(imMine.context)
            .load(image?.toUri())
            .apply(options)
            .placeholder(R.drawable.ic_placeholder_45)
            .error(R.drawable.ic_placeholder_45)
            .into(imMine)

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



}