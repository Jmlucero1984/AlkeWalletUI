package com.jmlucero.alkewallet.ui.home

import android.R
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jmlucero.alkewallet.data.model.Transaccion
import com.jmlucero.alkewallet.data.model.TransaccionSimple
import com.jmlucero.alkewallet.data.model.Usuario
import com.jmlucero.alkewallet.databinding.ListItemBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class TransaccionAdapter : RecyclerView.Adapter<TransaccionAdapter.TransaccionViewHolder>() {

    private var transacciones: List<TransaccionSimple> = emptyList()

    fun submitList(lista: List<TransaccionSimple>) {
        transacciones = lista
        notifyDataSetChanged()
    }

    inner class TransaccionViewHolder(
        private val binding: ListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(transaccionSimple: TransaccionSimple) {
            var url = transaccionSimple.usuario_avatar//.substring(1, transaccionSimple.usuario_avatar.length-1)
            binding.itemName.text = transaccionSimple.usuario_nombre

Log.i("DATETIME",transaccionSimple.fecha_creacion.toString())
            binding.itemEmail.text = transaccionSimple.fecha_creacion.date.split(" ")[0]+" | "+transaccionSimple.tipo_transaccion
            if(transaccionSimple.tipo_transaccion.equals("DEPOSITO")) {
                binding.itemId.text = "+"+transaccionSimple.cantidad_efectiva.toString()

            } else if (transaccionSimple.tipo_transaccion.equals("RETIRO")) {
                binding.itemId.text = "-"+transaccionSimple.cantidad_efectiva.toString()
            } else {
                binding.itemId.text = transaccionSimple.cantidad_efectiva.toString()
            }

            Picasso.get()
                .load(url)
                .placeholder(com.jmlucero.alkewallet.R.drawable.profile_svgrepo_com)
                .error(com.jmlucero.alkewallet.R.drawable.profile_svgrepo_com)
                .fit()
                .centerCrop()
                .into(binding.avatarUsuario)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransaccionViewHolder {
        val binding = ListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TransaccionViewHolder(binding)
    }

    override fun getItemCount(): Int = transacciones.size

    override fun onBindViewHolder(holder: TransaccionViewHolder, position: Int) {
        holder.bind(transacciones[position])
    }
}