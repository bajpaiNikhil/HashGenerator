package com.example.hashgenerator

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class UserHashAdapter(val userHashList : ArrayList<userHash>): RecyclerView.Adapter<UserHashAdapter.ItemAdapter>() {
    class ItemAdapter(view : View) : RecyclerView.ViewHolder(view) {
        val CName = view.findViewById<TextView>(R.id.GuestNameT)
        val Cemail = view.findViewById<TextView>(R.id.GuestEmailT)
        val CPhone = view.findViewById<TextView>(R.id.GuestPhoneNoT)
        val butonClick = view.findViewById<Button>(R.id.buttonCopy)
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : ItemAdapter {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
        return ItemAdapter(view)
    }

    override fun onBindViewHolder(holder : ItemAdapter, position : Int) {
        val currentItem = userHashList[position]
        holder.CName.text = currentItem.userInputText
        holder.Cemail.text = currentItem.userInputAlgorithm
        holder.CPhone.text = currentItem.userOutputHash

        holder.butonClick.setOnClickListener {
            Log.d("adapter" , "Buton is clicked")
            val clipboardManager = holder.itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("Encrypted Text." , holder.CPhone.text)
            clipboardManager.setPrimaryClip(clipData)
            val snackBar = Snackbar.make(holder.itemView, "item Copied" ,Snackbar.LENGTH_LONG)
            snackBar.show()
        }
    }

    override fun getItemCount() : Int {
        return userHashList.size
    }
}