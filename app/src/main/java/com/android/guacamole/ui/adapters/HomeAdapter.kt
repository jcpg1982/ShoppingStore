package com.android.guacamole.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.guacamole.data.dataBase.entity.ProductEntity
import com.android.guacamole.data.models.CategoryWithProduct
import com.android.guacamole.databinding.RowHomeBinding

class HomeAdapter(
    var dataList: List<CategoryWithProduct>,
    private val select: (ProductEntity) -> Unit
) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    private val TAG = HomeAdapter::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(RowHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    inner class ViewHolder(private val binding: RowHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CategoryWithProduct) {
            binding.run {
                txtCategory.text = item.category?.nameCategory.orEmpty()
                item.listProducts?.let {
                    val child = HomeChildAdapter(it, select = { data ->
                        select(data)
                    })
                    val layoutManager = GridLayoutManager(this.root.context, 2)
                    recycler.setHasFixedSize(true)
                    recycler.layoutManager = layoutManager
                    recycler.adapter = child
                }
            }
        }
    }
}