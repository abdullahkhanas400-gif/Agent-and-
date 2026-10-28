package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.CartItem
import com.example.data.model.Category
import com.example.data.model.DeliveryAddress
import com.example.data.model.Order
import com.example.data.model.PaymentMethod
import com.example.data.model.Product
import com.example.data.repository.ShopRepository
import com.example.ui.navigation.BottomTab
import com.example.ui.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ShopViewModel(
    private val repository: ShopRepository = ShopRepository()
) : ViewModel() {

    // Navigation Stack
    private val _screenStack = MutableStateFlow<List<Screen>>(listOf(Screen.Splash))
    val currentScreen: StateFlow<Screen> = MutableStateFlow<Screen>(Screen.Splash).apply {
        viewModelScope.launch {
            _screenStack.collect { stack ->
                this@apply.value = stack.lastOrNull() ?: Screen.Home
            }
        }
    }

    private val _activeTab = MutableStateFlow(BottomTab.HOME)
    val activeTab: StateFlow<BottomTab> = _activeTab.asStateFlow()

    // Authentication state
    private val _isUserLoggedIn = MutableStateFlow(false)
    val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn.asStateFlow()

    private val _userName = MutableStateFlow("Guest User")
    val userName: StateFlow<String> = _userName.asStateFlow()

    // Search query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Data from Repository
    val categories: StateFlow<List<Category>> = repository.categories
    val products: StateFlow<List<Product>> = repository.products
    val cart: StateFlow<List<CartItem>> = repository.cart
    val wishlist: StateFlow<Set<String>> = repository.wishlist
    val appliedCoupon: StateFlow<String?> = repository.appliedCoupon
    val userAddress: StateFlow<DeliveryAddress> = repository.userAddress
    val orders: StateFlow<List<Order>> = repository.orders

    val cartItemCount: StateFlow<Int> = cart.combine(_searchQuery) { cartItems, _ ->
        cartItems.sumOf { it.quantity }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val cartSummary: StateFlow<Triple<Double, Double, Double>> = combine(cart, appliedCoupon) { _, _ ->
        repository.calculateCartSummary()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), repository.calculateCartSummary())

    // UI Feedback Banner/Snackbar
    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    // Navigation methods
    fun navigateTo(screen: Screen, addToBackstack: Boolean = true) {
        val current = _screenStack.value
        if (addToBackstack) {
            _screenStack.value = current + screen
        } else {
            _screenStack.value = listOf(screen)
        }

        // Sync bottom tab if applicable
        when (screen) {
            is Screen.Home -> _activeTab.value = BottomTab.HOME
            is Screen.Categories -> _activeTab.value = BottomTab.CATEGORIES
            is Screen.Cart -> _activeTab.value = BottomTab.CART
            is Screen.Profile -> _activeTab.value = BottomTab.PROFILE
            else -> {}
        }
    }

    fun navigateBack(): Boolean {
        val current = _screenStack.value
        if (current.size > 1) {
            _screenStack.value = current.dropLast(1)
            val top = _screenStack.value.last()
            when (top) {
                is Screen.Home -> _activeTab.value = BottomTab.HOME
                is Screen.Categories -> _activeTab.value = BottomTab.CATEGORIES
                is Screen.Cart -> _activeTab.value = BottomTab.CART
                is Screen.Profile -> _activeTab.value = BottomTab.PROFILE
                else -> {}
            }
            return true
        }
        return false
    }

    fun selectBottomTab(tab: BottomTab) {
        _activeTab.value = tab
        when (tab) {
            BottomTab.HOME -> navigateTo(Screen.Home, addToBackstack = false)
            BottomTab.CATEGORIES -> navigateTo(Screen.Categories, addToBackstack = false)
            BottomTab.CART -> navigateTo(Screen.Cart, addToBackstack = true)
            BottomTab.ORDERS -> {
                val latestOrder = orders.value.firstOrNull()
                if (latestOrder != null) {
                    navigateTo(Screen.OrderTracking(latestOrder.id), addToBackstack = true)
                } else {
                    navigateTo(Screen.Profile, addToBackstack = false)
                }
            }
            BottomTab.PROFILE -> navigateTo(Screen.Profile, addToBackstack = false)
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun loginAs(name: String, email: String) {
        _userName.value = name
        _isUserLoggedIn.value = true
        _userMessage.value = "Welcome to ShopEase, $name!"
        navigateTo(Screen.Home, addToBackstack = false)
    }

    fun continueAsGuest() {
        _userName.value = "Guest Shopper"
        _isUserLoggedIn.value = false
        _userMessage.value = "Browsing in Guest Mode"
        navigateTo(Screen.Home, addToBackstack = false)
    }

    fun addToCart(product: Product, quantity: Int = 1, color: Long = 0xFF00875A, size: String = "Standard") {
        repository.addToCart(product, quantity, color, size)
        _userMessage.value = "Added '${product.name}' to Cart"
    }

    fun updateCartQuantity(productId: String, color: Long, size: String, newQuantity: Int) {
        repository.updateCartQuantity(productId, color, size, newQuantity)
    }

    fun removeFromCart(productId: String, color: Long, size: String) {
        repository.removeFromCart(productId, color, size)
        _userMessage.value = "Item removed from Cart"
    }

    fun toggleWishlist(productId: String) {
        val added = repository.toggleWishlist(productId)
        _userMessage.value = if (added) "Added to your Wishlist" else "Removed from Wishlist"
    }

    fun applyCoupon(code: String) {
        val (success, message) = repository.applyCoupon(code)
        _userMessage.value = message
    }

    fun removeCoupon() {
        repository.removeCoupon()
        _userMessage.value = "Coupon removed"
    }

    fun updateAddress(newAddress: DeliveryAddress) {
        repository.updateAddress(newAddress)
        _userMessage.value = "Delivery Address updated"
    }

    fun placeOrder(paymentMethod: PaymentMethod): Order {
        val order = repository.placeOrder(paymentMethod)
        navigateTo(Screen.OrderSuccess(order.id), addToBackstack = false)
        return order
    }

    fun getOrderById(orderId: String): Order? {
        return orders.value.find { it.id == orderId }
    }

    fun getProductById(productId: String): Product? {
        return repository.getProductById(productId)
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }
}
