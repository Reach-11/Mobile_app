package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Product
import com.example.data.model.ProductColor
import com.example.ui.EcommerceViewModel
import com.example.ui.Screen
import com.example.ui.theme.CoralPrimary
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GoldenAmber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    product: Product,
    viewModel: EcommerceViewModel,
    modifier: Modifier = Modifier
) {
    val wishlistIds by viewModel.repository.wishlistIds.collectAsState()
    val isWishlisted = wishlistIds.contains(product.id)

    var selectedColor by remember { mutableStateOf<ProductColor?>(product.colors.firstOrNull()) }
    var selectedSize by remember { mutableStateOf<String?>(product.sizes.firstOrNull()) }
    var quantity by remember { mutableIntStateOf(1) }

    BackHandler {
        viewModel.navigateBack()
    }

    Scaffold(
        modifier = modifier.testTag("product_detail_screen"),
        topBar = {
            TopAppBar(
                title = { Text(product.brand, style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier.testTag("detail_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.toggleWishlist(product) },
                        modifier = Modifier.testTag("detail_wishlist_btn")
                    ) {
                        Icon(
                            imageVector = if (isWishlisted) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Wishlist",
                            tint = if (isWishlisted) CoralPrimary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = { /* Share affordance */ }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share"
                        )
                    }
                }
            )
        },
        bottomBar = {
            // Sticky Bottom Action Bar
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("detail_bottom_bar"),
                tonalElevation = 8.dp,
                shadowElevation = 16.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                        .navigationBarsPadding(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Total Price Column
                    Column(modifier = Modifier.widthIn(min = 90.dp)) {
                        Text(
                            text = "Total Price",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "$${String.format("%.2f", product.price * quantity)}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Add to Cart Button
                    OutlinedButton(
                        onClick = {
                            viewModel.addToCart(product, quantity, selectedColor, selectedSize)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("detail_add_to_cart_btn"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = Brush.horizontalGradient(listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primary))
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ShoppingBag,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Add to Cart", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }

                    // Buy Now Button
                    Button(
                        onClick = {
                            viewModel.addToCart(product, quantity, selectedColor, selectedSize)
                            viewModel.setCheckoutDialogVisible(true)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("detail_buy_now_btn"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Buy Now", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Hero Image Presentation
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                    MaterialTheme.colorScheme.surfaceVariant
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (product.drawableResId != null) {
                        Image(
                            painter = painterResource(id = product.drawableResId),
                            contentDescription = product.title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(140.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = product.fallbackEmoji, fontSize = 64.sp)
                        }
                    }

                    // Top Badges
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.TopCenter)
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (product.discountPercent > 0) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = EmeraldGreen
                            ) {
                                Text(
                                    text = "SAVE ${product.discountPercent}%",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                        } else {
                            Spacer(modifier = Modifier.width(1.dp))
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                        ) {
                            Text(
                                text = if (product.inStock) "In Stock" else "Sold Out",
                                color = if (product.inStock) EmeraldGreen else Color.Red,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // Title, Rating, and Price Section
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = product.brand.uppercase(),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = product.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Rating Row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = GoldenAmber.copy(alpha = 0.15f)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = GoldenAmber,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${product.rating}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = GoldenAmber
                                )
                            }
                        }

                        Text(
                            text = "(${product.reviewCount} verified reviews)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Price and Discount row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "$${String.format("%.2f", product.price)}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        if (product.originalPrice > product.price) {
                            Text(
                                text = "$${String.format("%.2f", product.originalPrice)}",
                                style = MaterialTheme.typography.titleMedium,
                                textDecoration = TextDecoration.LineThrough,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = EmeraldGreen.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "You save $${String.format("%.2f", product.originalPrice - product.price)}",
                                    color = EmeraldGreen,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Color Variant Selector
            if (product.colors.isNotEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Color",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = selectedColor?.name ?: "",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            product.colors.forEach { color ->
                                val isSelected = selectedColor?.name == color.name
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(Color(color.hexColor))
                                        .border(
                                            width = if (isSelected) 3.dp else 1.dp,
                                            color = if (isSelected) MaterialTheme.colorScheme.primary else Color(0x33888888),
                                            shape = CircleShape
                                        )
                                        .clickable { selectedColor = color }
                                        .testTag("color_swatch_${color.name}"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Selected",
                                            tint = if (color.hexColor == 0xFFE2E8F0L || color.hexColor == 0xFFFFFFFFL) Color.Black else Color.White,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Size Variant Selector
            if (product.sizes.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "Size / Variant",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            product.sizes.forEach { size ->
                                val isSelected = selectedSize == size
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { selectedSize = size },
                                    label = { Text(size, fontWeight = FontWeight.SemiBold) },
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.testTag("size_chip_$size")
                                )
                            }
                        }
                    }
                }
            }

            // Quantity Counter
            item {
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Quantity",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(4.dp)
                        ) {
                            IconButton(
                                onClick = { if (quantity > 1) quantity-- },
                                modifier = Modifier
                                    .size(36.dp)
                                    .testTag("detail_qty_minus")
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "Decrease quantity")
                            }

                            Text(
                                text = "$quantity",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )

                            IconButton(
                                onClick = { quantity++ },
                                modifier = Modifier
                                    .size(36.dp)
                                    .testTag("detail_qty_plus")
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Increase quantity")
                            }
                        }
                    }
                }
            }

            // Perks / Guarantee Row
            item {
                Spacer(modifier = Modifier.height(18.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.LocalShipping,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("Free Shipping", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text("On orders $75+", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }

                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Security,
                                contentDescription = null,
                                tint = EmeraldGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("2-Yr Warranty", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text("30-day returns", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }

            // Description & Highlights
            item {
                Spacer(modifier = Modifier.height(18.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Overview",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = product.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Key Highlights",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    product.highlights.forEach { highlight ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = EmeraldGreen,
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = highlight,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            // Technical Specs
            if (product.specs.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(18.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "Specifications",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                product.specs.entries.forEachIndexed { index, (key, value) ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 6.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = key,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = value,
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                    if (index < product.specs.size - 1) {
                                        HorizontalDivider(
                                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                                            modifier = Modifier.padding(vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Customer Reviews List
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Customer Reviews",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            items(viewModel.repository.mockReviews.size) { index ->
                val review = viewModel.repository.mockReviews[index]
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = review.authorName.take(1),
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = review.authorName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = review.date,
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                repeat(5) { starIndex ->
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = if (starIndex < review.rating.toInt()) GoldenAmber else MaterialTheme.colorScheme.outlineVariant,
                                        modifier = Modifier.size(13.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = review.comment,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}
