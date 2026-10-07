package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Product
import com.example.ui.EcommerceViewModel
import com.example.ui.Screen
import com.example.ui.components.HeroBanner
import com.example.ui.components.ProductCard
import com.example.ui.components.SearchBarWithFilter
import com.example.ui.theme.CoralPrimary
import com.example.ui.theme.GoldenAmber

@Composable
fun HomeScreen(
    viewModel: EcommerceViewModel,
    modifier: Modifier = Modifier
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val wishlistIds by viewModel.repository.wishlistIds.collectAsState()
    val cartItems by viewModel.repository.cartItems.collectAsState()
    val filters by viewModel.filters.collectAsState()
    val products = viewModel.repository.allProducts

    val flashDeals = products.filter { it.isFlashDeal }
    val featuredProducts = products.filter { it.isFeatured }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen"),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // App Header Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "A",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "AURA STORE",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Curated Lifestyle & Gear",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Notification & Cart Action Icons
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = { /* Demo notification affordance */ },
                        modifier = Modifier.testTag("notifications_btn")
                    ) {
                        BadgedBox(
                            badge = { Badge { Text("2") } }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    FilledTonalIconButton(
                        onClick = { viewModel.navigateTo(Screen.CART) },
                        modifier = Modifier.testTag("home_cart_btn")
                    ) {
                        BadgedBox(
                            badge = {
                                if (cartItems.isNotEmpty()) {
                                    Badge(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    ) {
                                        Text("${cartItems.sumOf { it.quantity }}")
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ShoppingBag,
                                contentDescription = "Cart",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }

        // Search Bar & Category Chips
        item {
            SearchBarWithFilter(
                query = searchQuery,
                onQueryChange = {
                    viewModel.setSearchQuery(it)
                    if (it.isNotEmpty()) {
                        viewModel.navigateTo(Screen.EXPLORE)
                    }
                },
                categories = viewModel.repository.categories,
                selectedCategory = selectedCategory,
                onCategorySelected = { cat ->
                    viewModel.selectCategory(cat)
                    if (cat != "all") {
                        viewModel.navigateTo(Screen.EXPLORE)
                    }
                },
                onFilterClick = { viewModel.setFilterSheetVisible(true) },
                hasActiveFilters = filters.minRating > 0f || filters.maxPrice < 500.0 || filters.inStockOnly
            )
        }

        // Hero Promotional Banner with Countdown
        item {
            Spacer(modifier = Modifier.height(16.dp))
            HeroBanner(
                onExploreClick = { viewModel.navigateTo(Screen.EXPLORE) }
            )
        }

        // Section: Flash Deals (Horizontal Carousel)
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "⚡ Flash Deals",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = CoralPrimary.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "Limited Stock",
                            color = CoralPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                TextButton(
                    onClick = { viewModel.navigateTo(Screen.EXPLORE) },
                    modifier = Modifier.testTag("see_all_flash_btn")
                ) {
                    Text("See All", color = MaterialTheme.colorScheme.primary, fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(flashDeals) { product ->
                    ProductCard(
                        product = product,
                        isWishlisted = wishlistIds.contains(product.id),
                        onProductClick = { viewModel.openProductDetail(it) },
                        onWishlistToggle = { viewModel.toggleWishlist(it) },
                        onAddToCart = { viewModel.addToCart(it) },
                        modifier = Modifier.width(200.dp)
                    )
                }
            }
        }

        // Section: Featured Drops (2-column layout)
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🔥 Trending Now",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                TextButton(
                    onClick = { viewModel.navigateTo(Screen.EXPLORE) },
                    modifier = Modifier.testTag("see_all_trending_btn")
                ) {
                    Text("Explore More", color = MaterialTheme.colorScheme.primary, fontSize = 13.sp)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // 2-Column Grid for Featured Items
        val chunkedProducts = featuredProducts.chunked(2)
        items(chunkedProducts) { pair ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                for (product in pair) {
                    ProductCard(
                        product = product,
                        isWishlisted = wishlistIds.contains(product.id),
                        onProductClick = { viewModel.openProductDetail(it) },
                        onWishlistToggle = { viewModel.toggleWishlist(it) },
                        onAddToCart = { viewModel.addToCart(it) },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (pair.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
