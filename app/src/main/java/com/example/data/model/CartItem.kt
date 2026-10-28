package com.example.data.model

data class CartItem(
    val product: Product,
    val quantity: Int = 1,
    val selectedColor: Long = product.availableColors.firstOrNull() ?: 0xFF00875A,
    val selectedSize: String = product.availableSizes.firstOrNull() ?: "Standard"
) {
    val totalPrice: Double
        get() = product.price * quantity
}
