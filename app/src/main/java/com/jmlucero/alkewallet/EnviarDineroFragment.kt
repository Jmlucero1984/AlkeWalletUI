package com.jmlucero.alkewallet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.jmlucero.alkewallet.databinding.FragmentEnviarDineroBinding
import com.jmlucero.alkewallet.databinding.FragmentIngresarDineroBinding
import com.jmlucero.alkewallet.viewmodel.UserViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch


class EnviarDineroFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentEnviarDineroBinding? = null
    private val binding get() = _binding!!
    //private lateinit var userViewModel: UserViewModel


    private val userViewModel: UserViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEnviarDineroBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.homePageFragment)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    userViewModel.usuario.collect { usuario ->

                        if (usuario != null) {
                            binding.nombreUsuario.text =
                                "${usuario.nombre} ${usuario.apellido}"
                            binding.emailUsuario.text =
                                usuario.email.toString()

                            var url = usuario.avatar_url.substring(1, usuario.avatar_url.length - 1)

                            Picasso.get()
                                .load(url)
                                .placeholder(R.drawable.profile_svgrepo_com)
                                .error(R.drawable.profile_svgrepo_com)
                                .fit()
                                .centerCrop()
                                .into(binding.avatarUsuario)
                        }
                    }
                }
            }
        }
    }



}