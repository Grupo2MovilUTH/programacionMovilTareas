package com.example.a24_kotlin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a24_kotlin.Modelo.signaturess

class CardView_CustomAdapter(
    private val context: Context,
    private val listaFirmas: List<signaturess>
) : RecyclerView.Adapter<CardView_CustomAdapter.SignatureViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SignatureViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_row_cardview, parent, false)
        return SignatureViewHolder(view)
    }

    override fun onBindViewHolder(holder: SignatureViewHolder, position: Int) {
        val item = listaFirmas[position]
        holder.signatureDescription.text = item.descripcion

        // Convertir byte[] a Bitmap
        val signatureBytes = item.firma
        if (signatureBytes != null) {
            val signatureBitmap = BitmapFactory.decodeByteArray(signatureBytes, 0, signatureBytes.size)
            holder.signatureImage.setImageBitmap(signatureBitmap)
        }
    }

    override fun getItemCount(): Int {
        return listaFirmas.size
    }

    // Clase ViewHolder interna
    class SignatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val signatureImage: ImageView = itemView.findViewById(R.id.signatureImage)
        val signatureDescription: TextView = itemView.findViewById(R.id.signatureDescription)
    }
}
