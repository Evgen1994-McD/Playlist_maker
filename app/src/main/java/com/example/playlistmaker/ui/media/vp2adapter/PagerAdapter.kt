package com.example.playlistmaker.ui.media.vp2adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.ui.media.fragments.FavoriteTrakListFragment
import com.example.playlistmaker.ui.media.fragments.PlaylistFragment

class PagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {

    private val fragments = listOf(
        FavoriteTrakListFragment(),
        PlaylistFragment()
    )



    override fun getItemCount(): Int = 2 // Количество страниц

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> fragments[0]
            1 -> fragments[1]
            else -> throw IllegalArgumentException("Invalid position!")
        }
}