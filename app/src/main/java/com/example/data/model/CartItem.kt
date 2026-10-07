package com.example.data.model

data class CartItem(
    val cartItemId: String,
    val product: Product,
    val quantity: Int,
    val selectedColor: ProductColor?,
    val selectedSize: String?
) {
    val totalPrice: Double
        get() = product.price * quantity
}

data class Coupon(
    val code: String,
    val discountPercent: Int,
    val minSpend: Double,
    val description: String
)
