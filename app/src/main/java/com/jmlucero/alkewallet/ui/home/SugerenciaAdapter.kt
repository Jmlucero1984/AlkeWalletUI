package com.jmlucero.alkewallet.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jmlucero.alkewallet.R
import com.jmlucero.alkewallet.data.model.entity.Usuario
import com.jmlucero.alkewallet.databinding.ItemSugerenciaBinding
import com.squareup.picasso.Picasso

class SugerenciaAdapter(
    private val onItemClick: (Usuario) -> Unit
) : ListAdapter<Usuario, SugerenciaAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(val binding: ItemSugerenciaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(usuario: Usuario) {
            binding.nombreText.text = "${usuario.nombre} ${usuario.apellido}"
            binding.emailText.text = usuario.email

            Picasso.get()
                .load(usuario.avatar_url)
                .placeholder(R.drawable.profile_svgrepo_com)
                .error(R.drawable.profile_svgrepo_com)
                .into(binding.avatarImg)

            binding.root.setOnClickListener {
                onItemClick(usuario)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSugerenciaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<Usuario>() {
        override fun areItemsTheSame(oldItem: Usuario, newItem: Usuario): Boolean {
            return oldItem.email == newItem.email
        }

        override fun areContentsTheSame(oldItem: Usuario, newItem: Usuario): Boolean {
            return oldItem == newItem
        }
    }
}