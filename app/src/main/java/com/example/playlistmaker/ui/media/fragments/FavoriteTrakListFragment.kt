package com.example.playlistmaker.ui.media.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoriteTrakListBinding
import com.example.playlistmaker.ui.media.activity.ActivityMediaCatalogue
import com.example.playlistmaker.ui.media.viewmodel.FavoriteFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class FavoriteTrakListFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteTrakListBinding
    private val viewModel : FavoriteFragmentViewModel by activityViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//viewModel.controlThemeInOtherWindows()
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

showFavoriteTracks()
        viewModel.getLiveData.observe(viewLifecycleOwner){ new ->
            if(new==false) {
                binding.phNtsh.makeVisible()
                binding.msgTxtBottom.makeVisible()
            } else {
                with(binding){
                    phNtsh.makeVisible()
                    msgTxtBottom.makeVisible()
                }
            }
/*
тут будет логика, это заготовка
 */
        }

    }

    companion object {

        fun newInstance() = FavoriteTrakListFragment()

    }

    private fun showFavoriteTracks(){
        with(binding){
            phNtsh.makeVisible()
            msgTxtBottom.makeVisible()
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