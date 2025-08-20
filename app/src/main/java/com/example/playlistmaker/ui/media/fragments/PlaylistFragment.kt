package com.example.playlistmaker.ui.media.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistFragmentBinding
import com.example.playlistmaker.domain.models.PlayList
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.media.PlayListAdapter
import com.example.playlistmaker.ui.media.fragments.FavoriteTrakListFragment
import com.example.playlistmaker.ui.media.onPlaylistClickListener
import com.example.playlistmaker.ui.media.viewmodel.PlaylistFragmentViewModel
import com.example.playlistmaker.ui.search.adapters.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class PlaylistFragment : Fragment(), onPlaylistClickListener {
    private lateinit var binding: PlaylistFragmentBinding
    private val viewModel: PlaylistFragmentViewModel by activityViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = PlaylistFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
observeForPlayLists()

        viewModel.getAllPlaylists()






        binding.btCreatePlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_addPlayListFragment)
        }

    }

    companion object {

        fun newInstance() = PlaylistFragment()

    }

    private fun showPlaylists(playLists: List<PlayList>) {
        with(binding) {

                rcView.layoutManager = GridLayoutManager(requireContext(), 2)
                rcView.adapter = PlayListAdapter(playLists, this@PlaylistFragment)
                rcView.makeVisible()
            phNtsh2.makeInvisible()
            msgTxtBottom.makeInvisible()
            rcView.makeVisible()

        }

    }

    private fun observeForPlayLists(){
        viewModel.getLiveData.observe(viewLifecycleOwner) { playlists ->
            if (playlists.isNullOrEmpty()) {
                binding.phNtsh2.makeVisible()
                binding.msgTxtBottom.makeVisible()
            } else {
                with(binding) {
                   showPlaylists(playlists)
                }
            }
            /*
            тут будет логика, это заготовка
             */
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

    override fun onPlaylistClicked(playList: PlayList) {
        TODO("Not yet implemented")
    }

}