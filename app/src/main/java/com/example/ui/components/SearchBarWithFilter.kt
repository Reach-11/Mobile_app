package com.example.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CategoryItem

@Composable
fun SearchBarWithFilter(
    query: String,
    onQueryChange: (String) -> Unit,
    categories: List<CategoryItem>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    onFilterClick: () -> Unit,
    hasActiveFilters: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Search text field + Filter button row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = {
                    Text(
                        "Search headphones, watches, shoes...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search icon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { onQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear search",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp)
                    .testTag("search_input")
            )

            Spacer(modifier = Modifier.width(10.dp))

            // Filter button with badge
            BadgedBox(
                badge = {
                    if (hasActiveFilters) {
                        Badge(containerColor = MaterialTheme.colorScheme.primary)
                    }
                }
            ) {
                FilledTonalIconButton(
                    onClick = onFilterClick,
                    modifier = Modifier
                        .size(54.dp)
                        .testTag("filter_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = if (hasActiveFilters)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Tune,
                        contentDescription = "Filters",
                        tint = if (hasActiveFilters)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Horizontal Category Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { category ->
                val isSelected = selectedCategory == category.id
                FilterChip(
                    selected = isSelected,
                    onClick = { onCategorySelected(category.id) },
                    label = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = category.emoji, fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = category.name,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 13.sp
                            )
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = Color.White,
                        containerColor = MaterialTheme.colorScheme.surface,
                        labelColor = MaterialTheme.colorScheme.onSurface
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                    ),
                    modifier = Modifier.testTag("category_chip_${category.id}")
                )
            }
        }
    }
}
