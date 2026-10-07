package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Money
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.EcommerceViewModel
import com.example.ui.Screen
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GoldenAmber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutDialog(
    viewModel: EcommerceViewModel,
    onDismiss: () -> Unit
) {
    val selectedAddress by viewModel.repository.selectedAddress.collectAsState()
    val cartItems by viewModel.repository.cartItems.collectAsState()
    val appliedCoupon by viewModel.repository.appliedCoupon.collectAsState()
    val walletBalance by viewModel.repository.walletBalance.collectAsState()

    var selectedSpeed by remember { mutableStateOf(DeliverySpeed.STANDARD) }
    var selectedPayment by remember { mutableStateOf(PaymentMethod.GOOGLE_PAY) }

    val subtotal = cartItems.sumOf { it.totalPrice }
    val discount = appliedCoupon?.let { (subtotal * it.discountPercent) / 100.0 } ?: 0.0
    val shipping = if (subtotal >= 75.0 && selectedSpeed == DeliverySpeed.STANDARD) 0.0 else selectedSpeed.cost
    val tax = (subtotal - discount).coerceAtLeast(0.0) * 0.08
    val grandTotal = (subtotal - discount + shipping + tax).coerceAtLeast(0.0)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        modifier = Modifier.testTag("checkout_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Checkout Order",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = "${cartItems.size} items",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Delivery Address Card
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Delivery Address",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
                TextButton(
                    onClick = { viewModel.setAddressDialogVisible(true) },
                    modifier = Modifier.testTag("change_address_btn")
                ) {
                    Text("Change", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                }
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = selectedAddress.recipientName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            Text(
                                text = selectedAddress.title,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = selectedAddress.street,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = selectedAddress.cityStateZip,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = selectedAddress.phone,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Shipping Speed Selector
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocalShipping,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Delivery Method",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            DeliverySpeed.values().forEach { speed ->
                val isSelected = selectedSpeed == speed
                val speedCost = if (subtotal >= 75.0 && speed == DeliverySpeed.STANDARD) "FREE" else "$${speed.cost}"
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { selectedSpeed = speed }
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.05f) else MaterialTheme.colorScheme.surface
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = speed.label,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = speed.estimatedDays,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = speedCost,
                            fontWeight = FontWeight.Bold,
                            color = if (speedCost == "FREE") EmeraldGreen else MaterialTheme.colorScheme.primary,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Payment Method Selector
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Payment,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Payment Option",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            PaymentMethod.values().forEach { method ->
                val isSelected = selectedPayment == method
                val subtitle = when (method) {
                    PaymentMethod.AURA_WALLET -> "Balance: $${String.format("%.2f", walletBalance)}"
                    else -> method.iconDescription
                }
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { selectedPayment = method }
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.05f) else MaterialTheme.colorScheme.surface
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = when (method) {
                                    PaymentMethod.GOOGLE_PAY -> Icons.Default.Payment
                                    PaymentMethod.CREDIT_CARD -> Icons.Default.CreditCard
                                    PaymentMethod.AURA_WALLET -> Icons.Outlined.AccountBalanceWallet
                                    PaymentMethod.CASH_ON_DELIVERY -> Icons.Outlined.Money
                                },
                                contentDescription = null,
                                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = method.label,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = subtitle,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        RadioButton(
                            selected = isSelected,
                            onClick = { selectedPayment = method },
                            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Order Confirmation Button
            Button(
                onClick = {
                    viewModel.placeOrder(selectedSpeed, selectedPayment)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("confirm_place_order_btn"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = "Pay $${String.format("%.2f", grandTotal)} & Complete",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
fun OrderSuccessDialog(
    order: OrderItem,
    onTrackOrder: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.testTag("order_success_dialog"),
        shape = RoundedCornerShape(24.dp),
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(EmeraldGreen.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Success",
                        tint = EmeraldGreen,
                        modifier = Modifier.size(40.dp)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Order Confirmed!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Thank you for shopping with Aura Store.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Order ID", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(order.orderId, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Estimated Delivery", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(order.estimatedArrival, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total Paid", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("$${String.format("%.2f", order.totalAmount)}", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onTrackOrder,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("track_confirmed_order_btn")
            ) {
                Text("Track Shipment", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("close_success_dialog_btn")
            ) {
                Text("Continue Shopping")
            }
        }
    )
}
