package com.android.guacamole.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.Reader
import java.io.StringWriter
import java.io.Writer
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object Utils {

    val dateCalendar get() = Calendar.getInstance(Locale.getDefault())
    val date get() = dateCalendar.time
    val String.dateFormat get() = SimpleDateFormat(this, Locale.getDefault())

    val generateUnique: String
        get() {
            val dateFormat = "yyyyMMddHHmmssSSS".dateFormat
            return dateFormat.format(date)
        }

    val dateTime: String
        get() {
            val dateFormat = "yyyy/MM/dd-HH:mm:ss".dateFormat
            return dateFormat.format(date)
        }

    val Double?.orZero: Double
        get() = this ?: 0.00

    val Double?.formatDecimal: String
        get() {
            val dfs = DecimalFormatSymbols(Locale.ROOT)
            val format = DecimalFormat("###,###,##0.00", dfs)
            return format.format(orZero)
        }

    val Double?.formatQuantity: String
        get() = orZero.formatDecimal.replace(".00", "").replace(".0", "")

    fun readJsonFromRaw(context: Context, resourceId: Int): String {
        val inputStream = context.resources.openRawResource(resourceId)
        val writer: Writer = StringWriter()
        val buffer = CharArray(1024)
        inputStream.use { stream ->
            val reader: Reader = BufferedReader(InputStreamReader(stream, "UTF-8"))
            var n: Int
            while (reader.read(buffer).also { n = it } != -1) {
                writer.write(buffer, 0, n)
            }
        }
        return writer.toString()
    }

    fun <T> String?.stringToObject(clase: Class<T>): T? = if (isNullOrEmpty()) null
    else {
        try {
            Gson().fromJson(this, clase)
        } catch (e: Exception) {
            null
        }
    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        this.hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}