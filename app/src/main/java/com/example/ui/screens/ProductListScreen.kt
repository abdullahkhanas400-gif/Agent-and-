package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Product
import com.example.ui.components.ProductCard
import com.example.ui.theme.ShopGreenContainer
import com.example.ui.theme.ShopGreenPrimary
import com.example.ui.theme.ShopTextMuted
import com.example.ui.theme.ShopTextPrimary
import com.example.ui.theme.ShopTextSecondary
import com.example.ui.viewmodel.ShopViewModel

enum class ProductSort {
    DEFAULT,
    PRICE_LOW_HIGH,
    PRICE_HIGH_LOW,
    RATING,
    DISCOUNT
}

@Composable
fun ProductListScreen(
    categoryId: String?,
    categoryName: String?,
    initialQuery: String?,
    viewModel: ShopViewModel,
    onProductClick: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var searchQuery by remember { mutableStateOf(initialQuery ?: "") }
    var selectedSort by remember { mutableStateOf(ProductSort.DEFAULT) }

    val allProducts by viewModel.products.collectAsState()
    val wishlist by viewModel.wishlist.collectAsState()

    // Filter by Category
    val categoryFiltered = if (categoryId.isNullOrBlank() || categoryId == "all") {
        allProducts
    } else {
        allProducts.filter { it.categoryId.equals(categoryId, ignoreCase = true) }
    }

    // Filter by Search Query
    val searchFiltered = if (searchQuery.isBlank()) {
        categoryFiltered
    } else {
        categoryFiltered.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
            it.categoryName.contains(searchQuery, ignoreCase = true) ||
            it.description.contains(searchQuery, ignoreCase = true)
        }
    }

    // Sort
    val sortedProducts = when (selectedSort) {
        ProductSort.DEFAULT -> searchFiltered
        ProductSort.PRICE_LOW_HIGH -> searchFiltered.sortedBy { it.price }
        ProductSort.PRICE_HIGH_LOW -> searchFiltered.sortedByDescending { it.price }
        ProductSort.RATING -> searchFiltered.sortedByDescending { it.rating }
        ProductSort.DISCOUNT -> searchFiltered.sortedByDescending { it.discountPercent }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .testTag("product_list_screen")
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("product_list_back_btn")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = ShopTextPrimary
                )
            }

            // Search Bar Input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(categoryName ?: "Search products...", fontSize = 13.sp, color = ShopTextMuted)
                },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = ShopGreenPrimary, modifier = Modifier.size(20.dp))
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear", tint = ShopTextMuted, modifier = Modifier.size(18.dp))
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .testTag("search_text_input"),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ShopGreenPrimary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )

            Spacer(modifier = Modifier.width(8.dp))
        }

        // Sorting & Filter Chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedSort == ProductSort.DEFAULT,
                    onClick = { selectedSort = ProductSort.DEFAULT },
                    label = { Text("Popular") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ShopGreenContainer,
                        selectedLabelColor = ShopGreenPrimary
                    )
                )
            }
            item {
                FilterChip(
                    selected = selectedSort == ProductSort.PRICE_LOW_HIGH,
                    onClick = { selectedSort = ProductSort.PRICE_LOW_HIGH },
                    label = { Text("Price: Low to High") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ShopGreenContainer,
                        selectedLabelColor = ShopGreenPrimary
                    )
                )
            }
            item {
                FilterChip(
                    selected = selectedSort == ProductSort.PRICE_HIGH_LOW,
                    onClick = { selectedSort = ProductSort.PRICE_HIGH_LOW },
                    label = { Text("Price: High to Low") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ShopGreenContainer,
                        selectedLabelColor = ShopGreenPrimary
                    )
                )
            }
            item {
                FilterChip(
                    selected = selectedSort == ProductSort.RATING,
                    onClick = { selectedSort = ProductSort.RATING },
                    label = { Text("Top Rated (★)") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ShopGreenContainer,
                        selectedLabelColor = ShopGreenPrimary
                    )
                )
            }
            item {
                FilterChip(
                    selected = selectedSort == ProductSort.DISCOUNT,
                    onClick = { selectedSort = ProductSort.DISCOUNT },
                    label = { Text("Best Discounts") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ShopGreenContainer,
                        selectedLabelColor = ShopGreenPrimary
                    )
                )
            }
        }

        // Results count header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${sortedProducts.size} Products found",
                fontSize = 12.sp,
                color = ShopTextSecondary,
                fontWeight = FontWeight.Medium
            )
        }

        // Content
        if (sortedProducts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.SearchOff,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = ShopTextMuted
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No matching products found",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = ShopTextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Try searching with other keywords or clear filter",
                        fontSize = 13.sp,
                        color = ShopTextSecondary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            searchQuery = ""
                            selectedSort = ProductSort.DEFAULT
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ShopGreenPrimary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Reset Search")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                val chunks = sortedProducts.chunked(2)
                items(chunks) { chunk ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        chunk.forEach { product ->
                            Box(modifier = Modifier.weight(1f)) {
                                ProductCard(
                                    product = product,
                                    isWishlisted = wishlist.contains(product.id),
                                    onProductClick = { onProductClick(product.id) },
                                    onAddToCartClick = { viewModel.addToCart(product) },
                                    onToggleWishlist = { viewModel.toggleWishlist(product.id) }
                                )
                            }
                        }
                        if (chunk.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}
