package com.example.playlistmaker.ui.media.fragments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAddPlayListBinding
import com.example.playlistmaker.domain.models.PlayList
import com.example.playlistmaker.ui.media.viewmodel.AddPlayListViewModel
import com.example.playlistmaker.utils.Constants
import com.example.playlistmaker.utils.DialogManager
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.io.File
import java.io.FileOutputStream

class AddPlayListFragment : Fragment() {
private lateinit var binding: FragmentAddPlayListBinding
private var ur1: Uri? = null
    private var title: CharSequence? = ""
    private var text: CharSequence? = ""
private  var trackId =""
    private val viewModel: AddPlayListViewModel by activityViewModel()

companion object{
    private const val playListName = "NAME"
    private const val playListBody = "BODY"
    private const val playListImage = "IMAGE"
}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddPlayListBinding.inflate(layoutInflater, container, false)
        return binding.root

    savedInstanceState?.let {
        binding.edPlaylistName.setText(it.getString(playListName))
        binding.edAboutPlaylist.setText(it.getString(playListBody))
        binding.imMines.setImageURI((it.getString(playListImage))?.toUri())
    }


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
watcherForTitle()
watcherForBody()
        trackId = arguments?.getString("track_id").toString()
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        binding.edPlaylistName.setOnClickListener {
            imm.showSoftInput(binding.edPlaylistName, InputMethodManager.SHOW_IMPLICIT)
        }

        binding.edAboutPlaylist.setOnClickListener {
            imm.showSoftInput(binding.edAboutPlaylist, InputMethodManager.SHOW_IMPLICIT)
        }



        binding.toolbar.setNavigationOnClickListener {
if (ur1 != null || title != "" || text != ""){
    showDialog()
} else findNavController().popBackStack()


        }


        val pickMediaPhoto =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()){ uri ->
                if (uri != null){
                    val options = RequestOptions().centerCrop()//опции для Glide
                    val radiusInDP = 8f
                    val radiusInPX = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        radiusInDP,
                        resources.displayMetrics
                    )
                    Glide.with(binding.imMines.context)
                        .load(uri)
                        .apply(options)

                        .transform(RoundedCorners(radiusInPX.toInt()))
                        .into(binding.imMines)
                    saveImageToPrivateStorage(uri)
                    binding.ph.isVisible = false
                    ur1 = uri
                } else {
                    binding.ph.isVisible = false

                }

            }
        binding.imMines.setOnClickListener {
pickMediaPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.btSave.setOnClickListener {
            snackBar()
            savePlayList()
            findNavController().popBackStack()
        }


    }


    private fun savePlayList(){
        var size = 0
        if (trackId.isNotEmpty()){
            size = 1
        }
        val playList = PlayList(null, title.toString(), text.toString(), ur1.toString(),trackId, size)

        viewModel.savePlayList(playList)
    }


    private fun saveImageToPrivateStorage(uri: Uri) {
        //создаём экземпляр класса File, который указывает на нужный каталог
        val filePath = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), Constants.playlistAlbum)
        //создаем каталог, если он не создан
        if (!filePath.exists()){
            filePath.mkdirs()
        }
        //создаём экземпляр класса File, который указывает на файл внутри каталога
        val file = File(filePath, "$title.jpg")
        ur1 = file.toUri()
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
                    binding.btSave.isEnabled=false
                } else binding.btSave.isEnabled = true
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(playListName, title.toString())
        outState.putString(playListBody, text.toString())
        outState.putString(playListImage, ur1.toString())
    }








}