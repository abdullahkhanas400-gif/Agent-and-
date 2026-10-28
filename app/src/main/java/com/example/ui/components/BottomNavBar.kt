package com.example.ui.components

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.navigation.BottomTab
import com.example.ui.theme.ShopAccentCoral
import com.example.ui.theme.ShopGreenContainer
import com.example.ui.theme.ShopGreenPrimary
import com.example.ui.theme.ShopTextMuted
import com.example.ui.theme.ShopTextSecondary

@Composable
fun ShopBottomNavBar(
    activeTab: BottomTab,
    cartItemCount: Int,
    onTabSelected: (BottomTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .navigationBarsPadding()
            .testTag("shop_bottom_nav_bar"),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        // Home
        NavigationBarItem(
            selected = activeTab == BottomTab.HOME,
            onClick = { onTabSelected(BottomTab.HOME) },
            icon = {
                Icon(
                    imageVector = if (activeTab == BottomTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Home",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = {
                Text(
                    text = "Home",
                    fontSize = 11.sp,
                    fontWeight = if (activeTab == BottomTab.HOME) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ShopGreenPrimary,
                selectedTextColor = ShopGreenPrimary,
                indicatorColor = ShopGreenContainer,
                unselectedIconColor = ShopTextMuted,
                unselectedTextColor = ShopTextSecondary
            ),
            modifier = Modifier.testTag("tab_home")
        )

        // Categories
        NavigationBarItem(
            selected = activeTab == BottomTab.CATEGORIES,
            onClick = { onTabSelected(BottomTab.CATEGORIES) },
            icon = {
                Icon(
                    imageVector = if (activeTab == BottomTab.CATEGORIES) Icons.Filled.Category else Icons.Outlined.Category,
                    contentDescription = "Categories",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = {
                Text(
                    text = "Categories",
                    fontSize = 11.sp,
                    fontWeight = if (activeTab == BottomTab.CATEGORIES) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ShopGreenPrimary,
                selectedTextColor = ShopGreenPrimary,
                indicatorColor = ShopGreenContainer,
                unselectedIconColor = ShopTextMuted,
                unselectedTextColor = ShopTextSecondary
            ),
            modifier = Modifier.testTag("tab_categories")
        )

        // Cart with Badge
        NavigationBarItem(
            selected = activeTab == BottomTab.CART,
            onClick = { onTabSelected(BottomTab.CART) },
            icon = {
                BadgedBox(
                    badge = {
                        if (cartItemCount > 0) {
                            Badge(
                                containerColor = ShopAccentCoral,
                                contentColor = Color.White
                            ) {
                                Text(
                                    text = if (cartItemCount > 99) "99+" else cartItemCount.toString(),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (activeTab == BottomTab.CART) Icons.Filled.ShoppingCart else Icons.Outlined.ShoppingCart,
                        contentDescription = "Cart",
                        modifier = Modifier.size(22.dp)
                    )
                }
            },
            label = {
                Text(
                    text = "Cart",
                    fontSize = 11.sp,
                    fontWeight = if (activeTab == BottomTab.CART) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ShopGreenPrimary,
                selectedTextColor = ShopGreenPrimary,
                indicatorColor = ShopGreenContainer,
                unselectedIconColor = ShopTextMuted,
                unselectedTextColor = ShopTextSecondary
            ),
            modifier = Modifier.testTag("tab_cart")
        )

        // Orders / Tracking
        NavigationBarItem(
            selected = activeTab == BottomTab.ORDERS,
            onClick = { onTabSelected(BottomTab.ORDERS) },
            icon = {
                Icon(
                    imageVector = if (activeTab == BottomTab.ORDERS) Icons.Filled.LocalShipping else Icons.Outlined.LocalShipping,
                    contentDescription = "Orders",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = {
                Text(
                    text = "Orders",
                    fontSize = 11.sp,
                    fontWeight = if (activeTab == BottomTab.ORDERS) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ShopGreenPrimary,
                selectedTextColor = ShopGreenPrimary,
                indicatorColor = ShopGreenContainer,
                unselectedIconColor = ShopTextMuted,
                unselectedTextColor = ShopTextSecondary
            ),
            modifier = Modifier.testTag("tab_orders")
        )

        // Profile
        NavigationBarItem(
            selected = activeTab == BottomTab.PROFILE,
            onClick = { onTabSelected(BottomTab.PROFILE) },
            icon = {
                Icon(
                    imageVector = if (activeTab == BottomTab.PROFILE) Icons.Filled.Person else Icons.Outlined.Person,
                    contentDescription = "Profile",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = {
                Text(
                    text = "Profile",
                    fontSize = 11.sp,
                    fontWeight = if (activeTab == BottomTab.PROFILE) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ShopGreenPrimary,
                selectedTextColor = ShopGreenPrimary,
                indicatorColor = ShopGreenContainer,
                unselectedIconColor = ShopTextMuted,
                unselectedTextColor = ShopTextSecondary
            ),
            modifier = Modifier.testTag("tab_profile")
        )
    }
}
