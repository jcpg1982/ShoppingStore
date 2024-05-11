package com.android.guacamole.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.guacamole.data.models.ProductReport
import com.android.guacamole.databinding.RowFavoriteBinding
import com.android.guacamole.utils.Utils.formatDecimal
import com.android.guacamole.utils.Utils.formatQuantity

class FavoriteAdapter(
    var dataList: List<ProductReport>
) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    private val TAG = FavoriteAdapter::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(RowFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    inner class ViewHolder(private val binding: RowFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ProductReport) {
            binding.run {
                txtNameProduct.text = item.nameProduct
                txtQuantity.text = item.cantidadComprada.formatQuantity
                txtAmount.text = "S/ ${item.totalGastado.formatDecimal}"
            }
        }
    }
}