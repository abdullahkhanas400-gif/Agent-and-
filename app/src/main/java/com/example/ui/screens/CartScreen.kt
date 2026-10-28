package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RemoveShoppingCart
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CartItem
import com.example.ui.components.ProductArtIllustration
import com.example.ui.theme.ShopAccentCoral
import com.example.ui.theme.ShopGreenContainer
import com.example.ui.theme.ShopGreenPrimary
import com.example.ui.theme.ShopTextMuted
import com.example.ui.theme.ShopTextPrimary
import com.example.ui.theme.ShopTextSecondary
import com.example.ui.viewmodel.ShopViewModel

@Composable
fun CartScreen(
    viewModel: ShopViewModel,
    onProceedToCheckout: () -> Unit,
    onContinueShopping: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cartItems by viewModel.cart.collectAsState()
    val appliedCoupon by viewModel.appliedCoupon.collectAsState()
    val (subtotal, discount, deliveryFee) = viewModel.cartSummary.collectAsState().value
    val totalAmount = (subtotal - discount + deliveryFee).coerceAtLeast(0.0)

    var couponInput by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .testTag("cart_screen")
    ) {
        if (cartItems.isEmpty()) {
            // Empty Cart State
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(ShopGreenContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.RemoveShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(52.dp),
                        tint = ShopGreenPrimary
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Your Cart is Empty",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = ShopTextPrimary
                )

                Text(
                    text = "आपकी कार्ट खाली है • अपनी मनपसंद चीजें जोड़ें",
                    fontSize = 13.sp,
                    color = ShopTextSecondary,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onContinueShopping,
                    colors = ButtonDefaults.buttonColors(containerColor = ShopGreenPrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(48.dp)
                ) {
                    Text("Explore Store • शॉपिंग करें", fontWeight = FontWeight.Bold)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 120.dp)
            ) {
                // Header
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Shopping Cart",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = ShopTextPrimary
                            )
                            Text(
                                text = "${cartItems.sumOf { it.quantity }} items in your bag",
                                fontSize = 12.sp,
                                color = ShopTextSecondary
                            )
                        }

                        TextButton(onClick = { viewModel.clearUserMessage() }) {
                            Text(
                                text = "Eco Checkout",
                                color = ShopGreenPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Cart Items List
                items(cartItems) { item ->
                    CartItemCard(
                        item = item,
                        onIncrease = {
                            viewModel.updateCartQuantity(item.product.id, item.selectedColor, item.selectedSize, item.quantity + 1)
                        },
                        onDecrease = {
                            viewModel.updateCartQuantity(item.product.id, item.selectedColor, item.selectedSize, item.quantity - 1)
                        },
                        onRemove = {
                            viewModel.removeFromCart(item.product.id, item.selectedColor, item.selectedSize)
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                // Coupon Code Box
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocalOffer, contentDescription = null, tint = ShopGreenPrimary, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Coupons & Discounts • कूपन कोड",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ShopTextPrimary
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            if (appliedCoupon != null) {
                                // Active coupon pill
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(ShopGreenContainer, RoundedCornerShape(12.dp))
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Check, contentDescription = null, tint = ShopGreenPrimary, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "'$appliedCoupon' Applied",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = ShopGreenPrimary
                                        )
                                    }

                                    IconButton(
                                        onClick = { viewModel.removeCoupon() },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(Icons.Default.Close, contentDescription = "Remove coupon", tint = ShopTextSecondary, modifier = Modifier.size(16.dp))
                                    }
                                }
                            } else {
                                // Coupon Input Row
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedTextField(
                                        value = couponInput,
                                        onValueChange = { couponInput = it.uppercase() },
                                        placeholder = { Text("Enter GREEN20 or SHOPEASE50", fontSize = 12.sp) },
                                        singleLine = true,
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(50.dp)
                                            .testTag("coupon_input"),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = ShopGreenPrimary,
                                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                        )
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Button(
                                        onClick = {
                                            if (couponInput.isNotBlank()) {
                                                viewModel.applyCoupon(couponInput)
                                                couponInput = ""
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = ShopGreenPrimary),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.height(50.dp).testTag("apply_coupon_btn")
                                    ) {
                                        Text("Apply", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }

                // Bill Details Summary Card
                item {
                    Spacer(modifier = Modifier.height(14.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Order Summary • बिल विवरण",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = ShopTextPrimary
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Items Total", fontSize = 13.sp, color = ShopTextSecondary)
                                Text("₹${subtotal.toInt()}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = ShopTextPrimary)
                            }

                            if (discount > 0) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Coupon Discount", fontSize = 13.sp, color = ShopGreenPrimary, fontWeight = FontWeight.Medium)
                                    Text("-₹${discount.toInt()}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = ShopGreenPrimary)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Delivery Fee", fontSize = 13.sp, color = ShopTextSecondary)
                                Text(
                                    text = if (deliveryFee == 0.0) "FREE" else "₹${deliveryFee.toInt()}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (deliveryFee == 0.0) ShopGreenPrimary else ShopTextPrimary
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Total Payable Amount",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ShopTextPrimary
                                    )
                                    Text(
                                        text = "कुल देय राशि (Taxes included)",
                                        fontSize = 11.sp,
                                        color = ShopTextSecondary
                                    )
                                }
                                Text(
                                    text = "₹${totalAmount.toInt()}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = ShopGreenPrimary
                                )
                            }
                        }
                    }
                }
            }

            // Bottom Sticky Checkout Bar
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 12.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "₹${totalAmount.toInt()}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = ShopGreenPrimary
                        )
                        Text(
                            text = "View Bill Summary",
                            fontSize = 11.sp,
                            color = ShopTextSecondary
                        )
                    }

                    Button(
                        onClick = onProceedToCheckout,
                        modifier = Modifier
                            .height(50.dp)
                            .testTag("cart_proceed_to_checkout_btn"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ShopGreenPrimary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Proceed to Checkout", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemCard(
    item: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .testTag("cart_item_${item.product.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
            ) {
                ProductArtIllustration(
                    categoryKey = item.product.imageCategory,
                    iconSize = 36.dp,
                    tintColor = Color(item.selectedColor)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.product.name,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = ShopTextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Size: ${item.selectedSize}",
                    fontSize = 11.sp,
                    color = ShopTextSecondary
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "₹${item.totalPrice.toInt()}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = ShopGreenPrimary
                    )

                    // Quantity Stepper
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                            .padding(2.dp)
                    ) {
                        IconButton(
                            onClick = onDecrease,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(14.dp))
                        }
                        Text(
                            text = item.quantity.toString(),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        IconButton(
                            onClick = onIncrease,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Increase", modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }

            // Remove Button
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Remove",
                    tint = ShopAccentCoral.copy(alpha = 0.8f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
