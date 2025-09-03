package com.example.playlistmaker.ui.media.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.PlayList
import com.example.playlistmaker.ui.media.viewmodel.ReplacePlaylistViewModel
import com.example.playlistmaker.utils.getPlaylistIdFromArguments
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ReplacePlayListFragment(

) : AddPlayListFragment() {
    override val viewModel: ReplacePlaylistViewModel by viewModel() {
        parametersOf(
            getPlaylistIdFromArguments(REPLACE_LISTID)
        )
    }


    companion object {

        const val REPLACE_LISTID = "listId"

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observePlaylistData()
        viewModel.getReplacePlaylist()


    }


    private fun observePlaylistData() =
        viewModel.getLiveData.observe(viewLifecycleOwner) { playlist ->
            ur1 = playlist.image?.toUri()


            binding.edPlaylistName.setText(playlist.name)
            binding.edAboutPlaylist.setText(playlist.about)
            binding.imMines.setImageURI(playlist.image?.toUri())
            if (playlist.image != "null") {
                binding.ph.isVisible = false
            } else binding.ph.isVisible = true


            binding.toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            binding.toolbar.setTitle(R.string.replace_playlist_title)
            binding.btSave.setText(R.string.replace_playlist_button)

            binding.btSave.setOnClickListener {
                val playList = playlist.copy(
                    listId = playlist.listId,
                    name = title.toString(),
                    about = text.toString(),
                    image = ur1.toString()
                )


                viewModel.savePlayList(playList)
                findNavController().popBackStack()
            }

        }
}