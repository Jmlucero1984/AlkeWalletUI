package com.jmlucero.alkewallet

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.jmlucero.alkewallet.data.model.UiState
import com.jmlucero.alkewallet.databinding.FragmentHomePageBinding
import com.jmlucero.alkewallet.viewmodel.AuthViewModel
import kotlinx.coroutines.launch


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomePageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomePageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentHomePageBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


        //findNavController().navigate(R.id.homeActivity)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        binding.usuarioProfileImg.setOnClickListener {
            findNavController().navigate(R.id.profileActivity)
        }
        binding.enviarDineroButton.setOnClickListener {
           // findNavController().navigate(R.id.action_home_to_enviarDinero)
            viewModel.cargarUsuarios()
        }
        binding.ingresarDineroButton.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_ingresarDinero)
        }

        binding.campanitaIcon.setOnClickListener {
            binding.frameLayoutEmptyTransactions.isVisible =
                !binding.frameLayoutEmptyTransactions.isVisible
            binding.fragmentContainerView.isVisible =
                !binding.fragmentContainerView.isVisible
        }




        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.usuariosState.collect { state ->

                    when (state) {

                        is UiState.Idle -> {}

                        is UiState.Loading -> {
                            Log.d("HOME_FRAGMENT", "Cargando usuarios...")
                        }

                        is UiState.Success -> {
                            Log.d("HOME_FRAGMENT", "Usuarios recibidos:")

                            state.data.forEach { usuario ->
                                Log.d(
                                    "HOME_FRAGMENT",
                                    "ID: ${usuario.usuario_id} | Nombre: ${usuario.nombre} | Email: ${usuario.email}"
                                )
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


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomePageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomePageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}