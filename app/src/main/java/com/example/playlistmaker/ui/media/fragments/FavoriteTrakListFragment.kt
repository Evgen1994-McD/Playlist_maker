package com.example.playlistmaker.ui.media.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoriteTrakListBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.media.viewmodel.FavoriteFragmentViewModel
import com.example.playlistmaker.ui.search.adapters.TrackAdapter
import com.example.playlistmaker.ui.search.listener.OnTrackClickListener
import com.example.playlistmaker.utils.debounce
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class FavoriteTrakListFragment : Fragment(), OnTrackClickListener {
    private lateinit var binding: FragmentFavoriteTrakListBinding
    private val viewModel: FavoriteFragmentViewModel by activityViewModel()
    private lateinit var trackClickDebounce: (Track) -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_favorite_trak_list, container, false)
        binding = FragmentFavoriteTrakListBinding.inflate(inflater, container, false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        trackClickDebounce =
            debounce(100L, viewLifecycleOwner.lifecycleScope, false){ track ->
                getTrackIntentAndStart(track, requireContext())
            }
//        showFavoriteTracks()

        observeFavoriteTracks()
        viewModel.favoriteTracks


    }

    companion object {

        fun newInstance() = FavoriteTrakListFragment()

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


    private fun displayTracks(tracks: List<Track>) = with(binding) {

        rcView1.layoutManager = LinearLayoutManager(requireContext())
        rcView1.adapter = TrackAdapter(tracks, this@FavoriteTrakListFragment)
        rcView1.makeVisible()
        phNtsh.makeInvisible()
        msgTxtBottom.makeInvisible()
    }

    private fun displayPlaceholders() = with(binding) {


        rcView1.makeInvisible()
        phNtsh.makeVisible()
        msgTxtBottom.makeVisible()
    }

    override fun onTrackClicked(track: Track) {

        trackClickDebounce(track)
    }

    fun getTrackIntentAndStart(track: Track, context: Context) {
        val bundle = Bundle().apply {
            putSerializable("track", track)

        }
        findNavController().navigate(R.id.playerFragment, bundle)

    }


    private fun observeFavoriteTracks() {
        viewModel.favoriteTracks.observe(viewLifecycleOwner) { tracks ->
            if (!tracks.isNullOrEmpty()) {
                displayTracks(tracks)

            } else displayPlaceholders()

        }

    }

}