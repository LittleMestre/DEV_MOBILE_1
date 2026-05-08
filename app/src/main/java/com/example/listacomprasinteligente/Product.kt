package com.example.listacomprasinteligente

data class Product(
    val id: String,
    val name: String,
    var availableQuantity: Int,
    val unit: String,
    val category: String,
    val imageName: String
)
