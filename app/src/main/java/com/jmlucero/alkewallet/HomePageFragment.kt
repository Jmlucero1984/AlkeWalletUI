package com.jmlucero.alkewallet

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jmlucero.alkewallet.data.model.UiState
import com.jmlucero.alkewallet.data.model.Usuario
import com.jmlucero.alkewallet.databinding.FragmentHomePageBinding
import com.jmlucero.alkewallet.ui.home.TransaccionAdapter
import com.jmlucero.alkewallet.ui.home.UsuarioAdapter
import com.jmlucero.alkewallet.viewmodel.HomeViewModel
import com.jmlucero.alkewallet.viewmodel.TransactionViewModel
import com.jmlucero.alkewallet.viewmodel.UserViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch



class HomePageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentHomePageBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel
    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var usuarioAdapter: UsuarioAdapter
    private lateinit var transaccionAdapter: TransaccionAdapter
    private lateinit var homeViewModel: HomeViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        //findNavController().navigate(R.id.homeActivity)
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
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
        usuarioAdapter = UsuarioAdapter()

        transactionViewModel = ViewModelProvider(requireActivity())[TransactionViewModel::class.java]
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

        binding.campanitaIcon.setOnClickListener {
            binding.transactionsRecyclerView.apply {
                adapter = transaccionAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
            transactionViewModel.getTransaccionesSimple()
//            binding.frameLayoutEmptyTransactions.isVisible =
//                !binding.frameLayoutEmptyTransactions.isVisible
//            binding.fragmentContainerView.isVisible =
//                !binding.fragmentContainerView.isVisible
        }




        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    homeViewModel.balance.collect {state->
                        when (state) {
                            is UiState.Idle -> {}
                            is UiState.Loading -> {
                                Log.d("HOME_FRAGMENT", "Cargando balance...")
                            }
                            is UiState.Success -> {
                                binding.actualBalanceText.setText(state.data.balance.toString())
                            }

                            is UiState.Error -> {
                                Log.e("HOME_FRAGMENT", "Error: ${state.message}")
                            }
                        }

                    }

                }
                launch {
                    homeViewModel.usuario.collect { usuario ->

                        if (usuario != null) {

                            binding.perfilNombreUsuario.text =
                                "Hola ${usuario.nombre} ${usuario.apellido}"

                            var url = usuario.avatar_url.substring(1,usuario.avatar_url.length-1)

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
                        }
                    }
                }

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

                                    transaccionAdapter.submitList(state.data)
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