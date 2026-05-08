package com.example.listacomprasinteligente

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.listacomprasinteligente.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadData()

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun loadData() {
        val name = intent.getStringExtra(Constants.EXTRA_NAME).orEmpty()
        val availableQuantity = intent.getIntExtra(Constants.EXTRA_AVAILABLE_QTY, 0)
        val unit = intent.getStringExtra(Constants.EXTRA_UNIT).orEmpty()
        val category = intent.getStringExtra(Constants.EXTRA_CATEGORY).orEmpty()
        val imageName = intent.getStringExtra(Constants.EXTRA_IMAGE_NAME).orEmpty()

        binding.textProductName.text = name
        binding.textProductQuantity.text = getString(R.string.available_quantity_label, availableQuantity, unit)
        binding.textProductCategory.text = getString(R.string.category_label, category)
        binding.imageProductDetail.setImageResource(resolveDrawable(imageName))
    }

    private fun resolveDrawable(imageName: String): Int {
        val resourceId = resources.getIdentifier(imageName, "drawable", packageName)
        return if (resourceId != 0) resourceId else R.drawable.shopping_logo
    }
}
