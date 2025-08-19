package com.example.playlistmaker.ui.playlists

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.edPlaylistName.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
          if (p0.isNullOrEmpty()){
              binding.button.isEnabled=false
              } else binding.button.isEnabled = true
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })


    }
}