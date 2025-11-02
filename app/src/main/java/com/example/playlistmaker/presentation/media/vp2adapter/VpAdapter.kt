package com.example.playlistmaker.presentation.media.vp2adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.presentation.media.fragments.FavoriteTrakListFragment
import com.example.playlistmaker.presentation.media.fragments.PlaylistFragment

class VpAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {


    override fun getItemCount(): Int = 2 // Количество страниц

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> FavoriteTrakListFragment.newInstance()
            1 -> PlaylistFragment.newInstance()
            else -> throw IllegalArgumentException("Invalid position!")
        }
}