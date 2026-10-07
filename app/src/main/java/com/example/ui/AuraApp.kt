package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.*
import com.example.ui.screens.*
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AuraApp(
    viewModel: EcommerceViewModel = viewModel()
) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val showFilterSheet by viewModel.showFilterSheet.collectAsState()
    val showCheckoutDialog by viewModel.showCheckoutDialog.collectAsState()
    val placedOrder by viewModel.placedOrder.collectAsState()
    val trackedOrder by viewModel.trackedOrder.collectAsState()
    val showWalletDialog by viewModel.showWalletDialog.collectAsState()
    val showAddressDialog by viewModel.showAddressDialog.collectAsState()

    val wishlistIds by viewModel.repository.wishlistIds.collectAsState()
    val addresses by viewModel.repository.addresses.collectAsState()
    val selectedAddress by viewModel.repository.selectedAddress.collectAsState()
    val walletBalance by viewModel.repository.walletBalance.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.snackbarEvent.collectLatest { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
        }
    }

    // Top-level back press handling
    BackHandler(enabled = currentScreen != Screen.HOME) {
        viewModel.navigateBack()
    }

    // Modal Sheets & Dialogs
    if (showFilterSheet) {
        FilterBottomSheet(
            currentFilters = viewModel.filters.value,
            onDismiss = { viewModel.setFilterSheetVisible(false) },
            onApply = { newFilters ->
                viewModel.updateFilters(newFilters)
            },
            onReset = {
                viewModel.resetFilters()
            }
        )
    }

    if (showCheckoutDialog) {
        CheckoutDialog(
            viewModel = viewModel,
            onDismiss = { viewModel.setCheckoutDialogVisible(false) }
        )
    }

    placedOrder?.let { order ->
        OrderSuccessDialog(
            order = order,
            onTrackOrder = {
                viewModel.setPlacedOrder(null)
                viewModel.trackOrder(order)
            },
            onDismiss = {
                viewModel.setPlacedOrder(null)
                viewModel.navigateTo(Screen.HOME)
            }
        )
    }

    trackedOrder?.let { order ->
        OrderTrackingSheet(
            order = order,
            onDismiss = { viewModel.trackOrder(null) }
        )
    }

    if (showWalletDialog) {
        WalletTopUpDialog(
            currentBalance = walletBalance,
            onTopUp = { amount -> viewModel.topUpWallet(amount) },
            onDismiss = { viewModel.setWalletDialogVisible(false) }
        )
    }

    if (showAddressDialog) {
        AddressManagerDialog(
            addresses = addresses,
            selectedAddress = selectedAddress,
            onSelectAddress = { viewModel.selectAddress(it) },
            onAddAddress = { viewModel.addAddress(it) },
            onDismiss = { viewModel.setAddressDialogVisible(false) }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .padding(bottom = 80.dp)
                    .testTag("app_snackbar_host")
            )
        },
        bottomBar = {
            // Only show bottom navigation on primary screens
            if (currentScreen in listOf(Screen.HOME, Screen.EXPLORE, Screen.WISHLIST, Screen.ORDERS)) {
                NavigationBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("bottom_nav_bar"),
                    tonalElevation = 6.dp
                ) {
                    NavigationBarItem(
                        selected = currentScreen == Screen.HOME,
                        onClick = { viewModel.navigateTo(Screen.HOME) },
                        icon = {
                            Icon(
                                imageVector = if (currentScreen == Screen.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                                contentDescription = "Home"
                            )
                        },
                        label = { Text("Shop", fontSize = 12.sp) },
                        modifier = Modifier.testTag("nav_item_home")
                    )

                    NavigationBarItem(
                        selected = currentScreen == Screen.EXPLORE,
                        onClick = { viewModel.navigateTo(Screen.EXPLORE) },
                        icon = {
                            Icon(
                                imageVector = if (currentScreen == Screen.EXPLORE) Icons.Filled.Explore else Icons.Outlined.Explore,
                                contentDescription = "Explore"
                            )
                        },
                        label = { Text("Catalog", fontSize = 12.sp) },
                        modifier = Modifier.testTag("nav_item_explore")
                    )

                    NavigationBarItem(
                        selected = currentScreen == Screen.WISHLIST,
                        onClick = { viewModel.navigateTo(Screen.WISHLIST) },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (wishlistIds.isNotEmpty()) {
                                        Badge { Text("${wishlistIds.size}") }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (currentScreen == Screen.WISHLIST) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                    contentDescription = "Wishlist"
                                )
                            }
                        },
                        label = { Text("Wishlist", fontSize = 12.sp) },
                        modifier = Modifier.testTag("nav_item_wishlist")
                    )

                    NavigationBarItem(
                        selected = currentScreen == Screen.ORDERS,
                        onClick = { viewModel.navigateTo(Screen.ORDERS) },
                        icon = {
                            Icon(
                                imageVector = if (currentScreen == Screen.ORDERS) Icons.Filled.Person else Icons.Outlined.Person,
                                contentDescription = "Account"
                            )
                        },
                        label = { Text("Account", fontSize = 12.sp) },
                        modifier = Modifier.testTag("nav_item_orders")
                    )
                }
            }
        }
    ) { innerPadding ->
        AnimatedContent(
            targetState = currentScreen,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            modifier = Modifier.padding(innerPadding),
            label = "screen_transition"
        ) { targetScreen ->
            when (targetScreen) {
                Screen.HOME -> HomeScreen(viewModel = viewModel)
                Screen.EXPLORE -> ExploreScreen(viewModel = viewModel)
                Screen.WISHLIST -> WishlistScreen(viewModel = viewModel)
                Screen.ORDERS -> OrdersScreen(viewModel = viewModel)
                Screen.CART -> CartScreen(viewModel = viewModel)
                Screen.PRODUCT_DETAIL -> {
                    val product = viewModel.selectedProduct.collectAsState().value
                    if (product != null) {
                        ProductDetailScreen(product = product, viewModel = viewModel)
                    } else {
                        HomeScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}
