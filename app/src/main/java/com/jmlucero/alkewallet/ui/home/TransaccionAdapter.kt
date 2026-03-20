package com.jmlucero.alkewallet.ui.home

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.jmlucero.alkewallet.R
import com.jmlucero.alkewallet.data.model.entity.TransaccionSimple
import com.jmlucero.alkewallet.databinding.ListItemBinding
import com.squareup.picasso.Picasso




class TransaccionAdapter : RecyclerView.Adapter<TransaccionAdapter.TransaccionViewHolder>() {

    private var transacciones: List<TransaccionSimple> = emptyList()
    private  var context: Context? = null


    fun submitList(lista: List<TransaccionSimple>, _context: Context?) {
        transacciones = lista
        context=_context
        notifyDataSetChanged()
    }

    inner class TransaccionViewHolder(
        private val binding: ListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {


        @SuppressLint("SetTextI18n")
        fun bind(transaccionSimple: TransaccionSimple) {
            var url = transaccionSimple.usuario_avatar//.substring(1, transaccionSimple.usuario_avatar.length-1)
            binding.itemName.text = transaccionSimple.usuario_nombre
            if(!transaccionSimple.comentario.isNullOrEmpty() && transaccionSimple.comentario!=""){
                binding.itemDetalle.setOnClickListener {
                    AlertDialog.Builder(context)
                        .setTitle(R.string.detalleTransaccion)
                        .setMessage(transaccionSimple.comentario)
                        .setPositiveButton(R.string.ok, null)
                        .show()
                }
                binding.itemDetalle.text = transaccionSimple.tipo_transaccion+" (+info)"
            } else {
                binding.itemDetalle.text = transaccionSimple.tipo_transaccion
            }


            Log.i("DATETIME",transaccionSimple.fecha_creacion.toString())

            binding.itemEmail.text = transaccionSimple.fecha_creacion.date.split(" ")[0]
            if(transaccionSimple.tipo_transaccion.equals("DEPOSITO")||
                transaccionSimple.tipo_transaccion.equals("RECIBE TCDM MMO") ||
                transaccionSimple.tipo_transaccion.equals("RECIBE TCDM MMD") ||
                transaccionSimple.tipo_transaccion.equals("RECIBE TCIM")


                ) {
                binding.iconSaleDinero.isVisible=false
                binding.iconEntraDinero.isVisible=true
                binding.itemId.text = "+${transaccionSimple.cantidad_efectiva}"

            } else if (transaccionSimple.tipo_transaccion.equals("RETIRO") ||
                transaccionSimple.tipo_transaccion.equals("REALIZA TCDM MMO") ||
                transaccionSimple.tipo_transaccion.equals("REALIZA TCDM MMD") ||
                transaccionSimple.tipo_transaccion.equals("REALIZA TCIM")

                ) {
                binding.iconSaleDinero.isVisible=true
                binding.iconEntraDinero.isVisible=false
                binding.itemId.text ="-${transaccionSimple.cantidad_efectiva}"
            } else {
                binding.itemId.text = transaccionSimple.cantidad_efectiva.toString()
                binding.iconSaleDinero.isVisible=false
                binding.iconEntraDinero.isVisible=false
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