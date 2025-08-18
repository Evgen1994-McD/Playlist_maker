package com.example.playlistmaker.ui.media.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistFragmentBinding
import com.example.playlistmaker.ui.media.viewmodel.PlaylistFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class PlaylistFragment : Fragment() {
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




        showPlaylists()
        viewModel.getLiveData.observe(viewLifecycleOwner) { new ->
            if (new == false) {
                binding.phNtsh2.makeVisible()
                binding.msgTxtBottom.makeVisible()
            } else {
                with(binding) {
                    phNtsh2.makeVisible()
                    msgTxtBottom.makeVisible()
                }
            }
            /*
            тут будет логика, это заготовка
             */
        }

        binding.btCreatePlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_addPlayListFragment)
        }

    }

    companion object {

        fun newInstance() = PlaylistFragment()

    }

    private fun showPlaylists() {
        with(binding) {
            phNtsh2.makeVisible()
            msgTxtBottom.makeVisible()
            btCreatePlaylist.makeVisible()
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

}