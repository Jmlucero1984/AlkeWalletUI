package com.jmlucero.alkewallet.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jmlucero.alkewallet.data.model.Usuario
import com.jmlucero.alkewallet.databinding.ListItemBinding

class UsuarioAdapter : RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>() {

    private var usuarios: List<Usuario> = emptyList()

    fun submitList(lista: List<Usuario>) {
        usuarios = lista
        notifyDataSetChanged()
    }

    inner class UsuarioViewHolder(
        private val binding: ListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(usuario: Usuario) {
            binding.itemName.text = usuario.nombre
            binding.itemEmail.text = usuario.email
            binding.itemId.text = usuario.usuario_id.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val binding = ListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return UsuarioViewHolder(binding)
    }

    override fun getItemCount(): Int = usuarios.size

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        holder.bind(usuarios[position])
    }
}