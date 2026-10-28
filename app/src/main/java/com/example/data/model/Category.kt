package com.example.data.model

data class Category(
    val id: String,
    val name: String,
    val hindiName: String,
    val iconName: String,
    val itemCount: Int,
    val bannerText: String,
    val subcategories: List<String>
)
