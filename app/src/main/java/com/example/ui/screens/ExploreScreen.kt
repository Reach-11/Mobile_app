package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.EcommerceViewModel
import com.example.ui.Screen
import com.example.ui.components.ProductCard
import com.example.ui.components.SearchBarWithFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    viewModel: EcommerceViewModel,
    modifier: Modifier = Modifier
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val filters by viewModel.filters.collectAsState()
    val filteredProducts by viewModel.filteredProducts.collectAsState()
    val wishlistIds by viewModel.repository.wishlistIds.collectAsState()

    BackHandler {
        if (searchQuery.isNotEmpty() || selectedCategory != "all") {
            viewModel.resetFilters()
        } else {
            viewModel.navigateBack()
        }
    }

    Scaffold(
        modifier = modifier.testTag("explore_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Explore Catalog",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier.testTag("explore_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.setFilterSheetVisible(true) },
                        modifier = Modifier.testTag("explore_filter_icon")
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Sort and Filters",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Search & Category Chips
            item {
                SearchBarWithFilter(
                    query = searchQuery,
                    onQueryChange = { viewModel.setSearchQuery(it) },
                    categories = viewModel.repository.categories,
                    selectedCategory = selectedCategory,
                    onCategorySelected = { viewModel.selectCategory(it) },
                    onFilterClick = { viewModel.setFilterSheetVisible(true) },
                    hasActiveFilters = filters.minRating > 0f || filters.maxPrice < 500.0 || filters.inStockOnly
                )
            }

            // Results count and active sort label
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${filteredProducts.size} Products Found",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    AssistChip(
                        onClick = { viewModel.setFilterSheetVisible(true) },
                        label = { Text(filters.sortOption.label, fontSize = 12.sp) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                        },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("active_sort_chip")
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Empty state if no results match
            if (filteredProducts.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SentimentDissatisfied,
                            contentDescription = "No products found",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No matching items",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Try adjusting your search terms or relaxing the filters.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = { viewModel.resetFilters() },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("reset_empty_search_btn")
                        ) {
                            Text("Reset All Filters")
                        }
                    }
                }
            } else {
                // 2-column grid of products
                val chunks = filteredProducts.chunked(2)
                items(chunks) { pair ->
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
    }
}
