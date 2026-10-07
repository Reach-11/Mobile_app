package com.example.data.model

data class ProductColor(
    val name: String,
    val hexColor: Long
)

data class Product(
    val id: String,
    val title: String,
    val brand: String,
    val category: String,
    val price: Double,
    val originalPrice: Double,
    val discountPercent: Int = (((originalPrice - price) / originalPrice) * 100).toInt(),
    val rating: Float,
    val reviewCount: Int,
    val description: String,
    val highlights: List<String>,
    val specs: Map<String, String>,
    val colors: List<ProductColor>,
    val sizes: List<String> = emptyList(),
    val inStock: Boolean = true,
    val isFeatured: Boolean = false,
    val isFlashDeal: Boolean = false,
    val drawableResId: Int? = null,
    val fallbackEmoji: String = "✨"
)

data class CategoryItem(
    val id: String,
    val name: String,
    val count: Int,
    val emoji: String
)

data class Review(
    val id: String,
    val authorName: String,
    val rating: Float,
    val date: String,
    val comment: String,
    val verifiedPurchase: Boolean = true
)
