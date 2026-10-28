package com.example.data.repository

import com.example.data.model.CartItem
import com.example.data.model.Category
import com.example.data.model.DeliveryAddress
import com.example.data.model.Order
import com.example.data.model.PaymentMethod
import com.example.data.model.Product
import com.example.data.model.TrackingStep
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ShopRepository {

    private val sampleCategories = listOf(
        Category(
            id = "electronics",
            name = "Electronics",
            hindiName = "इलेक्ट्रॉनिक्स",
            iconName = "devices",
            itemCount = 24,
            bannerText = "Up to 50% Off Smart Gadgets",
            subcategories = listOf("All", "Earbuds", "Smartwatches", "Chargers", "Audio")
        ),
        Category(
            id = "fashion",
            name = "Fashion",
            hindiName = "फैशन और कपड़े",
            iconName = "checkroom",
            itemCount = 48,
            bannerText = "Eco Cotton & Summer Wear",
            subcategories = listOf("All", "T-Shirts", "Shirts", "Pants", "Jackets")
        ),
        Category(
            id = "home",
            name = "Home & Living",
            hindiName = "होम और डेकोर",
            iconName = "weekend",
            itemCount = 32,
            bannerText = "Sustainable Wooden & Ceramic Decor",
            subcategories = listOf("All", "Decor", "Lighting", "Kitchen", "Bedding")
        ),
        Category(
            id = "footwear",
            name = "Footwear",
            hindiName = "जूते और स्लीपर्स",
            iconName = "roller_skating",
            itemCount = 18,
            bannerText = "Comfort Running & Casuals",
            subcategories = listOf("All", "Sneakers", "Loafers", "Sandals")
        ),
        Category(
            id = "beauty",
            name = "Beauty & Care",
            hindiName = "ब्यूटी और स्किनकेयर",
            iconName = "spa",
            itemCount = 19,
            bannerText = "100% Herbal & Clean Ingredients",
            subcategories = listOf("All", "Skincare", "Haircare", "Face Gel")
        ),
        Category(
            id = "organic",
            name = "Organic Foods",
            hindiName = "ऑर्गेनिक फूड्स",
            iconName = "eco",
            itemCount = 22,
            bannerText = "Farm Fresh & Direct Produce",
            subcategories = listOf("All", "Green Tea", "Honey", "Seeds & Nuts")
        )
    )

    private val sampleProducts = listOf(
        Product(
            id = "prod_1",
            name = "AeroPods Pro Wireless ANC Earbuds",
            categoryId = "electronics",
            categoryName = "Electronics",
            price = 2499.0,
            originalPrice = 4999.0,
            rating = 4.8f,
            reviewCount = 1240,
            imageCategory = "earbuds",
            description = "Active Noise Cancellation up to 35dB, 36 hours total battery playtime, deep bass drivers, IPX5 water resistance, and instant Bluetooth 5.3 auto-pairing.",
            features = listOf("Active Noise Cancellation", "36h Battery Life", "Fast Charging (10m = 3h)", "Ultra Low Latency Mode"),
            availableColors = listOf(0xFF00875A, 0xFF0F172A, 0xFFFFFFFF),
            availableSizes = listOf("Standard"),
            tag = "Mega Deal",
            deliveryDays = 2
        ),
        Product(
            id = "prod_2",
            name = "AuraPulse AMOLED Smartwatch 2.0",
            categoryId = "electronics",
            categoryName = "Electronics",
            price = 3299.0,
            originalPrice = 6999.0,
            rating = 4.7f,
            reviewCount = 890,
            imageCategory = "smartwatch",
            description = "1.43-inch HD AMOLED display with 1000 nits brightness, SpO2 & 24/7 heart rate monitoring, Bluetooth calling, and 120+ sports workout modes.",
            features = listOf("Bluetooth Calling", "Always-On AMOLED", "7-Day Battery", "Sleep & Heart Tracking"),
            availableColors = listOf(0xFF00875A, 0xFF1E293B, 0xFFD97706),
            availableSizes = listOf("42mm", "46mm"),
            tag = "Best Seller",
            deliveryDays = 1
        ),
        Product(
            id = "prod_3",
            name = "Organic Breathable Cotton Crew T-Shirt",
            categoryId = "fashion",
            categoryName = "Fashion",
            price = 699.0,
            originalPrice = 1299.0,
            rating = 4.9f,
            reviewCount = 612,
            imageCategory = "tshirt",
            description = "Crafted from 100% combed organic ring-spun cotton. Ultra-soft bio-washed fabric for superior comfort and all-day breathability.",
            features = listOf("100% Organic Cotton", "Pre-shrunk Fabric", "Zero Plastic Packaging", "Color-Fast Dyed"),
            availableColors = listOf(0xFF00875A, 0xFFFFFFFF, 0xFF1E293B, 0xFF64748B),
            availableSizes = listOf("S", "M", "L", "XL", "XXL"),
            tag = "Eco Pick",
            deliveryDays = 2
        ),
        Product(
            id = "prod_4",
            name = "Handcrafted Ceramic Matcha & Coffee Mug",
            categoryId = "home",
            categoryName = "Home & Living",
            price = 449.0,
            originalPrice = 899.0,
            rating = 4.6f,
            reviewCount = 340,
            imageCategory = "mug",
            description = "Artisanal textured stoneware ceramic cup with an earthy speckled glaze. Microwave and dishwasher safe, 350ml capacity.",
            features = listOf("350ml Capacity", "Microwave & Dishwasher Safe", "Handmade Glaze", "Heat Retaining"),
            availableColors = listOf(0xFF00875A, 0xFFFFFFFF, 0xFF78350F),
            availableSizes = listOf("350ml"),
            tag = "Trending",
            deliveryDays = 3
        ),
        Product(
            id = "prod_5",
            name = "CloudGlide Lightweight Running Shoes",
            categoryId = "footwear",
            categoryName = "Footwear",
            price = 1999.0,
            originalPrice = 3999.0,
            rating = 4.8f,
            reviewCount = 745,
            imageCategory = "shoes",
            description = "Engineered mesh upper for maximum airflow, EVA responsive rebound sole, and memory foam cushioned insole for road running & walking.",
            features = listOf("Ultra Lightweight 220g", "High Rebound Foam", "Breathable Mesh", "Anti-Skid Rubber Grip"),
            availableColors = listOf(0xFF00875A, 0xFF0F172A, 0xFFFFFFFF),
            availableSizes = listOf("UK 7", "UK 8", "UK 9", "UK 10", "UK 11"),
            tag = "Best Seller",
            deliveryDays = 2
        ),
        Product(
            id = "prod_6",
            name = "Herbal Aloe Vera & Green Tea Face Gel (150g)",
            categoryId = "beauty",
            categoryName = "Beauty & Care",
            price = 349.0,
            originalPrice = 599.0,
            rating = 4.9f,
            reviewCount = 1820,
            imageCategory = "cream",
            description = "Pure cold-pressed aloe vera enriched with organic green tea extract. Calms redness, hydrates skin deeply, and prevents acne breakouts.",
            features = listOf("Zero Parabens & Sulfates", "Non-Sticky Formula", "100% Vegan", "Dermatologically Tested"),
            availableColors = listOf(0xFF10B981),
            availableSizes = listOf("150g", "300g"),
            tag = "Eco Pick",
            deliveryDays = 1
        ),
        Product(
            id = "prod_7",
            name = "Pure Himalayan Wild Raw Honey (500g)",
            categoryId = "organic",
            categoryName = "Organic Foods",
            price = 499.0,
            originalPrice = 799.0,
            rating = 4.9f,
            reviewCount = 520,
            imageCategory = "honey",
            description = "Unprocessed, unpasteurized monofloral forest honey harvested directly by local beekeepers in Himalayan foothills.",
            features = listOf("100% Raw & Natural", "No Added Invert Sugar", "Rich in Antioxidants", "Glass Jar Packing"),
            availableColors = listOf(0xFFD97706),
            availableSizes = listOf("500g", "1kg"),
            tag = "Organic Pick",
            deliveryDays = 2
        ),
        Product(
            id = "prod_8",
            name = "65W GaN Triple-Port Fast Wall Charger",
            categoryId = "electronics",
            categoryName = "Electronics",
            price = 1499.0,
            originalPrice = 2999.0,
            rating = 4.7f,
            reviewCount = 415,
            imageCategory = "charger",
            description = "Gallium Nitride (GaN) technology provides 65W PD ultra-fast charging for laptops, tablets, and phones simultaneously with dual USB-C and USB-A ports.",
            features = listOf("65W Power Delivery", "2x USB-C + 1x USB-A", "GaN III Tech (Cool Running)", "Foldable Travel Pins"),
            availableColors = listOf(0xFFFFFFFF, 0xFF0F172A),
            availableSizes = listOf("Standard"),
            tag = "Trending",
            deliveryDays = 1
        )
    )

    private val _categories = MutableStateFlow(sampleCategories)
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _products = MutableStateFlow(sampleProducts)
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _cart = MutableStateFlow<List<CartItem>>(
        listOf(
            CartItem(product = sampleProducts[0], quantity = 1, selectedColor = 0xFF00875A, selectedSize = "Standard"),
            CartItem(product = sampleProducts[2], quantity = 2, selectedColor = 0xFF00875A, selectedSize = "L")
        )
    )
    val cart: StateFlow<List<CartItem>> = _cart.asStateFlow()

    private val _wishlist = MutableStateFlow<Set<String>>(setOf("prod_1", "prod_5"))
    val wishlist: StateFlow<Set<String>> = _wishlist.asStateFlow()

    private val _appliedCoupon = MutableStateFlow<String?>("GREEN20")
    val appliedCoupon: StateFlow<String?> = _appliedCoupon.asStateFlow()

    private val _userAddress = MutableStateFlow(
        DeliveryAddress(
            id = "addr_primary",
            fullName = "Rahul Sharma",
            phone = "+91 98765 43210",
            street = "B-204, Green Palms Apartment, Sector 62",
            city = "Noida",
            state = "Uttar Pradesh",
            pincode = "201301",
            type = "Home"
        )
    )
    val userAddress: StateFlow<DeliveryAddress> = _userAddress.asStateFlow()

    private val _orders = MutableStateFlow<List<Order>>(
        listOf(
            Order(
                id = "SE-78219",
                date = "24 Oct 2026",
                items = listOf(
                    CartItem(product = sampleProducts[1], quantity = 1, selectedColor = 0xFF00875A, selectedSize = "46mm")
                ),
                subtotal = 3299.0,
                discount = 329.9,
                deliveryFee = 0.0,
                totalAmount = 2969.1,
                address = _userAddress.value,
                paymentMethod = PaymentMethod.UPI,
                status = "Delivered",
                estimatedDeliveryDate = "Delivered on 26 Oct",
                trackingSteps = listOf(
                    TrackingStep("Order Confirmed", "Your order has been verified", "24 Oct, 10:30 AM", true),
                    TrackingStep("Packed & Quality Checked", "Item packed at Delhi Warehouse", "24 Oct, 03:15 PM", true),
                    TrackingStep("Shipped via BlueDart", "Dispatched to hub in Noida", "25 Oct, 09:00 AM", true),
                    TrackingStep("Out for Delivery", "Courier executive assigned", "26 Oct, 11:20 AM", true),
                    TrackingStep("Delivered Successfully", "Delivered to Rahul Sharma", "26 Oct, 02:45 PM", true)
                )
            )
        )
    )
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    fun getProductById(id: String): Product? {
        return _products.value.find { it.id == id }
    }

    fun getProductsByCategory(categoryId: String?): List<Product> {
        if (categoryId.isNullOrBlank() || categoryId == "all") return _products.value
        return _products.value.filter { it.categoryId.equals(categoryId, ignoreCase = true) }
    }

    fun searchProducts(query: String): List<Product> {
        if (query.isBlank()) return _products.value
        return _products.value.filter {
            it.name.contains(query, ignoreCase = true) ||
            it.categoryName.contains(query, ignoreCase = true) ||
            it.description.contains(query, ignoreCase = true) ||
            (it.tag?.contains(query, ignoreCase = true) == true)
        }
    }

    fun addToCart(product: Product, quantity: Int = 1, color: Long = 0xFF00875A, size: String = "Standard") {
        val currentList = _cart.value.toMutableList()
        val existingIndex = currentList.indexOfFirst {
            it.product.id == product.id && it.selectedColor == color && it.selectedSize == size
        }
        if (existingIndex != -1) {
            val existing = currentList[existingIndex]
            currentList[existingIndex] = existing.copy(quantity = existing.quantity + quantity)
        } else {
            currentList.add(CartItem(product = product, quantity = quantity, selectedColor = color, selectedSize = size))
        }
        _cart.value = currentList
    }

    fun updateCartQuantity(productId: String, color: Long, size: String, newQuantity: Int) {
        val currentList = _cart.value.toMutableList()
        val index = currentList.indexOfFirst {
            it.product.id == productId && it.selectedColor == color && it.selectedSize == size
        }
        if (index != -1) {
            if (newQuantity <= 0) {
                currentList.removeAt(index)
            } else {
                currentList[index] = currentList[index].copy(quantity = newQuantity)
            }
            _cart.value = currentList
        }
    }

    fun removeFromCart(productId: String, color: Long, size: String) {
        _cart.value = _cart.value.filterNot {
            it.product.id == productId && it.selectedColor == color && it.selectedSize == size
        }
    }

    fun clearCart() {
        _cart.value = emptyList()
    }

    fun toggleWishlist(productId: String): Boolean {
        val current = _wishlist.value.toMutableSet()
        val wasInWishlist = current.contains(productId)
        if (wasInWishlist) {
            current.remove(productId)
        } else {
            current.add(productId)
        }
        _wishlist.value = current
        return !wasInWishlist
    }

    fun isWishlisted(productId: String): Boolean {
        return _wishlist.value.contains(productId)
    }

    fun applyCoupon(code: String): Pair<Boolean, String> {
        val clean = code.trim().uppercase()
        return when (clean) {
            "GREEN20" -> {
                _appliedCoupon.value = "GREEN20"
                Pair(true, "Coupon GREEN20 applied! 20% discount added.")
            }
            "SHOPEASE50" -> {
                _appliedCoupon.value = "SHOPEASE50"
                Pair(true, "Welcome Offer SHOPEASE50 applied! ₹500 off.")
            }
            "FREESHIP" -> {
                _appliedCoupon.value = "FREESHIP"
                Pair(true, "Free Express Delivery applied!")
            }
            else -> Pair(false, "Invalid coupon code. Try 'GREEN20' or 'SHOPEASE50'.")
        }
    }

    fun removeCoupon() {
        _appliedCoupon.value = null
    }

    fun calculateCartSummary(): Triple<Double, Double, Double> {
        val subtotal = _cart.value.sumOf { it.totalPrice }
        val discount = when (_appliedCoupon.value) {
            "GREEN20" -> subtotal * 0.20
            "SHOPEASE50" -> if (subtotal >= 1000) 500.0 else subtotal * 0.10
            else -> 0.0
        }
        val delivery = if (subtotal > 999 || _appliedCoupon.value == "FREESHIP" || subtotal == 0.0) 0.0 else 49.0
        return Triple(subtotal, discount, delivery)
    }

    fun updateAddress(newAddress: DeliveryAddress) {
        _userAddress.value = newAddress
    }

    fun placeOrder(paymentMethod: PaymentMethod): Order {
        val (subtotal, discount, deliveryFee) = calculateCartSummary()
        val total = (subtotal - discount + deliveryFee).coerceAtLeast(0.0)
        val orderNum = (10000..99999).random()
        val orderId = "SE-$orderNum"
        val currentDate = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date())

        val steps = listOf(
            TrackingStep("Order Confirmed", "Verified and sent to seller", currentDate, true),
            TrackingStep("Packed & Dispatched", "Eco-friendly zero-plastic packaging", "Expected tomorrow", false),
            TrackingStep("Out for Delivery", "Express doorstep transit", "In 2 days", false),
            TrackingStep("Delivered", "Delivered with happiness", "Estimated in 2 days", false)
        )

        val newOrder = Order(
            id = orderId,
            date = currentDate,
            items = _cart.value,
            subtotal = subtotal,
            discount = discount,
            deliveryFee = deliveryFee,
            totalAmount = total,
            address = _userAddress.value,
            paymentMethod = paymentMethod,
            status = "Confirmed",
            trackingSteps = steps,
            estimatedDeliveryDate = "Within 2-3 business days"
        )

        _orders.value = listOf(newOrder) + _orders.value
        clearCart()
        return newOrder
    }
}
