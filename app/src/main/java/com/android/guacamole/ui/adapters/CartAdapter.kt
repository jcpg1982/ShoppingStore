package com.android.guacamole.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.guacamole.data.models.DetailProduct
import com.android.guacamole.databinding.RowCartBinding
import com.android.guacamole.utils.Utils.formatDecimal
import com.android.guacamole.utils.Utils.formatQuantity
import com.android.guacamole.utils.Utils.orZero

class CartAdapter(
    var dataList: List<DetailProduct>,
    private val delete: (DetailProduct) -> Unit,
    private val add: (DetailProduct) -> Unit,
    private val remove: (DetailProduct) -> Unit
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    private val TAG = CartAdapter::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(RowCartBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = dataList.size

    inner class ViewHolder(private val binding: RowCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DetailProduct) {
            binding.run {
                txtNameProduct.text = item.nameProduct
                txtQuantity.text = item.quantity.formatQuantity
                val total = item.quantity.orZero * item.price.orZero
                txtTotal.text = "S/ ${total.formatDecimal}"
                ivDelete.setOnClickListener { delete(item) }
                ivAdd.setOnClickListener { add(item) }
                ivRemove.setOnClickListener {
                    if (item.quantity.orZero > 1) remove(item)
                }
            }
        }
    }
}