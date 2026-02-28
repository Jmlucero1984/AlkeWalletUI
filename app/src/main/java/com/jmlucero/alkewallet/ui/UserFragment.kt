package com.jmlucero.alkewallet.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.jmlucero.alkewallet.R
import com.jmlucero.alkewallet.databinding.FragmentUserBinding
import com.jmlucero.alkewallet.viewmodel.UserViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupObservers()
        setupListeners()
    }
    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
    }

    @SuppressLint("SetTextI18n")
    private fun setupObservers() {
        // Observar usuario individual
        viewModel.user.observe(viewLifecycleOwner) { user ->
            user?.let {
                binding.tvUserInfo.text = """
                    ID: ${it.usuario_id}
                    Nombre: ${it.nombre}
                    Apellido: ${it.apellido}
                    Email: ${it.email}
                """.trimIndent()
            }
        }

        // Observar lista de usuarios
        viewModel.users.observe(viewLifecycleOwner) { users ->
            // Aquí puedes actualizar un RecyclerView
            binding.tvUsersCount.text = "Usuarios cargados: ${users.size}"
        }

        // Observar estado de carga
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Observar errores
        viewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            errorMsg?.let {
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_LONG).show()
                viewModel.clearError()
            }
        }
    }

    private fun setupListeners() {
        binding.btnGetUser.setOnClickListener {
            val userId = binding.etUserId.text.toString().toIntOrNull()
            if (userId != null) {
                viewModel.getUserById(userId)
            } else {
                Toast.makeText(requireContext(), "Ingresa un ID válido", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnGetAllUsers.setOnClickListener {
            viewModel.getAllUsers()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}