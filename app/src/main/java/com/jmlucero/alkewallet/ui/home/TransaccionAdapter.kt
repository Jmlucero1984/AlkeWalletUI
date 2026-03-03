package com.jmlucero.alkewallet.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jmlucero.alkewallet.data.model.Transaccion
import com.jmlucero.alkewallet.data.model.Usuario
import com.jmlucero.alkewallet.databinding.ListItemBinding

class TransaccionAdapter : RecyclerView.Adapter<TransaccionAdapter.TransaccionViewHolder>() {

    private var transacciones: List<Transaccion> = emptyList()

    fun submitList(lista: List<Transaccion>) {
        transacciones = lista
        notifyDataSetChanged()
    }

    inner class TransaccionViewHolder(
        private val binding: ListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(transaccion: Transaccion) {
            binding.itemName.text = transaccion.cuenta_origen_nombre+"/"+transaccion.cuenta_destino_nombre
            binding.itemEmail.text = transaccion.tipo_transaccion+"/"+transaccion.fecha_creacion.date
            binding.itemId.text = transaccion.monto.toString()
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