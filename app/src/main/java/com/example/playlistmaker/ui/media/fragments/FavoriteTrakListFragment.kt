package com.example.playlistmaker.ui.media.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoriteTrakListBinding


class FavoriteTrakListFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteTrakListBinding

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

    companion object {

        fun newInstance() = FavoriteTrakListFragment()

    }

    private fun showFavoriteTracks(){

    }
}