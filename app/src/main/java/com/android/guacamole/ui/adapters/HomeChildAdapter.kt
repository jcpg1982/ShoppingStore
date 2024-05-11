package com.android.guacamole.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.guacamole.data.dataBase.entity.ProductEntity
import com.android.guacamole.data.models.pojo.ProductWithStoreAndCategory
import com.android.guacamole.databinding.RowHomeChildBinding
import com.android.guacamole.utils.Utils.formatDecimal
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class HomeChildAdapter(
    var dataList: List<ProductWithStoreAndCategory>,
    private val select: (ProductEntity) -> Unit
) : RecyclerView.Adapter<HomeChildAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(RowHomeChildBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    inner class ViewHolder(private val binding: RowHomeChildBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ProductWithStoreAndCategory) {
            binding.run {
                Glide.with(itemView.context).load(item.product.imageProduct).centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(ivImageProduct)
                txtNameProduct.text = item.product.nameProduct
                txtPrice.text = "S/ ${item.product.price.formatDecimal}"
                btnAddCart.setOnClickListener { select(item.product) }
            }
        }
    }
}