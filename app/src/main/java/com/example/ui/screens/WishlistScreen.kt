package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.EcommerceViewModel
import com.example.ui.Screen
import com.example.ui.components.ProductCard
import com.example.ui.theme.CoralPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistScreen(
    viewModel: EcommerceViewModel,
    modifier: Modifier = Modifier
) {
    val wishlistIds by viewModel.repository.wishlistIds.collectAsState()
    val allProducts = viewModel.repository.allProducts
    val wishlistedProducts = allProducts.filter { wishlistIds.contains(it.id) }

    BackHandler {
        viewModel.navigateBack()
    }

    Scaffold(
        modifier = modifier.testTag("wishlist_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "Wishlist",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = CircleShape,
                            color = CoralPrimary.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "${wishlistedProducts.size}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = CoralPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier.testTag("wishlist_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (wishlistedProducts.isNotEmpty()) {
                        TextButton(
                            onClick = {
                                wishlistedProducts.forEach { viewModel.addToCart(it) }
                            },
                            modifier = Modifier.testTag("add_all_wishlist_to_cart_btn")
                        ) {
                            Text("Add All to Bag", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        if (wishlistedProducts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(CoralPrimary.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Empty Wishlist",
                            tint = CoralPrimary,
                            modifier = Modifier.size(44.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Your wishlist is empty",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Save items you love by tapping the heart icon on any product card.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { viewModel.navigateTo(Screen.EXPLORE) },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.testTag("explore_from_empty_wishlist_btn")
                    ) {
                        Text("Explore Products", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp)
            ) {
                item {
                    Text(
                        text = "Saved for later",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                val chunks = wishlistedProducts.chunked(2)
                items(chunks) { pair ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        for (product in pair) {
                            ProductCard(
                                product = product,
                                isWishlisted = true,
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
    }
}
