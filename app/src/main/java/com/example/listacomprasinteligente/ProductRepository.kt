package com.example.listacomprasinteligente

import android.content.Context

object ProductRepository {
    private lateinit var securityPreferences: SecurityPreferences

    var products: MutableList<Product> = mutableListOf()
        private set

    var cartQuantities: MutableMap<String, Int> = mutableMapOf()
        private set

    fun initialize(context: Context) {
        securityPreferences = SecurityPreferences(context)
        cartQuantities = loadCart()
        products = loadProducts()
    }

    fun getVisibleProducts(category: String): List<Product> {
        return if (category == "Todos") products else products.filter { it.category == category }
    }

    fun isInCart(product: Product): Boolean {
        return cartQuantities.containsKey(product.id)
    }

    fun addToCart(product: Product, amount: Int) {
        product.availableQuantity -= amount
        cartQuantities[product.id] = amount
        saveState()
    }

    fun removeFromCart(product: Product) {
        val amount = cartQuantities[product.id] ?: return
        product.availableQuantity += amount
        cartQuantities.remove(product.id)
        saveState()
    }

    fun getCartLines(context: Context): List<String> {
        return products
            .filter { cartQuantities.containsKey(it.id) }
            .map { product ->
                val amount = cartQuantities[product.id] ?: 0
                context.getString(R.string.cart_line, product.name, amount, product.unit)
            }
    }

    private fun saveState() {
        securityPreferences.storeString(Constants.KEY_CART, serializeCart())
        securityPreferences.storeString(Constants.KEY_PRODUCTS, serializeProducts())
    }

    private fun loadProducts(): MutableList<Product> {
        val savedProducts = securityPreferences.getString(Constants.KEY_PRODUCTS)
        return if (savedProducts.isEmpty()) {
            createDefaultProducts().toMutableList()
        } else {
            deserializeProducts(savedProducts).toMutableList()
        }
    }

    private fun loadCart(): MutableMap<String, Int> {
        val savedCart = securityPreferences.getString(Constants.KEY_CART)
        if (savedCart.isEmpty()) return mutableMapOf()

        return savedCart
            .split("|||")
            .filter { it.isNotBlank() }
            .mapNotNull { line ->
                val parts = line.split("=")
                if (parts.size == 2) {
                    parts[1].toIntOrNull()?.let { quantity -> parts[0] to quantity }
                } else {
                    null
                }
            }
            .toMap()
            .toMutableMap()
    }

    private fun createDefaultProducts(): List<Product> {
        return listOf(
            Product("maca", "Maçã", 6, "unidades", "Frutas", "maca"),
            Product("banana", "Banana", 12, "unidades", "Frutas", "banana"),
            Product("uva", "Uva", 1, "cacho", "Frutas", "uva"),
            Product("laranja", "Laranja", 8, "unidades", "Frutas", "laranja"),
            Product("detergente", "Detergente", 3, "frascos", "Limpeza", "detergente"),
            Product("rodo", "Rodo", 1, "unidade", "Limpeza", "rodo"),
            Product("qboa", "Qboa", 2, "litros", "Limpeza", "qboa"),
            Product("desinfetante", "Desinfetante", 2, "frascos", "Limpeza", "desinfetante"),
            Product("leite", "Leite", 2, "caixas", "Bebidas", "leite"),
            Product("monster", "Monster", 4, "latas", "Bebidas", "monster"),
            Product("coca", "Coca", 2, "garrafas", "Bebidas", "coca"),
            Product("whisky", "Whisky", 1, "garrafa", "Bebidas", "whisky"),
            Product("arroz", "Arroz", 1, "pacote", "Conservados", "arroz"),
            Product("carne", "Carne", 2, "kg", "Conservados", "carne"),
            Product("miojo", "Miojo", 5, "unidades", "Conservados", "miojo"),
            Product("bolacha", "Bolacha", 3, "pacotes", "Conservados", "bolacha")
        )
    }

    private fun serializeProducts(): String {
        return products.joinToString(separator = "|||") {
            listOf(
                it.id,
                it.name,
                it.availableQuantity.toString(),
                it.unit,
                it.category,
                it.imageName
            ).joinToString(separator = "§")
        }
    }

    private fun deserializeProducts(serialized: String): List<Product> {
        return serialized
            .split("|||")
            .filter { it.isNotBlank() }
            .mapNotNull { line ->
                val parts = line.split("§")
                if (parts.size == 6) {
                    val available = parts[2].toIntOrNull() ?: return@mapNotNull null
                    Product(
                        id = parts[0],
                        name = parts[1],
                        availableQuantity = available,
                        unit = parts[3],
                        category = parts[4],
                        imageName = parts[5]
                    )
                } else {
                    null
                }
            }
    }

    private fun serializeCart(): String {
        return cartQuantities.entries.joinToString(separator = "|||") { "${it.key}=${it.value}" }
    }
}
