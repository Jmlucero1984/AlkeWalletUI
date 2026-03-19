package com.jmlucero.alkewallet

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.jmlucero.alkewallet.data.model.Transferencia
import com.jmlucero.alkewallet.data.model.UiState
import com.jmlucero.alkewallet.databinding.FragmentEnviarDineroBinding
import com.jmlucero.alkewallet.ui.home.SugerenciaAdapter
import com.jmlucero.alkewallet.viewmodel.SharedViewModel
import com.jmlucero.alkewallet.viewmodel.UserViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode


class EnviarDineroFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentEnviarDineroBinding? = null
    private val binding get() = _binding!!

    //private lateinit var userViewModel: UserViewModel
    private var ratio_a_dolar_usuario_emisor: Double? = null
    private var codigo_moneda_emisor: String = ""
    private var codigo_moneda_destino: String = ""
    private var ratio_a_dolar_usuario_destino: Double? = null
    private var email_usuario_destino: String = ""
    private var balanceActual: BigDecimal? = null
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var adapter: SugerenciaAdapter
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



    fun hideAllElements() {
        binding.usuarioDestinoContainer.isVisible = false
        binding.txtIngreseCantidadTitulo.isVisible = false
        binding.txtInpLayout.isVisible = false
        binding.monedaSelectionContainer.isVisible = false

        binding.notaTransTitulo.isVisible = false
        binding.notaTransfTextInpLayout.isVisible = false
    }

    fun showAllElements(showSeleccionTransferencia: Boolean = false) {
        binding.usuarioDestinoContainer.isVisible = true
        binding.txtIngreseCantidadTitulo.isVisible = true
        binding.txtInpLayout.isVisible = true
        binding.monedaSelectionContainer.isVisible = showSeleccionTransferencia

        binding.notaTransTitulo.isVisible = true
        binding.notaTransfTextInpLayout.isVisible = true
    }

    fun resetFields() {
        binding.ingreseCantidad.text=null
        binding.notaTransfInput.text = null
        binding.switchSelectionMoneda.isChecked = false
        binding.notaTransfInput.text?.clear()
        binding.ingreseEmail.requestFocus()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.homePageFragment)
        }
        adapter = SugerenciaAdapter { usuario ->
            binding.ingreseEmail.setText(usuario.email)
            binding.recyclerSugerencias.visibility = View.GONE
        }
        binding.recyclerSugerencias.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerSugerencias.adapter = adapter

        fun TextInputEditText.textChanges(): Flow<String> = callbackFlow {
            val watcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    trySend(s.toString())
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            }

            addTextChangedListener(watcher)

            awaitClose { removeTextChangedListener(watcher) }
        }

        hideAllElements()


        binding.btnBuscarUsuario.setOnClickListener {
            val email = binding.ingreseEmail.text.toString()
            userViewModel.cargarUsuario(email)
        }

        binding.btnEnviarTransferencia.setOnClickListener {
            var factorConversion: Double = 1.0
            /*
             TRANSFERENCIA EN MONEDA DESTINO
             factorConversion = ratio_a_dolar_destino/ratio_a_dolar_origen
             cantidadEfectia = monto*factorConversion


             TRANSFERENCIA EN MONEDA ORIGEN
             factorConversion = ratio_a_dolar_origen/ratio_a_dolar_destino
             cantidadEfectia = monto*factorConversion

              */
            val cantidadIngresada = binding.ingreseCantidad.text.toString().toDouble()
            var montoEfectivo: BigDecimal = BigDecimal("0.00")
            if (binding.monedaSelectionContainer.isVisible) {
                if (binding.switchSelectionMoneda.isChecked) {
                    // EN MONEDA DESTINO  ///
                    ratio_a_dolar_usuario_emisor?.let { emisor ->
                        ratio_a_dolar_usuario_destino?.let { destino ->
                            factorConversion = destino / emisor
                            montoEfectivo =
                                BigDecimal(cantidadIngresada * factorConversion).setScale(
                                    2,
                                    RoundingMode.HALF_UP
                                )
                        }
                    }
                    if (montoEfectivo > balanceActual) {
                        mostrarAlertaPorFondos("Para transferir " + cantidadIngresada + " " + codigo_moneda_destino + " se requieren " + montoEfectivo + " " + codigo_moneda_emisor)
                    } else {
                        mostrarConfirmacion("Transfiere " + cantidadIngresada + " " + codigo_moneda_destino + " ( -" + montoEfectivo + " " + codigo_moneda_emisor + " )") {
                            userViewModel.transferir(
                                Transferencia(
                                    email_usuario_destino,
                                    binding.ingreseCantidad.text.toString(),
                                    "REALIZA TCDM MMD",
                                    binding.notaTransfInput.text.toString(),
                                )
                            )
                        }
                    }
                } else {
                    // EN MONEDA ORIGEN  ///
                    ratio_a_dolar_usuario_emisor?.let { emisor ->
                        ratio_a_dolar_usuario_destino?.let { destino ->
                            factorConversion = emisor / destino
                            montoEfectivo =
                                BigDecimal(cantidadIngresada * factorConversion).setScale(
                                    2,
                                    RoundingMode.HALF_UP
                                )
                        }
                    }
                    if (BigDecimal(cantidadIngresada).setScale(
                            2,
                            RoundingMode.HALF_UP
                        ) > balanceActual
                    ) {
                        binding.ingreseCantidad.setError("No hay fondos suficientes")
                    } else {
                        mostrarConfirmacion("Transfiere " + montoEfectivo + " " + codigo_moneda_destino + " ( -" + cantidadIngresada + " " + codigo_moneda_emisor + " )") {
                            userViewModel.transferir(
                                Transferencia(
                                    email_usuario_destino,
                                    binding.ingreseCantidad.text.toString(),
                                    "REALIZA TCDM MMO",
                                    binding.notaTransfInput.text.toString(),
                                )
                            )
                        }
                    }
                }
            } else {
                montoEfectivo = BigDecimal(cantidadIngresada).setScale(2, RoundingMode.HALF_UP)
                if (montoEfectivo > balanceActual) {
                    binding.ingreseCantidad.setError("No hay fondos suficientes")
                } else {
                    mostrarConfirmacion("Transfiere " + montoEfectivo + " " + codigo_moneda_destino) {
                        userViewModel.transferir(
                            Transferencia(
                                email_usuario_destino,
                                montoEfectivo.toString(),
                                "REALIZA TCIM",
                                binding.notaTransfInput.text.toString(),

                            )
                        )
                    }
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                var usuarioLogueadoMoneda: String = ""
                launch {
                    binding.ingreseEmail.textChanges()
                        .debounce(300)
                        .distinctUntilChanged()
                        .flatMapLatest { query ->
                            if (query.isBlank()) {
                                flowOf(emptyList())
                            } else {
                                userViewModel.buscarSugerencias(query)
                            }
                        }
                        .collect { usuarios ->

                            adapter.submitList(usuarios)
                            usuarios.forEach { usuario ->
                                Log.d("SUGERENCIAS", usuario.toString())
                            }

                            if (usuarios.isEmpty()) {
                                binding.recyclerSugerencias.visibility = View.GONE
                            } else {
                                binding.recyclerSugerencias.visibility = View.VISIBLE
                            }
                        }
                }
                launch {
                    userViewModel.transferenciaEvent.collect { state ->
                        when (state) {
                            is UiState.Idle -> {}
                            is UiState.Loading -> {
                                Log.d("ENVIAR_DINERO_FRAGMENT", "Enviando transferencia...")
                            }

                            is UiState.Success -> {
                                resetFields()
                                hideAllElements()
                                sharedViewModel.setMensaje(state.data.mensaje)
                                userViewModel.onTransferSuccess(
                                    state.data.nuevo_saldo.toString())
                                findNavController().navigate(R.id.action_back_to_home)



                                }


                            is UiState.Error -> {
                                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                                Log.e("ENVIAR_DINERO_FRAGMENT", "Error: ${state.message}")
                            }
                        }

                    }

                }
                launch {
                    userViewModel.usuarioDestinoEvent.collect { state ->
                        when (state) {
                            is UiState.Idle -> {}
                            is UiState.Loading -> {
                                Log.d("ENVIAR_DINERO_FRAGMENT", "Obteniendo usuario destino...")
                            }

                            is UiState.Success -> {
                                //  Toast.makeText(context,state.data.mensaje, Toast.LENGTH_LONG).show()

                                binding.nombreUsuarioDestino.text =
                                    state.data.nombre + " " + state.data.apellido
                                binding.emailUsuarioDestino.text = state.data.email
                                binding.infoCuentaDestino.text = state.data.moneda.nombre
                                Log.i(
                                    "INFORMACION",
                                    "MONEDA USUARIO LOGUEADO: " + usuarioLogueadoMoneda
                                )
                                Log.i(
                                    "INFORMACION",
                                    "MONEDA USUARIO ENCONTRADO: " + state.data.moneda.codigo
                                )
                                ratio_a_dolar_usuario_destino = state.data.moneda.ratio_a_usd
                                codigo_moneda_destino = state.data.moneda.codigo
                                email_usuario_destino = state.data.email

                                var url =
                                    state.data.avatar_url//.substring(1, state.data.avatar_url.length - 1)
                                Picasso.get()
                                    .load(url)
                                    .placeholder(R.drawable.profile_svgrepo_com)
                                    .error(R.drawable.profile_svgrepo_com)
                                    .fit()
                                    .centerCrop()
                                    .into(binding.avatarUsuarioDestino)

                                val showSeleccionTransferencia: Boolean =
                                    !usuarioLogueadoMoneda.equals(state.data.moneda.codigo)
                                showAllElements(showSeleccionTransferencia)

                            }

                            is UiState.Error -> {
                                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                                Log.e("ENVIAR_DINERO_FRAGMENT", "Error: ${state.message}")
                            }
                        }
                    }
                }


                launch {
                    userViewModel.usuarioConMoneda.collect { usuarioConMoneda ->
                        if (usuarioConMoneda != null) {
                            usuarioLogueadoMoneda = usuarioConMoneda.moneda.codigo
                            ratio_a_dolar_usuario_emisor = usuarioConMoneda.moneda.ratio_a_usd
                            codigo_moneda_emisor = usuarioConMoneda.moneda.codigo
                            balanceActual = BigDecimal(usuarioConMoneda.usuario.balance)
                            binding.nombreUsuario.text =
                                "${usuarioConMoneda.usuario.nombre} ${usuarioConMoneda.usuario.apellido}"
                            binding.emailUsuario.text =
                                usuarioConMoneda.usuario.email.toString()

                            var url =
                                usuarioConMoneda.usuario.avatar_url//.substring(1, usuarioConMoneda.usuario.avatar_url.length - 1)

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

    private fun mostrarConfirmacion(
        mensaje: String,
        onConfirm: () -> Unit
    ) {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirmar transferencia")
            .setMessage(mensaje)
            .setPositiveButton("Confirmar") { _, _ ->
                onConfirm()
            }
            .setNegativeButton("Corregir") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun mostrarAlertaPorFondos(mensaje: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Problema con la transferencia")
            .setMessage(mensaje)
            .setNegativeButton("Corregir") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    fun enviarDinero() {
        Toast.makeText(context, "CACA FRITA", Toast.LENGTH_LONG).show()
    }


}