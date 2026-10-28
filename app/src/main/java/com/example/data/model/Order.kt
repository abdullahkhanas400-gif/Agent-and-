package com.example.data.model

data class DeliveryAddress(
    val id: String = "addr_1",
    val fullName: String = "Rahul Sharma",
    val phone: String = "+91 98765 43210",
    val street: String = "Flat 402, Green Meadows, Sector 62",
    val city: String = "Noida",
    val state: String = "Uttar Pradesh",
    val pincode: String = "201301",
    val type: String = "Home" // Home, Work, Other
) {
    val fullFormatted: String
        get() = "$street, $city, $state - $pincode"
}

enum class PaymentMethod(val title: String, val subtitle: String, val icon: String) {
    UPI("UPI / Google Pay / PhonePe", "Instant payment with zero charges", "upi"),
    CARD("Credit / Debit Card", "Visa, Mastercard, RuPay & more", "card"),
    NET_BANKING("Net Banking", "All Indian major banks supported", "bank"),
    COD("Cash on Delivery (COD)", "Pay when your package arrives", "cash")
}

data class Order(
    val id: String,
    val date: String,
    val items: List<CartItem>,
    val subtotal: Double,
    val discount: Double,
    val deliveryFee: Double,
    val totalAmount: Double,
    val address: DeliveryAddress,
    val paymentMethod: PaymentMethod,
    val status: String = "Confirmed", // Confirmed, Packed, Shipped, Out for Delivery, Delivered
    val trackingSteps: List<TrackingStep> = emptyList(),
    val estimatedDeliveryDate: String = "In 2 business days"
)

data class TrackingStep(
    val title: String,
    val description: String,
    val timestamp: String,
    val isCompleted: Boolean
)
