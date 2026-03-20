package com.jmlucero.alkewallet.fragments

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import com.yalantis.ucrop.UCrop
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.jmlucero.alkewallet.R
import com.jmlucero.alkewallet.data.model.entity.UiState
import com.jmlucero.alkewallet.data.model.response.AvatarResponse
import com.jmlucero.alkewallet.databinding.FragmentProfileBinding
import com.jmlucero.alkewallet.viewmodel.ProfileViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!


    companion object {
        fun newInstance() = ProfileFragment()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)

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

    fun startCrop(uri: Uri):Uri {
        val destinationUri = Uri.fromFile(File(requireContext().cacheDir, "cropped.jpg"))

        UCrop.of(uri, destinationUri)
            .withAspectRatio(1f, 1f) // cuadrado (ideal avatar)
            .withMaxResultSize(512, 512)
            .start(requireContext(), this)
        return destinationUri;
    }
    fun onPhotoTaken(bitmap: Bitmap) {
        val resized = resizeBitmap(bitmap)
        val file = bitmapToFile(resized, requireContext())
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
    fun uriToBitmap(uri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(requireContext().contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
        }
    }

   /* private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val bitmap = uriToBitmap(it)
            onPhotoTaken(bitmap)
        }
    }*/
   private val cropLauncher = registerForActivityResult(
       ActivityResultContracts.StartActivityForResult()
   ) { result ->
       when {
           result.resultCode == AppCompatActivity.RESULT_OK && result.data != null -> {
               val croppedUri = UCrop.getOutput(result.data!!)
               croppedUri?.let { uri ->
                   val bitmap = uriToBitmap(uri)
                   onPhotoTaken(bitmap)
               }
           }
           result.resultCode == UCrop.RESULT_ERROR -> {
               val error = UCrop.getError(result.data!!)
               Log.e("UCrop", "Error: ${error?.message}")
           }
           else -> Log.d("UCrop", "Cancelado")
       }
   }



    private val pickImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val safeUri = copyUriToTempFile(it)  // copiar primero
            safeUri?.let { safe -> launchCrop(safe) }
        }
    }
    // 3. Lanza UCrop — ahora desde el Fragment directamente
    private fun launchCrop(sourceUri: Uri) {
        val destinationUri = Uri.fromFile(
            File(requireContext().cacheDir, "cropped_${System.currentTimeMillis()}.jpg")
        )

        val intent = UCrop.of(sourceUri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(1080, 1080)
            .withOptions(UCrop.Options().apply {
                setCompressionQuality(90)
                setFreeStyleCropEnabled(true)
                setToolbarTitle("Recortar imagen")
                setToolbarColor(ContextCompat.getColor(requireContext(), R.color.orange))
                setStatusBarColor(ContextCompat.getColor(requireContext(), R.color.orange))
            })
            .getIntent(requireContext())  // ← getIntent en vez de .start()

        cropLauncher.launch(intent)       // ← launch desde el Fragment
    }

    // 4. Copiar URI al caché para evitar el SecurityException
    private fun copyUriToTempFile(sourceUri: Uri): Uri? {
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(sourceUri) ?: return null
            val tempFile = File(requireContext().cacheDir, "temp_${System.currentTimeMillis()}.jpg")
            tempFile.outputStream().use { output ->
                inputStream.use { input -> input.copyTo(output) }
            }
            FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.fileprovider",
                tempFile
            )
        } catch (e: Exception) {
            Log.e("UCrop", "Error copiando: ${e.message}")
            null
        }
    }


    private val takePicture =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            bitmap?.let {
                onPhotoTaken(it)
            }
        }
    fun showImagePickerDialog() {
        val options = arrayOf("Tomar foto", "Elegir de galería")

        AlertDialog.Builder(requireContext())
            .setTitle("Seleccionar opción")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> takePicture.launch(null)
                    1 -> pickImage.launch("image/*")
                }
            }
            .show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel = ViewModelProvider(requireActivity())[ProfileViewModel::class.java]
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_homePageFragment)
        }

        binding.avatarUsuario.setOnClickListener {
            showImagePickerDialog()
           // takePicture.launch(null)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    profileViewModel.uploadAvatarEvent.collect { state ->
                        when (state) {
                            is UiState.Error -> Log.i("UPLOAD_ERROR",state.message)
                            UiState.Idle -> Log.i("UPLOAD_IDLE","IDDLE")
                            UiState.Loading -> Log.i("UPLOAD_LOADING","LOADING")
                            is UiState.Success<AvatarResponse> -> {
                                Log.i("UPLOAD_SUCCESS",state.data.nuevaUrlAvatar)
                                profileViewModel.updateAvatarUrl(state.data.nuevaUrlAvatar)
                            }

                        }
                    }
                }

                launch {
                    profileViewModel.usuario.collect { usuario ->


                        if (usuario != null) {
                            binding.perfilNombreUsuario.setText("${usuario.nombre} ${usuario.apellido}")


                            var url =
                                usuario.avatar_url//.substring(1, usuario.avatar_url.length - 1)

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
}