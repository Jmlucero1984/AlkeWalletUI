package com.jmlucero.alkewallet.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jmlucero.alkewallet.R
import com.jmlucero.alkewallet.data.model.entity.UiState
import com.jmlucero.alkewallet.databinding.FragmentHomePageBinding
import com.jmlucero.alkewallet.ui.home.TransaccionAdapter
import com.jmlucero.alkewallet.ui.home.UsuarioAdapter
import com.jmlucero.alkewallet.viewmodel.HomeViewModel
import com.jmlucero.alkewallet.viewmodel.SharedViewModel
import com.jmlucero.alkewallet.viewmodel.TransactionViewModel
import com.jmlucero.alkewallet.viewmodel.UserViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.math.BigDecimal

@AndroidEntryPoint
class HomePageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentHomePageBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel
    private val transactionViewModel: TransactionViewModel by viewModels()
    private lateinit var usuarioAdapter: UsuarioAdapter
    private lateinit var transaccionAdapter: TransaccionAdapter
    private lateinit var homeViewModel: HomeViewModel
    private val sharedViewModel: SharedViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //findNavController().navigate(R.id.homeActivity)


    }

    private fun mostrarDialogoSalir() {
        AlertDialog.Builder(requireContext(),R.style.CustomAlertDialog)
            .setTitle("Salir")
            .setMessage("¿Quieres cerrar sesión?")
            .setPositiveButton("Sí") { _, _ ->
                logout()
            }
            .setNegativeButton("Cancelar", null)
            .setIcon(R.drawable.aw_icon_alert)
            .show()

    }
    private fun logout() {
        // 1. Limpiar datos (Room / token)
         homeViewModel.logout()

        // 2. Navegar limpiando todo el stack
        findNavController().navigate(
            R.id.loginFragment,
            null,
            NavOptions.Builder()
                .setPopUpTo(R.id.nav_graph, true)
                .build()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner
        ) {
            mostrarDialogoSalir()
        }

        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
        usuarioAdapter = UsuarioAdapter()


        transaccionAdapter = TransaccionAdapter()

        binding.usuarioProfileImg.setOnClickListener {
            findNavController().navigate(R.id.action_homePageFragment_to_profileFragment)
        }

        binding.enviarDineroButton.setOnClickListener {
//            binding.transactionsRecyclerView.apply {
//                adapter = usuarioAdapter
//                layoutManager = LinearLayoutManager(requireContext())
//            }
//            userViewModel.cargarUsuarios()
            findNavController().navigate(R.id.action_home_to_enviarDinero)
        }

        binding.ingresarDineroButton.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_ingresarDinero)
        }

        binding.transactionsRecyclerView.apply {
            adapter = transaccionAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        transactionViewModel.getTransaccionesSimple()
        binding.campanitaIcon.setOnClickListener {
            // transactionViewModel.getTransaccionesSimple()
            //  pickImage.launch("image/*")
            // takePicture.launch(null)
        }

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                var codigo: String? = ""
                launch {
                    sharedViewModel.mensajePendiente.observe(viewLifecycleOwner) { mensaje ->
                        Log.i("VIEW CREATED", "mensajePendiente: ")
                        if (!mensaje.isNullOrEmpty()) {
                            Log.i("VIEW CREATED", "Mensaje: " + mensaje)
                            Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
                            sharedViewModel.limpiarMensaje()
                        }
                    }
                }

                launch {
                    combine(
                        homeViewModel.usuarioConMoneda,
                        homeViewModel.cuenta
                    ) { _usuarioConMoneda, _cuenta ->

                        Pair(_usuarioConMoneda, _cuenta)

                    }.collect { (_usuarioConMoneda, _cuenta) ->

                        _usuarioConMoneda?.let { usuarioConMoneda ->
                            _cuenta?.let { cuenta ->
                                val codigo = usuarioConMoneda.moneda.codigo

                                Log.i("INFORMACION MONEDA", codigo)
                                Log.i("INFORMACION CUENTA", cuenta.balance.toString())
                                val bf = BigDecimal(cuenta.balance).setScale(2, BigDecimal.ROUND_HALF_UP)
                                binding.actualBalanceText.text = "$${bf} $codigo"


                               }

                        } ?: run{
                            findNavController().navigate(
                                R.id.loginFragment,
                                null,
                                NavOptions.Builder()
                                    .setPopUpTo(R.id.nav_graph, true)
                                    .build()
                            )
                        }

                    }
                }


                launch {
                    homeViewModel.usuarioConMoneda.collect { _usuarioConMoneda ->
                        _usuarioConMoneda?.let { usuarioConMoneda ->

                            binding.perfilNombreUsuario.text =
                                "Hola ${usuarioConMoneda.usuario.nombre} ${usuarioConMoneda.usuario.apellido}!"
                            var url = usuarioConMoneda.usuario.avatar_url
                            Picasso.get()
                                .load(url)
                                .placeholder(R.drawable.profile_svgrepo_com)
                                .error(R.drawable.profile_svgrepo_com)
                                .fit()
                                .centerCrop()
                                .into(binding.usuarioProfileImg, object : Callback {
                                    override fun onSuccess() {
                                        Log.d("Picasso", "Imagen cargada exitosamente")
                                    }

                                    override fun onError(e: Exception) {
                                        Log.e("Picasso", "Error cargando imagen: ${e.message}")
                                        e.printStackTrace()
                                    }
                                })
                        } ?: run {
                            // 🔥 Usuario ya no existe → salir del Home
                            findNavController().navigate(
                                R.id.loginFragment,
                                null,
                                NavOptions.Builder()
                                    .setPopUpTo(R.id.nav_graph, true)
                                    .build()
                            )
                        }
                    }

                }

//                launch {
//                    homeViewModel.usuario.collect { usuario ->
//                    .
//                        binding.perfilNombreUsuario.text =
//                            "Hola ${usuario.nombre} ${usuario.apellido}"
//                        var url = usuario.avatar_url.substring(1, usuario.avatar_url.length - 1)
//                        Picasso.get()
//                            .load(url)
//                            .placeholder(R.drawable.profile_svgrepo_com)
//                            .error(R.drawable.profile_svgrepo_com)
//                            .fit()
//                            .centerCrop()
//                            .into(binding.usuarioProfileImg, object : Callback {
//                                override fun onSuccess() {
//                                    Log.d("Picasso", "Imagen cargada exitosamente")
//                                }
//
//                                override fun onError(e: Exception) {
//                                    Log.e("Picasso", "Error cargando imagen: ${e.message}")
//                                    e.printStackTrace()
//                                }
//                            })
//                    }
//                }

                launch {
                    userViewModel.usuariosState.collect { state ->
                        when (state) {
                            is UiState.Idle -> {}
                            is UiState.Loading -> {
                                Log.d("HOME_FRAGMENT", "Cargando usuarios...")
                            }

                            is UiState.Success -> {
                                if (state.data.isEmpty()) {
                                    binding.frameLayoutEmptyTransactions.isVisible = true
                                    binding.transactionsRecyclerView.isVisible = false
                                } else {
                                    binding.frameLayoutEmptyTransactions.isVisible = false
                                    binding.transactionsRecyclerView.isVisible = true
                                    usuarioAdapter.submitList(state.data)
                                }
                            }

                            is UiState.Error -> {
                                Log.e("HOME_FRAGMENT", "Error: ${state.message}")
                            }
                        }
                    }
                }
                launch {
                    transactionViewModel.transaccionesState.collect { state ->
                        when (state) {
                            is UiState.Idle -> {}
                            is UiState.Loading -> {
                                Log.d("HOME_FRAGMENT", "Cargando transacciones...")
                            }

                            is UiState.Success -> {
                                if (state.data.isEmpty()) {
                                    binding.frameLayoutEmptyTransactions.isVisible = true
                                    binding.transactionsRecyclerView.isVisible = false
                                } else {
                                    binding.frameLayoutEmptyTransactions.isVisible = false
                                    binding.transactionsRecyclerView.isVisible = true

                                    transaccionAdapter.submitList(state.data,context)
                                }
                            }

                            is UiState.Error -> {
                                Log.e("HOME_FRAGMENT", "Error: ${state.message}")
                            }
                        }
                    }
                }
            }
        }
    }
}



