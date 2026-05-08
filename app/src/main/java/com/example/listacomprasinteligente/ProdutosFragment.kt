package com.example.listacomprasinteligente

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listacomprasinteligente.databinding.FragmentProdutosBinding

class ProdutosFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentProdutosBinding? = null
    private val binding get() = _binding!!

    private var selectedCategory = "Todos"
    private lateinit var productAdapter: ProductAdapter
    private lateinit var securityPreferences: SecurityPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("CICLO", "ProdutosFragment - onCreate chamado")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d("CICLO", "ProdutosFragment - onCreateView chamado")
        _binding = FragmentProdutosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        securityPreferences = SecurityPreferences(requireContext())

        configureButtons()
        configureRecyclerView()
        updateHeader()
        updateCategoryButtons()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_all -> selectCategory("Todos")
            R.id.btn_fruit -> selectCategory("Frutas")
            R.id.btn_cleaning -> selectCategory("Limpeza")
            R.id.btn_drink -> selectCategory("Bebidas")
            R.id.btn_preserved -> selectCategory("Conservados")
        }
    }

    private fun configureButtons() {
        binding.btnAll.setOnClickListener(this)
        binding.btnFruit.setOnClickListener(this)
        binding.btnCleaning.setOnClickListener(this)
        binding.btnDrink.setOnClickListener(this)
        binding.btnPreserved.setOnClickListener(this)
    }

    private fun configureRecyclerView() {
        productAdapter = ProductAdapter(
            ProductRepository.getVisibleProducts(selectedCategory),
            onBuyChanged = { product, isChecked -> handleBuyChanged(product, isChecked) },
            onDetailsClicked = { product -> openDetails(product) }
        )

        binding.recyclerProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerProducts.adapter = productAdapter
    }

    private fun selectCategory(category: String) {
        selectedCategory = category
        updateCategoryButtons()
        updateProductList()
        Toast.makeText(requireContext(), getString(R.string.filter_selected, category), Toast.LENGTH_SHORT).show()
    }

    private fun handleBuyChanged(product: Product, isChecked: Boolean) {
        if (isChecked) {
            if (ProductRepository.isInCart(product)) return

            if (product.availableQuantity <= 0) {
                Toast.makeText(requireContext(), getString(R.string.no_stock, product.name), Toast.LENGTH_SHORT).show()
                updateProductList()
            } else {
                showQuantityDialog(product)
            }
        } else {
            val amount = ProductRepository.cartQuantities[product.id] ?: 0
            ProductRepository.removeFromCart(product)
            updateHeader()
            updateProductList()
            Toast.makeText(requireContext(), getString(R.string.item_unchecked, product.name, amount, product.unit), Toast.LENGTH_SHORT).show()
        }
    }

    private fun showQuantityDialog(product: Product) {
        val options = (1..product.availableQuantity).map { it.toString() }.toTypedArray()

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.choose_quantity_title, product.name))
            .setItems(options) { _, which ->
                val selectedAmount = options[which].toInt()
                ProductRepository.addToCart(product, selectedAmount)
                updateHeader()
                updateProductList()
                Toast.makeText(
                    requireContext(),
                    getString(R.string.item_checked, product.name, selectedAmount, product.unit),
                    Toast.LENGTH_SHORT
                ).show()
            }
            .setOnCancelListener {
                updateProductList()
            }
            .show()
    }

    private fun openDetails(product: Product) {
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra(Constants.EXTRA_NAME, product.name)
        intent.putExtra(Constants.EXTRA_AVAILABLE_QTY, product.availableQuantity)
        intent.putExtra(Constants.EXTRA_UNIT, product.unit)
        intent.putExtra(Constants.EXTRA_CATEGORY, product.category)
        intent.putExtra(Constants.EXTRA_IMAGE_NAME, product.imageName)
        startActivity(intent)
    }

    private fun updateHeader() {
        val userName = securityPreferences.getString(Constants.KEY_USER_NAME).ifEmpty { "Aluno" }
        binding.textGreeting.text = getString(R.string.greeting_text, userName)

        val cartLines = ProductRepository.getCartLines(requireContext())
        binding.textLastList.text = if (cartLines.isEmpty()) {
            getString(R.string.cart_empty)
        } else {
            cartLines.joinToString(separator = "\n")
        }
    }

    private fun updateProductList() {
        productAdapter.updateList(ProductRepository.getVisibleProducts(selectedCategory))
    }

    private fun updateCategoryButtons() {
        val selectedColor = ContextCompat.getColor(requireContext(), R.color.green_primary)
        val normalColor = ContextCompat.getColor(requireContext(), R.color.gray_light)
        val selectedTextColor = ContextCompat.getColor(requireContext(), R.color.white)
        val normalTextColor = ContextCompat.getColor(requireContext(), R.color.text_dark)

        val buttonCategoryMap = mapOf(
            binding.btnAll to "Todos",
            binding.btnFruit to "Frutas",
            binding.btnCleaning to "Limpeza",
            binding.btnDrink to "Bebidas",
            binding.btnPreserved to "Conservados"
        )

        for ((button, category) in buttonCategoryMap) {
            val isSelected = category == selectedCategory
            button.setBackgroundColor(if (isSelected) selectedColor else normalColor)
            button.setTextColor(if (isSelected) selectedTextColor else normalTextColor)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("CICLO", "ProdutosFragment - onStart chamado")
    }

    override fun onResume() {
        super.onResume()
        Log.d("CICLO", "ProdutosFragment - onResume chamado")
        updateHeader()
        updateProductList()
    }

    override fun onPause() {
        super.onPause()
        Log.d("CICLO", "ProdutosFragment - onPause chamado")
    }

    override fun onStop() {
        super.onStop()
        Log.d("CICLO", "ProdutosFragment - onStop chamado")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("CICLO", "ProdutosFragment - onDestroyView chamado")
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("CICLO", "ProdutosFragment - onDestroy chamado")
    }
}
