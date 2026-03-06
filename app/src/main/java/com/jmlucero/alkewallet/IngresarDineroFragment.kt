package com.jmlucero.alkewallet

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.jmlucero.alkewallet.data.model.Deposito
import com.jmlucero.alkewallet.data.model.UiState
import com.jmlucero.alkewallet.databinding.FragmentEnviarDineroBinding
import com.jmlucero.alkewallet.databinding.FragmentHomePageBinding
import com.jmlucero.alkewallet.databinding.FragmentIngresarDineroBinding
import com.jmlucero.alkewallet.viewmodel.UserViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import java.math.BigDecimal


class IngresarDineroFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentIngresarDineroBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIngresarDineroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.homePageFragment)
        }
        binding.ingresarDineroButton.setOnClickListener {
            val monto = BigDecimal(binding.ingreseCantidad.getText().toString())


            userViewModel.depositar(Deposito(monto))

        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    userViewModel.depositoState.collect { state ->
                        when (state) {
                            is UiState.Idle -> {}
                            is UiState.Loading -> {
                                Log.d("INGRESAR_DINERO_FRAGMENT", "Enviando depósito...")
                            }

                            is UiState.Success -> {
                                Toast.makeText(context,state.data.mensaje, Toast.LENGTH_LONG).show()
                                binding.ingreseCantidad.setText("")
                                binding.ingreseNota.setText("")
                            }

                            is UiState.Error -> {
                                Toast.makeText(context,state.message, Toast.LENGTH_LONG).show()
                                Log.e("INGRESAR_DINERO_FRAGMENT", "Error: ${state.message}")
                            }
                        }

                    }

                }
                launch {
                    userViewModel.usuario.collect { usuario ->

                        if (usuario != null) {
                            binding.nombreUsuario.text =
                                "${usuario.nombre} ${usuario.apellido}"
                            binding.emailUsuario.text =
                                usuario.email.toString()

                            var url = usuario.avatar_url.substring(1,usuario.avatar_url.length-1)

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

