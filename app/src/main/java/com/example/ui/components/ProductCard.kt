package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Product
import com.example.ui.theme.CoralPrimary
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GoldenAmber

@Composable
fun ProductCard(
    product: Product,
    isWishlisted: Boolean,
    onProductClick: (Product) -> Unit,
    onWishlistToggle: (Product) -> Unit,
    onAddToCart: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .testTag("product_card_${product.id}")
            .clip(RoundedCornerShape(20.dp))
            .clickable { onProductClick(product) },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Product Visual Container with badges
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
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
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Modern typographic / illustrative fallback
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = product.fallbackEmoji,
                            fontSize = 38.sp
                        )
                    }
                }

                // Discount Pill
                if (product.discountPercent > 0) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(10.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = EmeraldGreen
                    ) {
                        Text(
                            text = "-${product.discountPercent}%",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                        )
                    }
                }

                // Wishlist Heart Button
                val heartColor by animateColorAsState(
                    targetValue = if (isWishlisted) CoralPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                    label = "heartColor"
                )

                IconButton(
                    onClick = { onWishlistToggle(product) },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .size(36.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                            shape = CircleShape
                        )
                        .testTag("wishlist_btn_${product.id}")
                ) {
                    Icon(
                        imageVector = if (isWishlisted) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Wishlist toggle",
                        tint = heartColor,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Rating Badge at bottom left of image
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp),
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Rating",
                            tint = GoldenAmber,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = String.format("%.1f", product.rating),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // Info Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = product.brand.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.8.sp
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = product.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "$${String.format("%.2f", product.price)}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        if (product.originalPrice > product.price) {
                            Text(
                                text = "$${String.format("%.2f", product.originalPrice)}",
                                style = MaterialTheme.typography.bodySmall,
                                textDecoration = TextDecoration.LineThrough,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                fontSize = 11.sp
                            )
                        }
                    }

                    // Add to cart quick button
                    FilledIconButton(
                        onClick = { onAddToCart(product) },
                        modifier = Modifier
                            .size(38.dp)
                            .testTag("add_to_cart_${product.id}"),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ShoppingBag,
                            contentDescription = "Add to cart",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
