package com.jmlucero.alkewallet.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.jmlucero.alkewallet.R
import com.jmlucero.alkewallet.data.model.entity.SignUpNuevoUsuario
import com.jmlucero.alkewallet.data.model.entity.UiState

import com.jmlucero.alkewallet.databinding.FragmentSignUpPageBinding

import com.jmlucero.alkewallet.viewmodel.SignUpViewModel

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignUpPageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@AndroidEntryPoint
class SignUpPageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentSignUpPageBinding? = null
    private val binding get() = _binding!!

    private lateinit var signUpViewModel: SignUpViewModel

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
        // Inflate the layout for this fragment
        _binding = FragmentSignUpPageBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signUpViewModel = ViewModelProvider(this)[SignUpViewModel::class.java]
        signUpViewModel.getCurrencies()

        binding.txtYaTieneCuenta.setOnClickListener {
            findNavController().navigate(R.id.action_signin_to_login)
        }

        var monedaSeleccionada:String =""


        binding.monedaDropdown.setOnItemClickListener { parent, view, position, id ->
            monedaSeleccionada = parent.getItemAtPosition(position).toString().split("-")[0].trim()

            Toast.makeText(context,"SELECCION: "+monedaSeleccionada, Toast.LENGTH_LONG).show()
        }


        binding.btnCrearCuenta.setOnClickListener {
            //findNavController().navigate(R.id.action_signin_to_home)
            if(binding.txtInputNombre.text.toString().isEmpty() || binding.txtInputApellido.text.toString().isEmpty() || binding.txtInputEmail.text.toString().isEmpty() ||
                binding.txtInputContrasenia.text.toString().isEmpty() || binding.txtInputConfirmaContrasenia.text.toString().isEmpty()){
                Toast.makeText(context, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(binding.txtInputContrasenia.text.toString() != binding.txtInputConfirmaContrasenia.text.toString()){
                Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(monedaSeleccionada.equals("")){
                Toast.makeText(context, "Seleccione una moneda para su cuenta", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            signUpViewModel.signUpUsuario(SignUpNuevoUsuario(
                binding.txtInputNombre.text.toString(),
                binding.txtInputApellido.text.toString(),
                binding.txtInputEmail.text.toString(),
                binding.txtInputContrasenia.text.toString(),
                monedaSeleccionada
            )

            )

        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                var codigo: String? = ""
                launch {
                    signUpViewModel.currenciesState.collect { state ->
                        when (state) {
                            is UiState.Idle -> {}
                            is UiState.Loading -> {
                                Log.d("SIGNUP_FRAGMENT", "Cargando monedas...")
                            }

                            is UiState.Success -> {
                                if (state.data.isEmpty()) {
                                    val monedas = listOf(
                                        "USD - Dólar",
                                        "EUR - Euro",
                                        "CLP - Peso Chileno",
                                        "ARS - Peso Argentino"
                                    )

                                    val adapter = ArrayAdapter(
                                        requireContext(),
                                        android.R.layout.simple_list_item_1,
                                        monedas
                                    )
                                    binding.monedaDropdown.setAdapter(adapter)
                                } else {
                                    val monedasTexto = state.data.map { moneda ->
                                        "${moneda.codigo} - ${moneda.nombre}"
                                    }
                                    val adapter = ArrayAdapter(
                                        requireContext(),
                                        android.R.layout.simple_list_item_1,
                                        monedasTexto
                                    )
                                    binding.monedaDropdown.setAdapter(adapter)
                                }
                            }

                            is UiState.Error -> {
                                Log.e("SIGNUP_FRAGMENT", "Error: ${state.message}")
                            }
                        }
                    }
                }

                launch {
                    signUpViewModel.signUpEvent.collect { state ->
                        when (state) {
                            is UiState.Idle -> {}
                            is UiState.Loading -> {
                                Log.d("SIGNUP_FRAGMENT", "Registrando usuario...")
                            }

                            is UiState.Success -> {
                                Log.i("SIGNUP_FRAGMENT", "USUARIO REGISTRADO")
                                Toast.makeText(context, state.data.mensaje, Toast.LENGTH_SHORT).show()
                                findNavController().navigate(R.id.action_signin_to_login)

                            }

                            is UiState.Error -> {
                                Log.e("SIGNUP_FRAGMENT", "Error: ${state.message}")
                            }
                        }

                    }

                }
            }
        }

    }


}