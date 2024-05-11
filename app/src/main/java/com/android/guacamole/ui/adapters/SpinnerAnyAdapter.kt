package com.android.guacamole.ui.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.android.guacamole.data.enums.TypePayment

class SpinnerAnyAdapter<T>(context: Context, res: Int, private val dataList: List<T>) :
    ArrayAdapter<T>(context, res) {

    override fun getCount(): Int {
        return dataList.size
    }

    override fun getItem(position: Int): T {
        return dataList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val objects = dataList[position]
        val label: TextView = super.getView(position, convertView, parent) as TextView
        when (objects) {
            is TypePayment -> label.text = objects.typePayment
        }
        return label
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val objects = dataList[position]
        val label: TextView = super.getDropDownView(position, convertView, parent) as TextView
        when (objects) {
            is TypePayment -> {
                label.text = objects.typePayment
            }
        }
        return label
    }
}