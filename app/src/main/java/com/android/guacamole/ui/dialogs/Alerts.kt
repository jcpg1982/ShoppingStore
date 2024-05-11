package com.android.guacamole.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.android.guacamole.R
import com.android.guacamole.databinding.AlertDialogBinding

class Alerts(context: Context) {

    private val binding: AlertDialogBinding = AlertDialogBinding.inflate(LayoutInflater.from(context))
    private val dialog: Dialog = AlertDialog.Builder(context)
        .setView(binding.root)
        .setCancelable(false)
        .create()

    init {
        binding.btnRegister.setOnClickListener {
            showDialog(false, "")
        }
    }

    fun showDialog(show: Boolean, message: String) {
        if (show) {
            if (!dialog.isShowing) dialog.show()
            binding.messageTextView.text = message
        } else {
            if (dialog.isShowing) dialog.dismiss()
        }
    }
}