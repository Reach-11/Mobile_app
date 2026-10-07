package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CartItem
import com.example.ui.EcommerceViewModel
import com.example.ui.Screen
import com.example.ui.theme.EmeraldGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    viewModel: EcommerceViewModel,
    modifier: Modifier = Modifier
) {
    val cartItems by viewModel.repository.cartItems.collectAsState()
    val appliedCoupon by viewModel.repository.appliedCoupon.collectAsState()

    var couponInput by remember { mutableStateOf("") }

    val subtotal = cartItems.sumOf { it.totalPrice }
    val discount = appliedCoupon?.let { (subtotal * it.discountPercent) / 100.0 } ?: 0.0
    val shipping = if (subtotal >= 75.0 || cartItems.isEmpty()) 0.0 else 5.99
    val tax = (subtotal - discount).coerceAtLeast(0.0) * 0.08
    val grandTotal = (subtotal - discount + shipping + tax).coerceAtLeast(0.0)

    BackHandler {
        viewModel.navigateBack()
    }

    Scaffold(
        modifier = modifier.testTag("cart_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "My Bag",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = "${cartItems.sumOf { it.quantity }}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier.testTag("cart_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (cartItems.isNotEmpty()) {
                        TextButton(
                            onClick = { viewModel.repository.clearCart() },
                            modifier = Modifier.testTag("clear_cart_btn")
                        ) {
                            Text("Clear", color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (cartItems.isNotEmpty()) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("cart_bottom_bar"),
                    tonalElevation = 8.dp,
                    shadowElevation = 16.dp,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp)
                            .navigationBarsPadding()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Estimated Total",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "$${String.format("%.2f", grandTotal)}",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Button(
                                onClick = { viewModel.setCheckoutDialogVisible(true) },
                                modifier = Modifier
                                    .height(50.dp)
                                    .widthIn(min = 180.dp)
                                    .testTag("checkout_btn"),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text(
                                    text = "Proceed to Checkout",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        if (cartItems.isEmpty()) {
            // Empty State
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
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = "Empty Bag",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.size(48.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Your bag is empty",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Explore our trending collection and find something extraordinary today.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { viewModel.navigateTo(Screen.EXPLORE) },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.testTag("empty_cart_explore_btn")
                    ) {
                        Text("Discover Products", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Free Shipping Progress Tracker
                item {
                    val threshold = 75.0
                    val progress = (subtotal / threshold).coerceIn(0.0, 1.0).toFloat()
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = if (subtotal >= threshold) "🎉 You unlocked FREE Shipping!" else "Add $${String.format("%.2f", threshold - subtotal)} for FREE Shipping",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (subtotal >= threshold) EmeraldGreen else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "$${threshold.toInt()}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = if (subtotal >= threshold) EmeraldGreen else MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.outlineVariant
                            )
                        }
                    }
                }

                // Cart Items
                items(cartItems, key = { it.cartItemId }) { item ->
                    CartItemCard(
                        item = item,
                        onQuantityChange = { newQty ->
                            viewModel.updateCartQuantity(item.cartItemId, newQty)
                        },
                        onRemove = {
                            viewModel.removeCartItem(item.cartItemId)
                        },
                        onItemClick = {
                            viewModel.openProductDetail(item.product)
                        }
                    )
                }

                // Promo Coupon Section
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocalOffer,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Promo Voucher",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            if (appliedCoupon != null) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(EmeraldGreen.copy(alpha = 0.12f))
                                        .padding(horizontal = 12.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = EmeraldGreen,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                text = "${appliedCoupon!!.code} (${appliedCoupon!!.discountPercent}% OFF)",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = EmeraldGreen
                                            )
                                            Text(
                                                text = appliedCoupon!!.description,
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                    IconButton(
                                        onClick = { viewModel.removeCoupon() },
                                        modifier = Modifier
                                            .size(28.dp)
                                            .testTag("remove_coupon_btn")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Remove coupon",
                                            tint = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            } else {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedTextField(
                                        value = couponInput,
                                        onValueChange = { couponInput = it },
                                        placeholder = { Text("Enter coupon code", fontSize = 13.sp) },
                                        singleLine = true,
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(50.dp)
                                            .testTag("coupon_input"),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(
                                        onClick = {
                                            viewModel.applyCoupon(couponInput)
                                            couponInput = ""
                                        },
                                        enabled = couponInput.isNotBlank(),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier
                                            .height(50.dp)
                                            .testTag("apply_coupon_btn")
                                    ) {
                                        Text("Apply")
                                    }
                                }

                                // Quick coupon suggestions
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    listOf("AURA20", "WELCOME10").forEach { code ->
                                        SuggestionChip(
                                            onClick = { viewModel.applyCoupon(code) },
                                            label = { Text(code, fontSize = 11.sp) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Cost Summary Card
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Order Summary",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            SummaryRow(label = "Subtotal", value = "$${String.format("%.2f", subtotal)}")

                            if (discount > 0) {
                                SummaryRow(
                                    label = "Coupon Discount",
                                    value = "-$${String.format("%.2f", discount)}",
                                    valueColor = EmeraldGreen
                                )
                            }

                            SummaryRow(
                                label = "Estimated Shipping",
                                value = if (shipping == 0.0) "FREE" else "$${String.format("%.2f", shipping)}",
                                valueColor = if (shipping == 0.0) EmeraldGreen else MaterialTheme.colorScheme.onSurface
                            )

                            SummaryRow(label = "Estimated Tax (8%)", value = "$${String.format("%.2f", tax)}")

                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 10.dp),
                                color = MaterialTheme.colorScheme.outlineVariant
                            )

                            SummaryRow(
                                label = "Total Amount",
                                value = "$${String.format("%.2f", grandTotal)}",
                                isBold = true,
                                valueColor = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemCard(
    item: CartItem,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit,
    onItemClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { onItemClick() },
                contentAlignment = Alignment.Center
            ) {
                if (item.product.drawableResId != null) {
                    Image(
                        painter = painterResource(id = item.product.drawableResId),
                        contentDescription = item.product.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(text = item.product.fallbackEmoji, fontSize = 32.sp)
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.product.title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            if (item.selectedColor != null) {
                                Text(
                                    text = item.selectedColor.name,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            if (item.selectedSize != null) {
                                Text(
                                    text = "• ${item.selectedSize}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    IconButton(
                        onClick = onRemove,
                        modifier = Modifier.size(28.dp).testTag("delete_item_${item.cartItemId}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Remove",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$${String.format("%.2f", item.totalPrice)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // Quantity Counter Pill
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { onQuantityChange(item.quantity - 1) },
                                modifier = Modifier.size(30.dp)
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(14.dp))
                            }
                            Text(
                                text = "${item.quantity}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(horizontal = 6.dp)
                            )
                            IconButton(
                                onClick = { onQuantityChange(item.quantity + 1) },
                                modifier = Modifier.size(30.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Increase", modifier = Modifier.size(14.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SummaryRow(
    label: String,
    value: String,
    isBold: Boolean = false,
    valueColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = if (isBold) MaterialTheme.typography.titleSmall else MaterialTheme.typography.bodyMedium,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            color = if (isBold) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = if (isBold) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
            fontWeight = if (isBold) FontWeight.ExtraBold else FontWeight.SemiBold,
            color = valueColor
        )
    }
}
