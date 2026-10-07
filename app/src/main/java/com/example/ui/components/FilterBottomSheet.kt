package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.FilterState
import com.example.ui.SortOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    currentFilters: FilterState,
    onDismiss: () -> Unit,
    onApply: (FilterState) -> Unit,
    onReset: () -> Unit
) {
    var tempPrice by remember { mutableFloatStateOf(currentFilters.maxPrice.toFloat()) }
    var tempRating by remember { mutableFloatStateOf(currentFilters.minRating) }
    var tempInStock by remember { mutableStateOf(currentFilters.inStockOnly) }
    var tempSort by remember { mutableStateOf(currentFilters.sortOption) }

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
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filter & Sort",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                TextButton(
                    onClick = {
                        tempPrice = 500f
                        tempRating = 0f
                        tempInStock = false
                        tempSort = SortOption.POPULAR
                        onReset()
                    },
                    modifier = Modifier.testTag("reset_filters_btn")
                ) {
                    Text(
                        "Reset",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Sort By Options
            Text(
                text = "Sort By",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SortOption.values().take(2).forEach { option ->
                    FilterChip(
                        selected = tempSort == option,
                        onClick = { tempSort = option },
                        label = { Text(option.label, fontSize = 12.sp) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SortOption.values().drop(2).forEach { option ->
                    FilterChip(
                        selected = tempSort == option,
                        onClick = { tempSort = option },
                        label = { Text(option.label, fontSize = 12.sp) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Price range slider
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Max Price",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$${tempPrice.toInt()}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Slider(
                value = tempPrice,
                onValueChange = { tempPrice = it },
                valueRange = 50f..500f,
                steps = 9,
                modifier = Modifier.testTag("price_slider")
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Minimum Rating
            Text(
                text = "Minimum Rating",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(0f to "All", 4.0f to "4.0+ ★", 4.5f to "4.5+ ★", 4.8f to "4.8+ ★").forEach { (rating, label) ->
                    FilterChip(
                        selected = tempRating == rating,
                        onClick = { tempRating = rating },
                        label = { Text(label, fontSize = 12.sp) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // In Stock Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "In Stock Only",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Hide sold-out items",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = tempInStock,
                    onCheckedChange = { tempInStock = it },
                    modifier = Modifier.testTag("in_stock_switch")
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Apply Button
            Button(
                onClick = {
                    onApply(
                        FilterState(
                            maxPrice = tempPrice.toDouble(),
                            minRating = tempRating,
                            inStockOnly = tempInStock,
                            sortOption = tempSort
                        )
                    )
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("apply_filters_btn"),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Apply Filters", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }
    }
}
