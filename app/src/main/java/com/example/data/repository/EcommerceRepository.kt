package com.example.data.repository

import com.example.R
import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class EcommerceRepository {

    // Available categories
    val categories: List<CategoryItem> = listOf(
        CategoryItem("all", "All Items", 18, "✨"),
        CategoryItem("audio", "Audio & Tech", 5, "🎧"),
        CategoryItem("wearables", "Wearables", 4, "⌚"),
        CategoryItem("sneakers", "Footwear", 4, "👟"),
        CategoryItem("apparel", "Fashion", 3, "🧥"),
        CategoryItem("lifestyle", "Accessories", 2, "🕶️")
    )

    // Catalog of high-end products
    val allProducts: List<Product> = listOf(
        Product(
            id = "prod_1",
            title = "Aura Studio Pro Wireless ANC Headphones",
            brand = "Aura Acoustics",
            category = "audio",
            price = 249.99,
            originalPrice = 349.99,
            rating = 4.9f,
            reviewCount = 342,
            description = "Custom 40mm titanium drivers deliver studio-grade acoustics with ultra-low distortion. Industry-leading hybrid active noise cancellation blocks up to 98% of ambient noise. Enjoy 50 hours of wireless playback with rapid charge technology.",
            highlights = listOf(
                "Hybrid Active Noise Cancellation with Transparency Mode",
                "50-Hour battery life with 10-min quick charge (5 hrs playback)",
                "Spatial Audio with dynamic head tracking",
                "Ultra-soft memory foam earcups with breathable mesh"
            ),
            specs = mapOf(
                "Driver Size" to "40mm Titanium",
                "Bluetooth" to "5.3 with Multipoint",
                "Weight" to "255 grams",
                "Charging" to "USB-C Fast Charging",
                "Warranty" to "2 Years Worldwide"
            ),
            colors = listOf(
                ProductColor("Obsidian Black", 0xFF1E293B),
                ProductColor("Warm Coral", 0xFFFF5722),
                ProductColor("Silver Mist", 0xFFE2E8F0)
            ),
            sizes = emptyList(),
            inStock = true,
            isFeatured = true,
            isFlashDeal = true,
            drawableResId = R.drawable.img_featured_item_1791340693348,
            fallbackEmoji = "🎧"
        ),
        Product(
            id = "prod_2",
            title = "Apex Horizon Smartwatch Ultra Gen 2",
            brand = "Apex Tech",
            category = "wearables",
            price = 329.00,
            originalPrice = 399.00,
            rating = 4.8f,
            reviewCount = 219,
            description = "Crafted from aerospace-grade titanium with a vibrant sapphire crystal display that shines at up to 2500 nits. Features advanced dual-frequency GPS, biometric ECG, skin temperature sensor, and 100m water resistance.",
            highlights = listOf(
                "Aerospace-grade titanium casing with sapphire crystal",
                "Dual-frequency precision GPS & offline topo maps",
                "Up to 96 hours continuous battery in endurance mode",
                "Comprehensive ECG & blood oxygen heart tracking"
            ),
            specs = mapOf(
                "Display" to "1.92-inch LTPO OLED (2500 nits)",
                "Water Resistance" to "100 meters (ISO 22810)",
                "Case Size" to "49mm",
                "Battery" to "Up to 4 days typical use"
            ),
            colors = listOf(
                ProductColor("Raw Titanium", 0xFF94A3B8),
                ProductColor("Midnight Dark", 0xFF0F172A),
                ProductColor("Sunset Orange", 0xFFFF7043)
            ),
            sizes = listOf("45mm", "49mm"),
            inStock = true,
            isFeatured = true,
            isFlashDeal = false,
            drawableResId = null,
            fallbackEmoji = "⌚"
        ),
        Product(
            id = "prod_3",
            title = "Vortex Neo-Runner Carbon Sneakers",
            brand = "Strides Lab",
            category = "sneakers",
            price = 179.50,
            originalPrice = 220.00,
            rating = 4.7f,
            reviewCount = 184,
            description = "Engineered with a full-length carbon fiber propulsion plate sandwiched between ultra-responsive nitrogen-infused foam. Seamless knit upper wraps the foot like a second skin for unrivaled energy return.",
            highlights = listOf(
                "Full-length curved carbon fiber propulsion plate",
                "Supercritical nitrogen-infused cushioning foam",
                "Engineered breathable jacquard mesh upper",
                "Continental rubber outsole for multi-surface grip"
            ),
            specs = mapOf(
                "Weight" to "210g (Size 9)",
                "Drop" to "8mm (38mm heel / 30mm toe)",
                "Surface" to "Road & Marathon",
                "Plate" to "Full Carbon Curved"
            ),
            colors = listOf(
                ProductColor("Hyper Coral & White", 0xFFFF5722),
                ProductColor("Triple Stealth Black", 0xFF1E293B),
                ProductColor("Volt Emerald", 0xFF10B981)
            ),
            sizes = listOf("US 8", "US 9", "US 10", "US 11", "US 12"),
            inStock = true,
            isFeatured = false,
            isFlashDeal = true,
            drawableResId = null,
            fallbackEmoji = "👟"
        ),
        Product(
            id = "prod_4",
            title = "Aerolite Minimalist Technical Windbreaker",
            brand = "Urban Shell",
            category = "apparel",
            price = 139.00,
            originalPrice = 189.00,
            rating = 4.6f,
            reviewCount = 98,
            description = "Constructed with 3-layer weatherproof ripstop fabric with taped seams. Ultra-packable design stows inside its own chest pocket. Articulated sleeves and storm hood provide sleek mobility in rain and wind.",
            highlights = listOf(
                "20,000mm waterproof & 15,000g breathability rating",
                "YKK AquaGuard sealed water-repellent zippers",
                "Stows down into compact chest travel pouch",
                "Reflective 3M logos for low-light night visibility"
            ),
            specs = mapOf(
                "Material" to "100% Recycled Technical Ripstop",
                "Fit" to "Athletic Tailored",
                "Care" to "Machine Wash Cold"
            ),
            colors = listOf(
                ProductColor("Slate Charcoal", 0xFF334155),
                ProductColor("Terracotta Red", 0xFFD9481E),
                ProductColor("Sage Olive", 0xFF4B5563)
            ),
            sizes = listOf("S", "M", "L", "XL"),
            inStock = true,
            isFeatured = false,
            isFlashDeal = false,
            drawableResId = null,
            fallbackEmoji = "🧥"
        ),
        Product(
            id = "prod_5",
            title = "Prism Polarized Titanium Aviators",
            brand = "Lucid Optics",
            category = "lifestyle",
            price = 165.00,
            originalPrice = 210.00,
            rating = 4.9f,
            reviewCount = 127,
            description = "Handcrafted with Japanese beta-titanium frames weighing under 18 grams. Tri-acetate polarized lenses eliminate glare while enhancing natural contrast with 100% UV400 protection.",
            highlights = listOf(
                "Ultralight beta-titanium flexible frame",
                "High-definition polarized optical glass",
                "Anti-reflective hydrophobic lens coating",
                "Custom milled leather hard travel case included"
            ),
            specs = mapOf(
                "Weight" to "17.4g",
                "Lens Width" to "56mm",
                "Bridge" to "16mm",
                "Temple" to "145mm"
            ),
            colors = listOf(
                ProductColor("Matte Gold & Amber", 0xFFF59E0B),
                ProductColor("Gunmetal Black", 0xFF1E293B),
                ProductColor("Rose Bronze", 0xFF991B1B)
            ),
            sizes = emptyList(),
            inStock = true,
            isFeatured = true,
            isFlashDeal = false,
            drawableResId = null,
            fallbackEmoji = "🕶️"
        ),
        Product(
            id = "prod_6",
            title = "Aura SoundSphere 360 Bluetooth Speaker",
            brand = "Aura Acoustics",
            category = "audio",
            price = 119.00,
            originalPrice = 159.00,
            rating = 4.7f,
            reviewCount = 205,
            description = "Room-filling omnidirectional sound with dual passive bass radiators. IP67 waterproof and dustproof with a buoyant design that floats in water. Up to 24 hours of non-stop beats.",
            highlights = listOf(
                "True 360-degree acoustic projection",
                "IP67 submersible waterproof & floats in pools",
                "PartyLink connects up to 100 Aura speakers",
                "24-Hour continuous battery life"
            ),
            specs = mapOf(
                "Output" to "30W RMS Output",
                "Water Rating" to "IP67 Submersible",
                "Weight" to "580g"
            ),
            colors = listOf(
                ProductColor("Midnight Navy", 0xFF0F172A),
                ProductColor("Coral Blaze", 0xFFFF5722),
                ProductColor("Sand Dune", 0xFFD1D5DB)
            ),
            sizes = emptyList(),
            inStock = true,
            isFeatured = false,
            isFlashDeal = true,
            drawableResId = null,
            fallbackEmoji = "🔊"
        ),
        Product(
            id = "prod_7",
            title = "Kinetix Modular Urban Commuter Backpack",
            brand = "Kinetix",
            category = "lifestyle",
            price = 145.00,
            originalPrice = 185.00,
            rating = 4.8f,
            reviewCount = 143,
            description = "Weatherproof Cordura 500D nylon construction with magnetic Fidlock buckles. Padded dedicated sleeve protects up to 16-inch laptops with quick side-access zipper.",
            highlights = listOf(
                "Fidlock magnetic quick-release chest buckle",
                "Suspended false-bottom 16\" laptop sleeve",
                "Luggage passthrough strap for airports",
                "Concealed passport & RFID security pocket"
            ),
            specs = mapOf(
                "Capacity" to "26 Liters",
                "Material" to "Cordura 500D Weatherproof",
                "Laptop Fit" to "Up to 16-inch MacBook Pro"
            ),
            colors = listOf(
                ProductColor("Matte Charcoal", 0xFF1E293B),
                ProductColor("Forest Moss", 0xFF065F46)
            ),
            sizes = emptyList(),
            inStock = true,
            isFeatured = false,
            isFlashDeal = false,
            drawableResId = null,
            fallbackEmoji = "🎒"
        ),
        Product(
            id = "prod_8",
            title = "Aura MagCharge 3-in-1 Aluminum Stand",
            brand = "Aura Tech",
            category = "wearables",
            price = 89.99,
            originalPrice = 119.99,
            rating = 4.9f,
            reviewCount = 312,
            description = "Simultaneously fast-charges your smartphone, smartwatch, and earbuds wirelessly. Precision CNC milled from solid aircraft aluminum with strong neodymium magnets.",
            highlights = listOf(
                "Fast 15W Qi2 / MagSafe certified charging",
                "Milled aircraft aluminum solid weighted base",
                "Single cable powers all 3 wireless docks",
                "Foldable flat travel mode with pouch included"
            ),
            specs = mapOf(
                "Output" to "15W Phone + 5W Watch + 5W Buds",
                "Input" to "USB-C 30W Adapter Included",
                "Finish" to "Space Gray Anodized"
            ),
            colors = listOf(
                ProductColor("Space Gray", 0xFF334155),
                ProductColor("Silver Pearl", 0xFFE2E8F0)
            ),
            sizes = emptyList(),
            inStock = true,
            isFeatured = true,
            isFlashDeal = false,
            drawableResId = null,
            fallbackEmoji = "⚡"
        )
    )

    // Reviews list
    val mockReviews: List<Review> = listOf(
        Review(
            id = "rev_1",
            authorName = "Elena Vance",
            rating = 5.0f,
            date = "2 days ago",
            comment = "Absolute perfection! The soundstage is remarkably broad and the build quality feels like a $600 luxury flagship. Battery easily lasted through my Tokyo flight."
        ),
        Review(
            id = "rev_2",
            authorName = "Marcus Brody",
            rating = 4.5f,
            date = "1 week ago",
            comment = "Super lightweight and snug fit. ANC drowns out subway noise effortlessly. The coral accent detail looks stunning in person!"
        ),
        Review(
            id = "rev_3",
            authorName = "Sarah Jenkins",
            rating = 5.0f,
            date = "2 weeks ago",
            comment = "Packaging was top-notch, shipping took only 2 days. The companion app connects instantaneously. Highly recommended!"
        )
    )

    // Saved Addresses
    private val _addresses = MutableStateFlow<List<Address>>(
        listOf(
            Address(
                id = "addr_1",
                title = "Home",
                recipientName = "Alex Rivera",
                street = "742 Evergreen Terrace, Apt 4B",
                cityStateZip = "Springfield, OR 97477",
                phone = "+1 (555) 234-5678",
                isDefault = true
            ),
            Address(
                id = "addr_2",
                title = "Studio / Work",
                recipientName = "Alex Rivera (Design Dept)",
                street = "100 Market Street, Suite 500",
                cityStateZip = "San Francisco, CA 94105",
                phone = "+1 (555) 987-6543",
                isDefault = false
            )
        )
    )
    val addresses: StateFlow<List<Address>> = _addresses.asStateFlow()

    private val _selectedAddress = MutableStateFlow<Address>(_addresses.value.first())
    val selectedAddress: StateFlow<Address> = _selectedAddress.asStateFlow()

    // Wishlist Product IDs
    private val _wishlistIds = MutableStateFlow<Set<String>>(setOf("prod_1", "prod_5"))
    val wishlistIds: StateFlow<Set<String>> = _wishlistIds.asStateFlow()

    // Cart Items
    private val _cartItems = MutableStateFlow<List<CartItem>>(
        listOf(
            CartItem(
                cartItemId = "cart_init_1",
                product = allProducts[0],
                quantity = 1,
                selectedColor = allProducts[0].colors.firstOrNull(),
                selectedSize = null
            )
        )
    )
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    // Available Coupons
    val availableCoupons = listOf(
        Coupon("AURA20", 20, 100.0, "20% off orders over $100"),
        Coupon("WELCOME10", 10, 50.0, "10% off your purchase"),
        Coupon("FLASH25", 25, 150.0, "25% off flash sale weekend")
    )

    private val _appliedCoupon = MutableStateFlow<Coupon?>(null)
    val appliedCoupon: StateFlow<Coupon?> = _appliedCoupon.asStateFlow()

    // User Wallet & Loyalty Points
    private val _walletBalance = MutableStateFlow(240.00)
    val walletBalance: StateFlow<Double> = _walletBalance.asStateFlow()

    private val _loyaltyPoints = MutableStateFlow(1850)
    val loyaltyPoints: StateFlow<Int> = _loyaltyPoints.asStateFlow()

    // Order History
    private val _orders = MutableStateFlow<List<OrderItem>>(
        listOf(
            OrderItem(
                orderId = "AUR-92814",
                items = listOf(
                    CartItem("order_item_1", allProducts[1], 1, allProducts[1].colors.first(), "49mm")
                ),
                subtotal = 329.00,
                discount = 0.0,
                shippingFee = 0.0,
                tax = 26.32,
                totalAmount = 355.32,
                status = OrderStatus.IN_TRANSIT,
                trackingCode = "TRK-88210934US",
                orderDate = "Oct 4, 2026",
                estimatedArrival = "Tomorrow by 5 PM",
                address = _addresses.value.first(),
                paymentMethod = PaymentMethod.GOOGLE_PAY
            ),
            OrderItem(
                orderId = "AUR-81042",
                items = listOf(
                    CartItem("order_item_2", allProducts[4], 1, allProducts[4].colors.first(), null),
                    CartItem("order_item_3", allProducts[7], 1, allProducts[7].colors.first(), null)
                ),
                subtotal = 254.99,
                discount = 25.49,
                shippingFee = 0.0,
                tax = 18.36,
                totalAmount = 247.86,
                status = OrderStatus.DELIVERED,
                trackingCode = "TRK-77192043US",
                orderDate = "Sep 28, 2026",
                estimatedArrival = "Delivered on Sep 30",
                address = _addresses.value.first(),
                paymentMethod = PaymentMethod.CREDIT_CARD
            )
        )
    )
    val orders: StateFlow<List<OrderItem>> = _orders.asStateFlow()

    // Wishlist actions
    fun toggleWishlist(productId: String): Boolean {
        val current = _wishlistIds.value.toMutableSet()
        val isAdded = if (current.contains(productId)) {
            current.remove(productId)
            false
        } else {
            current.add(productId)
            true
        }
        _wishlistIds.value = current
        return isAdded
    }

    fun isWishlisted(productId: String): Boolean {
        return _wishlistIds.value.contains(productId)
    }

    // Cart actions
    fun addToCart(product: Product, quantity: Int = 1, color: ProductColor? = null, size: String? = null) {
        val currentList = _cartItems.value.toMutableList()
        val existingIndex = currentList.indexOfFirst {
            it.product.id == product.id &&
            it.selectedColor?.name == color?.name &&
            it.selectedSize == size
        }

        if (existingIndex >= 0) {
            val existing = currentList[existingIndex]
            currentList[existingIndex] = existing.copy(quantity = existing.quantity + quantity)
        } else {
            currentList.add(
                CartItem(
                    cartItemId = UUID.randomUUID().toString(),
                    product = product,
                    quantity = quantity,
                    selectedColor = color ?: product.colors.firstOrNull(),
                    selectedSize = size ?: product.sizes.firstOrNull()
                )
            )
        }
        _cartItems.value = currentList
    }

    fun updateCartQuantity(cartItemId: String, newQuantity: Int) {
        if (newQuantity <= 0) {
            removeCartItem(cartItemId)
            return
        }
        _cartItems.value = _cartItems.value.map {
            if (it.cartItemId == cartItemId) it.copy(quantity = newQuantity) else it
        }
    }

    fun removeCartItem(cartItemId: String) {
        _cartItems.value = _cartItems.value.filter { it.cartItemId != cartItemId }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
        _appliedCoupon.value = null
    }

    // Coupon actions
    fun applyCoupon(code: String): Result<Coupon> {
        val normalized = code.trim().uppercase(Locale.ROOT)
        val coupon = availableCoupons.find { it.code.uppercase(Locale.ROOT) == normalized }
        return if (coupon != null) {
            val subtotal = _cartItems.value.sumOf { it.totalPrice }
            if (subtotal >= coupon.minSpend) {
                _appliedCoupon.value = coupon
                Result.success(coupon)
            } else {
                Result.failure(Exception("Minimum order of $${coupon.minSpend} required for this coupon"))
            }
        } else {
            Result.failure(Exception("Invalid promo code. Try AURA20 or WELCOME10"))
        }
    }

    fun removeCoupon() {
        _appliedCoupon.value = null
    }

    // Wallet actions
    fun topUpWallet(amount: Double) {
        _walletBalance.value += amount
    }

    // Address actions
    fun selectAddress(address: Address) {
        _selectedAddress.value = address
    }

    fun addAddress(address: Address) {
        val list = _addresses.value.toMutableList()
        list.add(address)
        _addresses.value = list
        if (address.isDefault || list.size == 1) {
            _selectedAddress.value = address
        }
    }

    // Place Order
    fun placeOrder(
        speed: DeliverySpeed,
        paymentMethod: PaymentMethod
    ): OrderItem {
        val currentCart = _cartItems.value
        val subtotal = currentCart.sumOf { it.totalPrice }
        val discount = _appliedCoupon.value?.let { (subtotal * it.discountPercent) / 100.0 } ?: 0.0
        val shipping = if (subtotal >= 75.0 && speed == DeliverySpeed.STANDARD) 0.0 else speed.cost
        val tax = (subtotal - discount) * 0.08
        val total = (subtotal - discount + shipping + tax).coerceAtLeast(0.0)

        // Deduct from wallet if chosen
        if (paymentMethod == PaymentMethod.AURA_WALLET) {
            _walletBalance.value = (_walletBalance.value - total).coerceAtLeast(0.0)
        }

        // Add loyalty points (1 point per dollar spent)
        _loyaltyPoints.value += total.toInt()

        val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
        val newOrder = OrderItem(
            orderId = "AUR-${(10000..99999).random()}",
            items = currentCart,
            subtotal = subtotal,
            discount = discount,
            shippingFee = shipping,
            tax = tax,
            totalAmount = total,
            status = OrderStatus.CONFIRMED,
            trackingCode = "TRK-${(10000000..99999999).random()}US",
            orderDate = dateFormat.format(Date()),
            estimatedArrival = speed.estimatedDays,
            address = _selectedAddress.value,
            paymentMethod = paymentMethod
        )

        _orders.value = listOf(newOrder) + _orders.value
        clearCart()
        return newOrder
    }
}
