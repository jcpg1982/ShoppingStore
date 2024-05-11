package com.android.guacamole.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.android.guacamole.R

class Loading(context: Context) {
    private val dialog: Dialog

    init {
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        builder.setView(inflater.inflate(R.layout.loading_dialog, null))
        builder.setCancelable(false)

        dialog = builder.create()
    }

    fun showDialog(show: Boolean, message: String) {
        if (show) {
            if (!dialog.isShowing)
                dialog.show()
            dialog.findViewById<TextView>(R.id.messageTextView)?.text = message
        } else {
            if (dialog.isShowing)
                dialog.dismiss()
        }
    }
}