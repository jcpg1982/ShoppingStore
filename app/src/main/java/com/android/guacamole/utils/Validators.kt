package com.android.guacamole.utils

import com.google.android.material.textfield.TextInputLayout

object Validators {

    val TextInputLayout?.isEmpty: Boolean
        get() = this?.editText?.text.isNullOrEmpty()

    val TextInputLayout?.validateNotEmpty: Boolean
        get() {
            return if (isEmpty) {
                this?.error = "Campo obligatorio"
                false
            } else {
                this?.error = null
                true
            }
        }

    fun TextInputLayout?.areContentsTheSame(value: String): Boolean {
        return if (validateNotEmpty) {
            val content = this?.editText?.text.toString()
            if (content != value) {
                this?.error = "Los campos no son iguales"
                false
            } else {
                this?.error = null
                true
            }
        } else {
            validateNotEmpty
        }
    }

    fun TextInputLayout?.validateSize(size: Int): Boolean {
        return if (validateNotEmpty) {
            val content = this?.editText?.text.toString()
            if (content.length !in size..size) {
                this?.error = "El campo debe tener exactamente $size dígitos"
                false
            } else {
                this?.error = null
                true
            }
        } else {
            validateNotEmpty
        }
    }
}