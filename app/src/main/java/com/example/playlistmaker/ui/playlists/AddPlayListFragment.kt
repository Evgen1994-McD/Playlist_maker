package com.example.playlistmaker.ui.playlists

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAddPlayListBinding

class AddPlayListFragment : Fragment() {
private lateinit var binding: FragmentAddPlayListBinding


    private val viewModel: AddPlayListViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddPlayListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}