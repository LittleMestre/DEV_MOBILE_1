package com.example.listacomprasinteligente

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.listacomprasinteligente.databinding.ItemProductBinding

class ProductAdapter(
    private var products: List<Product>,
    private val onBuyChanged: (Product, Boolean) -> Unit,
    private val onDetailsClicked: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        val context = holder.itemView.context
        val binding = holder.binding

        val imageId = context.resources.getIdentifier(product.imageName, "drawable", context.packageName)
        binding.imageProduct.setImageResource(if (imageId != 0) imageId else R.drawable.shopping_logo)
        binding.textName.text = product.name
        binding.textQuantity.text = context.getString(R.string.available_quantity_label, product.availableQuantity, product.unit)
        binding.textCategory.text = context.getString(R.string.category_label, product.category)

        binding.checkBought.setOnCheckedChangeListener(null)
        binding.checkBought.isChecked = ProductRepository.isInCart(product)
        binding.checkBought.setOnCheckedChangeListener { _, isChecked ->
            onBuyChanged(product, isChecked)
        }

        binding.btnDetails.setOnClickListener {
            onDetailsClicked(product)
        }
    }

    override fun getItemCount(): Int = products.size

    fun updateList(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }
}
