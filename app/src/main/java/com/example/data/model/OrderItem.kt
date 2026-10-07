package com.example.data.model

data class Address(
    val id: String,
    val title: String, // "Home", "Work", "Studio"
    val recipientName: String,
    val street: String,
    val cityStateZip: String,
    val phone: String,
    val isDefault: Boolean = false
)

enum class OrderStatus(val label: String, val stepIndex: Int) {
    CONFIRMED("Order Confirmed", 0),
    PREPARING("Packaging", 1),
    IN_TRANSIT("In Transit", 2),
    OUT_FOR_DELIVERY("Out for Delivery", 3),
    DELIVERED("Delivered", 4)
}

enum class DeliverySpeed(val label: String, val estimatedDays: String, val cost: Double) {
    STANDARD("Standard Delivery", "3-5 Business Days", 0.0),
    EXPRESS("Express Air", "1-2 Business Days", 9.99),
    SAME_DAY("Priority Same Day", "Today by 8 PM", 14.99)
}

enum class PaymentMethod(val label: String, val iconDescription: String) {
    GOOGLE_PAY("Google Pay", "Instant 1-tap checkout"),
    CREDIT_CARD("Credit / Debit Card", "Visa •••• 4242"),
    AURA_WALLET("Aura Balance", "Pay via stored balance"),
    CASH_ON_DELIVERY("Cash on Delivery", "Pay upon receipt")
}

data class OrderItem(
    val orderId: String,
    val items: List<CartItem>,
    val subtotal: Double,
    val discount: Double,
    val shippingFee: Double,
    val tax: Double,
    val totalAmount: Double,
    val status: OrderStatus,
    val trackingCode: String,
    val orderDate: String,
    val estimatedArrival: String,
    val address: Address,
    val paymentMethod: PaymentMethod
)
