package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Category
import com.example.ui.components.ProductArtIllustration
import com.example.ui.theme.ShopGreenContainer
import com.example.ui.theme.ShopGreenPrimary
import com.example.ui.theme.ShopTextMuted
import com.example.ui.theme.ShopTextPrimary
import com.example.ui.theme.ShopTextSecondary
import com.example.ui.viewmodel.ShopViewModel

@Composable
fun CategoriesScreen(
    viewModel: ShopViewModel,
    onCategorySelected: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories by viewModel.categories.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .testTag("categories_screen"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
    ) {
        item {
            Column(modifier = Modifier.padding(bottom = 16.dp)) {
                Text(
                    text = "Categories • सभी श्रेणियां",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = ShopTextPrimary
                )
                Text(
                    text = "Explore collections curated for your lifestyle",
                    fontSize = 13.sp,
                    color = ShopTextSecondary
                )
            }
        }

        items(categories) { category ->
            CategoryDetailedCard(
                category = category,
                onCardClick = { onCategorySelected(category.id, category.name) },
                onSubcategoryClick = { sub -> onCategorySelected(category.id, "$sub (${category.name})") }
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun CategoryDetailedCard(
    category: Category,
    onCardClick: () -> Unit,
    onSubcategoryClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("category_card_${category.id}")
            .clickable { onCardClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(ShopGreenContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        ProductArtIllustration(
                            categoryKey = when (category.id) {
                                "electronics" -> "earbuds"
                                "fashion" -> "tshirt"
                                "home" -> "mug"
                                "footwear" -> "shoes"
                                "beauty" -> "cream"
                                "organic" -> "honey"
                                else -> "general"
                            },
                            iconSize = 30.dp
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = category.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = ShopTextPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "• ${category.hindiName}",
                                fontSize = 12.sp,
                                color = ShopGreenPrimary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Text(
                            text = "${category.itemCount}+ Products Available",
                            fontSize = 12.sp,
                            color = ShopTextMuted
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Open Category",
                    tint = ShopGreenPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Promotional banner text
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ShopGreenContainer.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocalOffer,
                        contentDescription = null,
                        tint = ShopGreenPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = category.bannerText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ShopGreenPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Subcategory chips
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(category.subcategories) { sub ->
                    SuggestionChip(
                        onClick = { onSubcategoryClick(sub) },
                        label = { Text(sub, fontSize = 11.sp) },
                        colors = androidx.compose.material3.SuggestionChipDefaults.suggestionChipColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            labelColor = ShopTextPrimary
                        ),
                        border = null
                    )
                }
            }
        }
    }
}
