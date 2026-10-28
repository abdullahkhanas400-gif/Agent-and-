package com.example.data.model

data class Product(
    val id: String,
    val name: String,
    val categoryId: String,
    val categoryName: String,
    val price: Double,
    val originalPrice: Double,
    val discountPercent: Int = (((originalPrice - price) / originalPrice) * 100).toInt(),
    val rating: Float,
    val reviewCount: Int,
    val imageDrawableRes: Int? = null,
    val imageCategory: String = "general",
    val description: String,
    val features: List<String> = emptyList(),
    val availableColors: List<Long> = listOf(0xFF00875A, 0xFF1E293B, 0xFFE2E8F0),
    val availableSizes: List<String> = listOf("S", "M", "L", "XL"),
    val inStock: Boolean = true,
    val tag: String? = null, // e.g. "Best Seller", "Mega Deal", "Trending", "Eco Pick"
    val deliveryDays: Int = 2
)
