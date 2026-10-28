package com.example.ui.navigation

sealed class Screen {
    object Splash : Screen()
    object Onboarding : Screen()
    object Login : Screen()
    object Home : Screen()
    object Categories : Screen()
    data class ProductList(val categoryId: String? = null, val categoryName: String? = null, val initialQuery: String? = null) : Screen()
    data class ProductDetail(val productId: String) : Screen()
    object Cart : Screen()
    object Checkout : Screen()
    data class OrderSuccess(val orderId: String) : Screen()
    object Profile : Screen()
    data class OrderTracking(val orderId: String) : Screen()
}

enum class BottomTab {
    HOME,
    CATEGORIES,
    CART,
    ORDERS,
    PROFILE
}
