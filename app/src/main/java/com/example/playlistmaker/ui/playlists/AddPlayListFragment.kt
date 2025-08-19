package com.example.playlistmaker.ui.playlists

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAddPlayListBinding
import com.example.playlistmaker.utils.DialogManager
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileOutputStream

class AddPlayListFragment : Fragment() {
private lateinit var binding: FragmentAddPlayListBinding
private var ur1: Uri? = null
    private var title: CharSequence? = null
    private var text: CharSequence? = null


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
watcherForTitle()
watcherForBody()



        binding.toolbar.setNavigationOnClickListener {
if (ur1 != null || title != null || text != null){
    showDialog()
} else findNavController().popBackStack()


        }


        val pickMediaPhoto =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()){ uri ->
                if (uri != null){
                    binding.imMine.setImageURI(uri)
                    saveImageToPrivateStorage(uri)
                    binding.ph.isVisible = false
                    ur1 = uri
                } else {
                    binding.ph.isVisible = false

                }

            }
        binding.imMine.setOnClickListener {
pickMediaPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.button.setOnClickListener {
            snackBar()
            findNavController().popBackStack()
        }


    }


    private fun saveImageToPrivateStorage(uri: Uri) {
        //создаём экземпляр класса File, который указывает на нужный каталог
        val filePath = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        //создаем каталог, если он не создан
        if (!filePath.exists()){
            filePath.mkdirs()
        }
        //создаём экземпляр класса File, который указывает на файл внутри каталога
        val file = File(filePath, "first_cover.jpg")
        // создаём входящий поток байтов из выбранной картинки
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        // создаём исходящий поток байтов в созданный выше файл
        val outputStream = FileOutputStream(file)
        // записываем картинку с помощью BitmapFactory
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

    private fun showDialog(){
        DialogManager.showDialog(requireContext(), R.string.title, R.string.text, R.string.positive, R.string.negative, object :DialogManager.Listener{
            override fun onClick() {
                findNavController().popBackStack()
            }

        })
    }



    private fun watcherForTitle(){
        binding.edPlaylistName.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.isNullOrEmpty()){
                    binding.button.isEnabled=false
                } else binding.button.isEnabled = true
                title = p0.toString()

            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    private fun snackBar(){
Snackbar.make(requireView(), "Плейлист [$title] cоздан", Snackbar.LENGTH_SHORT).show()
    }

    private fun watcherForBody(){
        binding.edAboutPlaylist.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    text = p0.toString()

            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }


}