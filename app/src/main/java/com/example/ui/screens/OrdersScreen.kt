package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OrderItem
import com.example.data.model.OrderStatus
import com.example.ui.EcommerceViewModel
import com.example.ui.theme.CoralPrimary
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GoldenAmber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    viewModel: EcommerceViewModel,
    modifier: Modifier = Modifier
) {
    val orders by viewModel.repository.orders.collectAsState()
    val walletBalance by viewModel.repository.walletBalance.collectAsState()
    val loyaltyPoints by viewModel.repository.loyaltyPoints.collectAsState()

    BackHandler {
        viewModel.navigateBack()
    }

    Scaffold(
        modifier = modifier.testTag("orders_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "My Account & Orders",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier.testTag("orders_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
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
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile & VIP Card
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(
                                            listOf(MaterialTheme.colorScheme.primary, GoldenAmber)
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Profile",
                                    tint = Color.White,
                                    modifier = Modifier.size(32.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Alex Rivera",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = GoldenAmber.copy(alpha = 0.2f)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = null,
                                                tint = GoldenAmber,
                                                modifier = Modifier.size(11.dp)
                                            )
                                            Spacer(modifier = Modifier.width(3.dp))
                                            Text(
                                                text = "Gold Elite",
                                                color = GoldenAmber,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = "alex.rivera@example.com",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        Spacer(modifier = Modifier.height(14.dp))

                        // Wallet & Loyalty Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AccountBalanceWallet,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Aura Wallet",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "$${String.format("%.2f", walletBalance)}",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            Button(
                                onClick = { viewModel.setWalletDialogVisible(true) },
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                modifier = Modifier.testTag("top_up_wallet_btn")
                            ) {
                                Text("+ Top Up", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Reward Loyalty Points",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "$loyaltyPoints PTS",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = GoldenAmber
                                )
                            }
                        }
                    }
                }
            }

            // Quick Actions: Saved Addresses & Help
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.setAddressDialogVisible(true) },
                        modifier = Modifier.weight(1f).testTag("saved_addresses_btn"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Addresses", fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = { /* Help & FAQ affordance */ },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Outlined.HelpOutline, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Support FAQ", fontSize = 12.sp)
                    }
                }
            }

            // Order History Header
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Orders",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${orders.size} Orders",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // List of Orders
            items(orders, key = { it.orderId }) { order ->
                OrderCard(
                    order = order,
                    onTrackClick = { viewModel.trackOrder(order) },
                    onReorderClick = {
                        order.items.forEach { viewModel.addToCart(it.product, it.quantity, it.selectedColor, it.selectedSize) }
                    }
                )
            }
        }
    }
}

@Composable
fun OrderCard(
    order: OrderItem,
    onTrackClick: () -> Unit,
    onReorderClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header with Order ID & Status Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Order #${order.orderId}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = order.orderDate,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                val statusColor = when (order.status) {
                    OrderStatus.DELIVERED -> EmeraldGreen
                    OrderStatus.IN_TRANSIT -> CoralPrimary
                    else -> MaterialTheme.colorScheme.primary
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = statusColor.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = order.status.label,
                        color = statusColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Items breakdown
            order.items.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${item.quantity}x",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = item.product.title,
                            fontSize = 12.sp,
                            maxLines = 1
                        )
                    }
                    Text(
                        text = "$${String.format("%.2f", item.totalPrice)}",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(10.dp))

            // Footer with Total and Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Total Paid",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "$${String.format("%.2f", order.totalAmount)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = onReorderClick,
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Reorder", fontSize = 12.sp)
                    }

                    Button(
                        onClick = onTrackClick,
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("track_order_${order.orderId}")
                    ) {
                        Icon(Icons.Default.LocalShipping, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Track", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderTrackingSheet(
    order: OrderItem,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "Shipment Tracking",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Tracking Code: ${order.trackingCode}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Timeline Steps
            val steps = listOf(
                "Order Placed" to "Order verified & confirmed",
                "Packaging" to "Item prepared at warehouse",
                "In Transit" to "Handed over to courier express",
                "Out for Delivery" to "Courier driver on the route",
                "Delivered" to "Delivered to your doorstep"
            )

            steps.forEachIndexed { index, (title, desc) ->
                val isCompleted = index <= order.status.stepIndex
                val isCurrent = index == order.status.stepIndex

                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isCompleted) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        if (index < steps.size - 1) {
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(36.dp)
                                    .background(
                                        if (index < order.status.stepIndex) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                                    )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = title,
                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = desc,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(18.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Close Tracking")
            }
        }
    }
}
