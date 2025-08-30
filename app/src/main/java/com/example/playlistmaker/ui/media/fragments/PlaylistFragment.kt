package com.example.playlistmaker.ui.media.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
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
import com.example.playlistmaker.ui.search.listener.OnTrackClickListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class PlaylistFragment : Fragment(), onPlaylistClickListener {
    private lateinit var binding: PlaylistFragmentBinding
    private val viewModel: PlaylistFragmentViewModel by activityViewModel()

    companion object{
        fun newInstance() = PlaylistFragment()


        const val ID = "tracksIds"
        val NAME =     "name"
        const val ABOUT =   "about"
        const val IMAGE =  "image"
        const val SIZE =  "size"
    }

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
                binding.rcView.makeInvisible()
            } else {

                   showPlaylists(playlists)

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

    @SuppressLint("SetTextI18n")
    override fun onPlaylistClicked(playList: PlayList) {
       try {
           val bundle = Bundle().apply {
               playList.listId?.toInt()?.let { putInt("ID1", it) }
               putString(ID, playList.tracksId)
               putString(NAME, playList.name)
               putString(ABOUT, playList.about)
               putString(IMAGE, playList.image)
               putInt(SIZE, playList.size)
           }
           findNavController().navigate(R.id.playlistTracksFragment, bundle)
       } catch(e: Exception){
       }
    }



    override fun onResume() {
        super.onResume()
        observeForPlayLists()
    }
}