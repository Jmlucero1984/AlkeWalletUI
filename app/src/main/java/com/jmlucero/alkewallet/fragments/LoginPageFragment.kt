package com.jmlucero.alkewallet.fragments

import android.os.Bundle
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
import com.jmlucero.alkewallet.R
import com.jmlucero.alkewallet.data.model.entity.UiState
import com.jmlucero.alkewallet.databinding.FragmentLoginPageBinding
import com.jmlucero.alkewallet.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginPageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class LoginPageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentLoginPageBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginPageBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        binding.textoCrearCuenta.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_signin)
        }

        /*
        binding.loginButton.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_home)
        }*/
        // Login button
        binding.loginButton.setOnClickListener {
            viewModel.login(
                binding.emailInput.text.toString(),
                binding.passwordInput.text.toString()
            )
        }
        // Observar estado
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.loginState.collect { state ->

                        when (state) {

                            is UiState.Idle -> {
                                // Estado inicial
                            }

                            is UiState.Loading -> {
                                // binding.progressBar.visibility = View.VISIBLE
                                binding.loginButton.isEnabled = false
                            }

                            is UiState.Success -> {
                                // binding.progressBar.visibility = View.GONE
                                binding.loginButton.isEnabled = true

                                //findNavController().navigate(R.id.action_login_to_home)
                                viewModel.saveCurrentLoggedUser()
                            }

                            is UiState.Error -> {
                                //binding.progressBar.visibility = View.GONE
                                binding.loginButton.isEnabled = true

                                Toast.makeText(
                                    requireContext(),
                                    state.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }

                launch {
                    viewModel.loggedUser.collect { state ->

                        when (state) {

                            is UiState.Idle -> {
                                // Estado inicial
                            }

                            is UiState.Loading -> {
                                // binding.progressBar.visibility = View.VISIBLE
                              //  binding.loginButton.isEnabled = false
                            }

                            is UiState.Success -> {
                                // binding.progressBar.visibility = View.GONE
                              //  binding.loginButton.isEnabled = true



                                findNavController().navigate(R.id.action_login_to_home)

                            }

                            is UiState.Error -> {
                                //binding.progressBar.visibility = View.GONE
                                //binding.loginButton.isEnabled = true

                                Toast.makeText(
                                    requireContext(),
                                    state.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }


            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}