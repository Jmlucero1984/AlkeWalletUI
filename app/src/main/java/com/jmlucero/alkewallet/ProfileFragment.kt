package com.jmlucero.alkewallet

import android.content.Context
import android.graphics.Bitmap
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.jmlucero.alkewallet.data.model.UiState
import com.jmlucero.alkewallet.databinding.FragmentIngresarDineroBinding
import com.jmlucero.alkewallet.databinding.FragmentProfileBinding
import com.jmlucero.alkewallet.viewmodel.ProfileViewModel
import com.jmlucero.alkewallet.viewmodel.UserViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!


    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }
    fun onPhotoTaken(bitmap: Bitmap) {
        val resized = resizeBitmap(bitmap)
        val file = bitmapToFile(resized,requireContext())
        profileViewModel.subirAvatar(file)
    }
    fun bitmapToFile(bitmap: Bitmap, context: Context): File {
        val file = File(context.cacheDir, "avatar.png")
        val stream = FileOutputStream(file)

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)

        stream.flush()
        stream.close()

        return file
    }
    fun resizeBitmap(bitmap: Bitmap): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, 256, 256, true)
    }

    private val takePicture =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            bitmap?.let {
                onPhotoTaken(it)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel = ViewModelProvider(requireActivity())[ProfileViewModel::class.java]
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_homePageFragment)
        }

        binding.avatarUsuario.setOnClickListener {
            takePicture.launch(null)
        }

    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            launch {
                profileViewModel.usuario.collect { usuario ->


                        binding.perfilNombreUsuario.setText(
                            "${usuario.nombre} ${usuario.apellido}")

                        var url = usuario.avatar_url//.substring(1, usuario.avatar_url.length - 1)

                        Picasso.get()
                            .load(url)
                            .placeholder(R.drawable.profile_svgrepo_com)
                            .error(R.drawable.profile_svgrepo_com)
                            .fit()
                            .centerCrop()
                            .into(binding.perfilAvatarUsuario)
                    }


                }
            }
        }
    }
}