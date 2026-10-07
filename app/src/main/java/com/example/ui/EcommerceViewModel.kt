package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.EcommerceRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

enum class Screen {
    HOME,
    EXPLORE,
    WISHLIST,
    ORDERS,
    CART,
    PRODUCT_DETAIL
}

enum class SortOption(val label: String) {
    POPULAR("Most Popular"),
    PRICE_LOW_HIGH("Price: Low to High"),
    PRICE_HIGH_LOW("Price: High to Low"),
    RATING("Highest Rated")
}

data class FilterState(
    val maxPrice: Double = 500.0,
    val minRating: Float = 0f,
    val inStockOnly: Boolean = false,
    val sortOption: SortOption = SortOption.POPULAR
)

class EcommerceViewModel(
    val repository: EcommerceRepository = EcommerceRepository()
) : ViewModel() {

    // Current Screen
    private val _currentScreen = MutableStateFlow(Screen.HOME)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // Screen stack for back navigation
    private val screenStack = mutableListOf(Screen.HOME)

    // Selected product for detail view
    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct.asStateFlow()

    // Search and Category
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("all")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    // Filters
    private val _filters = MutableStateFlow(FilterState())
    val filters: StateFlow<FilterState> = _filters.asStateFlow()

    // UI Snackbars/Messages
    private val _snackbarEvent = MutableSharedFlow<String>()
    val snackbarEvent: SharedFlow<String> = _snackbarEvent.asSharedFlow()

    // Dialog & Sheet States
    private val _showFilterSheet = MutableStateFlow(false)
    val showFilterSheet: StateFlow<Boolean> = _showFilterSheet.asStateFlow()

    private val _showCheckoutDialog = MutableStateFlow(false)
    val showCheckoutDialog: StateFlow<Boolean> = _showCheckoutDialog.asStateFlow()

    private val _placedOrder = MutableStateFlow<OrderItem?>(null)
    val placedOrder: StateFlow<OrderItem?> = _placedOrder.asStateFlow()

    private val _trackedOrder = MutableStateFlow<OrderItem?>(null)
    val trackedOrder: StateFlow<OrderItem?> = _trackedOrder.asStateFlow()

    private val _showWalletDialog = MutableStateFlow(false)
    val showWalletDialog: StateFlow<Boolean> = _showWalletDialog.asStateFlow()

    private val _showAddressDialog = MutableStateFlow(false)
    val showAddressDialog: StateFlow<Boolean> = _showAddressDialog.asStateFlow()

    // Filtered Products flow
    private val _filteredProducts = MutableStateFlow<List<Product>>(repository.allProducts)
    val filteredProducts: StateFlow<List<Product>> = _filteredProducts.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                _searchQuery,
                _selectedCategory,
                _filters
            ) { query, cat, filterState ->
                var list = repository.allProducts

                // Category filter
                if (cat != "all") {
                    list = list.filter { it.category == cat }
                }

                // Query filter
                if (query.isNotBlank()) {
                    val q = query.trim().lowercase()
                    list = list.filter {
                        it.title.lowercase().contains(q) ||
                        it.brand.lowercase().contains(q) ||
                        it.description.lowercase().contains(q) ||
                        it.category.lowercase().contains(q)
                    }
                }

                // Price filter
                list = list.filter { it.price <= filterState.maxPrice }

                // Rating filter
                if (filterState.minRating > 0f) {
                    list = list.filter { it.rating >= filterState.minRating }
                }

                // In stock filter
                if (filterState.inStockOnly) {
                    list = list.filter { it.inStock }
                }

                // Sorting
                when (filterState.sortOption) {
                    SortOption.POPULAR -> list.sortedByDescending { it.reviewCount }
                    SortOption.PRICE_LOW_HIGH -> list.sortedBy { it.price }
                    SortOption.PRICE_HIGH_LOW -> list.sortedByDescending { it.price }
                    SortOption.RATING -> list.sortedByDescending { it.rating }
                }
            }.collect {
                _filteredProducts.value = it
            }
        }
    }

    // Navigation
    fun navigateTo(screen: Screen) {
        if (_currentScreen.value != screen) {
            screenStack.add(screen)
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        if (screenStack.size > 1) {
            screenStack.removeAt(screenStack.size - 1)
            _currentScreen.value = screenStack.last()
            return true
        }
        return false
    }

    fun openProductDetail(product: Product) {
        _selectedProduct.value = product
        navigateTo(Screen.PRODUCT_DETAIL)
    }

    // Search & Filters
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(categoryId: String) {
        _selectedCategory.value = categoryId
    }

    fun setFilterSheetVisible(visible: Boolean) {
        _showFilterSheet.value = visible
    }

    fun updateFilters(newFilters: FilterState) {
        _filters.value = newFilters
    }

    fun resetFilters() {
        _filters.value = FilterState()
        _searchQuery.value = ""
        _selectedCategory.value = "all"
    }

    // Wishlist
    fun toggleWishlist(product: Product) {
        val added = repository.toggleWishlist(product.id)
        viewModelScope.launch {
            _snackbarEvent.emit(if (added) "Saved to your Wishlist ❤️" else "Removed from Wishlist")
        }
    }

    // Cart
    fun addToCart(product: Product, quantity: Int = 1, color: ProductColor? = null, size: String? = null) {
        repository.addToCart(product, quantity, color, size)
        viewModelScope.launch {
            _snackbarEvent.emit("Added ${product.title.take(20)}... to Cart 🛒")
        }
    }

    fun updateCartQuantity(cartItemId: String, quantity: Int) {
        repository.updateCartQuantity(cartItemId, quantity)
    }

    fun removeCartItem(cartItemId: String) {
        repository.removeCartItem(cartItemId)
        viewModelScope.launch {
            _snackbarEvent.emit("Item removed from Cart")
        }
    }

    fun applyCoupon(code: String) {
        val result = repository.applyCoupon(code)
        viewModelScope.launch {
            if (result.isSuccess) {
                _snackbarEvent.emit("Promo code '${code.uppercase()}' applied successfully! 🎉")
            } else {
                _snackbarEvent.emit(result.exceptionOrNull()?.message ?: "Invalid promo code")
            }
        }
    }

    fun removeCoupon() {
        repository.removeCoupon()
    }

    // Checkout
    fun setCheckoutDialogVisible(visible: Boolean) {
        _showCheckoutDialog.value = visible
    }

    fun setPlacedOrder(order: OrderItem?) {
        _placedOrder.value = order
    }

    fun trackOrder(order: OrderItem?) {
        _trackedOrder.value = order
    }

    fun placeOrder(speed: DeliverySpeed, paymentMethod: PaymentMethod) {
        val order = repository.placeOrder(speed, paymentMethod)
        _showCheckoutDialog.value = false
        _placedOrder.value = order
        viewModelScope.launch {
            _snackbarEvent.emit("Order #${order.orderId} placed successfully! 🚀")
        }
    }

    // Wallet & Address dialogs
    fun setWalletDialogVisible(visible: Boolean) {
        _showWalletDialog.value = visible
    }

    fun topUpWallet(amount: Double) {
        repository.topUpWallet(amount)
        _showWalletDialog.value = false
        viewModelScope.launch {
            _snackbarEvent.emit("Added $$amount to your Aura Wallet! 💳")
        }
    }

    fun setAddressDialogVisible(visible: Boolean) {
        _showAddressDialog.value = visible
    }

    fun addAddress(address: Address) {
        repository.addAddress(address)
        _showAddressDialog.value = false
        viewModelScope.launch {
            _snackbarEvent.emit("Delivery address saved! 📍")
        }
    }

    fun selectAddress(address: Address) {
        repository.selectAddress(address)
    }
}
