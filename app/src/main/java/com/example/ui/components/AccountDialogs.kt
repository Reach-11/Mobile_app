package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Address
import com.example.ui.EcommerceViewModel
import java.util.UUID

@Composable
fun WalletTopUpDialog(
    currentBalance: Double,
    onTopUp: (Double) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedAmount by remember { mutableStateOf(50.0) }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        title = {
            Text(
                text = "Top Up Aura Wallet",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Current Balance: $${String.format("%.2f", currentBalance)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Choose Top-up Amount",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(25.0, 50.0, 100.0, 200.0).forEach { amount ->
                        FilterChip(
                            selected = selectedAmount == amount,
                            onClick = { selectedAmount = amount },
                            label = { Text("+$${amount.toInt()}", fontWeight = FontWeight.Bold) },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onTopUp(selectedAmount) },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("confirm_wallet_top_up_btn")
            ) {
                Text("Add $${selectedAmount.toInt()} to Wallet", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun AddressManagerDialog(
    addresses: List<Address>,
    selectedAddress: Address,
    onSelectAddress: (Address) -> Unit,
    onAddAddress: (Address) -> Unit,
    onDismiss: () -> Unit
) {
    var isAddingNew by remember { mutableStateOf(false) }

    var titleInput by remember { mutableStateOf("") }
    var nameInput by remember { mutableStateOf("") }
    var streetInput by remember { mutableStateOf("") }
    var cityInput by remember { mutableStateOf("") }
    var phoneInput by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        title = {
            Text(
                text = if (isAddingNew) "Add New Address" else "Delivery Addresses",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            if (isAddingNew) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = titleInput,
                        onValueChange = { titleInput = it },
                        label = { Text("Label (e.g. Home, Office)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        label = { Text("Full Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = streetInput,
                        onValueChange = { streetInput = it },
                        label = { Text("Street Address") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = cityInput,
                        onValueChange = { cityInput = it },
                        label = { Text("City, State & ZIP") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = phoneInput,
                        onValueChange = { phoneInput = it },
                        label = { Text("Phone Number") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(addresses) { addr ->
                        val isSelected = addr.id == selectedAddress.id
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelectAddress(addr)
                                    onDismiss()
                                },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            border = androidx.compose.foundation.BorderStroke(
                                if (isSelected) 2.dp else 1.dp,
                                if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(addr.recipientName, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = MaterialTheme.colorScheme.surface
                                        ) {
                                            Text(addr.title, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                                        }
                                    }
                                    Text(addr.street, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(addr.cityStateZip, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                if (isSelected) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                    }

                    item {
                        OutlinedButton(
                            onClick = { isAddingNew = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Add Another Address")
                        }
                    }
                }
            }
        },
        confirmButton = {
            if (isAddingNew) {
                Button(
                    onClick = {
                        if (nameInput.isNotBlank() && streetInput.isNotBlank()) {
                            val newAddr = Address(
                                id = UUID.randomUUID().toString(),
                                title = if (titleInput.isBlank()) "Other" else titleInput,
                                recipientName = nameInput,
                                street = streetInput,
                                cityStateZip = cityInput,
                                phone = phoneInput
                            )
                            onAddAddress(newAddr)
                            isAddingNew = false
                        }
                    },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Save Address")
                }
            } else {
                TextButton(onClick = onDismiss) {
                    Text("Done")
                }
            }
        },
        dismissButton = {
            if (isAddingNew) {
                TextButton(onClick = { isAddingNew = false }) {
                    Text("Back")
                }
            }
        }
    )
}
