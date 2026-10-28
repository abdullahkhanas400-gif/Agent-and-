package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DeliveryAddress
import com.example.data.model.PaymentMethod
import com.example.ui.theme.ShopGreenContainer
import com.example.ui.theme.ShopGreenPrimary
import com.example.ui.theme.ShopTextMuted
import com.example.ui.theme.ShopTextPrimary
import com.example.ui.theme.ShopTextSecondary
import com.example.ui.viewmodel.ShopViewModel

@Composable
fun CheckoutScreen(
    viewModel: ShopViewModel,
    onBack: () -> Unit,
    onOrderSuccess: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val userAddress by viewModel.userAddress.collectAsState()
    val cartItems by viewModel.cart.collectAsState()
    val (subtotal, discount, deliveryFee) = viewModel.cartSummary.collectAsState().value
    val totalAmount = (subtotal - discount + deliveryFee).coerceAtLeast(0.0)

    var selectedPayment by remember { mutableStateOf(PaymentMethod.UPI) }
    var isExpressDelivery by remember { mutableStateOf(false) }
    var showAddressDialog by remember { mutableStateOf(false) }

    // Dialog state
    var editName by remember { mutableStateOf(userAddress.fullName) }
    var editPhone by remember { mutableStateOf(userAddress.phone) }
    var editStreet by remember { mutableStateOf(userAddress.street) }
    var editCity by remember { mutableStateOf(userAddress.city) }
    var editPincode by remember { mutableStateOf(userAddress.pincode) }

    val finalDeliveryFee = if (isExpressDelivery) (deliveryFee + 49.0) else deliveryFee
    val finalTotal = (subtotal - discount + finalDeliveryFee).coerceAtLeast(0.0)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .testTag("checkout_screen")
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            // Top Bar
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("checkout_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = ShopTextPrimary
                        )
                    }
                    Text(
                        text = "Checkout • सुरक्षित चेकआउट",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = ShopTextPrimary
                    )
                }
            }

            // Step Progress Banner
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = ShopGreenContainer)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CheckoutStepIndicator(number = "1", title = "Address", isActive = true)
                        HorizontalDivider(modifier = Modifier.weight(1f).padding(horizontal = 6.dp), color = ShopGreenPrimary)
                        CheckoutStepIndicator(number = "2", title = "Payment", isActive = true)
                        HorizontalDivider(modifier = Modifier.weight(1f).padding(horizontal = 6.dp), color = ShopGreenPrimary)
                        CheckoutStepIndicator(number = "3", title = "Confirmation", isActive = false)
                    }
                }
            }

            // 1. Delivery Address Section
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = ShopGreenPrimary, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Delivery Address • डिलीवरी पता",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ShopTextPrimary
                                )
                            }

                            TextButton(
                                onClick = {
                                    editName = userAddress.fullName
                                    editPhone = userAddress.phone
                                    editStreet = userAddress.street
                                    editCity = userAddress.city
                                    editPincode = userAddress.pincode
                                    showAddressDialog = true
                                },
                                modifier = Modifier.testTag("checkout_change_address_btn")
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp), tint = ShopGreenPrimary)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Change", color = ShopGreenPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "${userAddress.fullName} (${userAddress.type})",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = ShopTextPrimary
                        )
                        Text(
                            text = userAddress.phone,
                            fontSize = 12.sp,
                            color = ShopTextSecondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = userAddress.fullFormatted,
                            fontSize = 12.sp,
                            color = ShopTextSecondary,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // 2. Delivery Speed Options
            item {
                Spacer(modifier = Modifier.height(12.dp))
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
                            Icon(Icons.Default.LocalShipping, contentDescription = null, tint = ShopGreenPrimary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Delivery Method • डिलीवरी का तरीका",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = ShopTextPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Standard Eco
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (!isExpressDelivery) ShopGreenContainer else Color.Transparent)
                                .clickable { isExpressDelivery = false }
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = !isExpressDelivery,
                                    onClick = { isExpressDelivery = false },
                                    colors = RadioButtonDefaults.colors(selectedColor = ShopGreenPrimary)
                                )
                                Column {
                                    Text("Standard Eco Delivery", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                    Text("Delivered in 2-3 business days", fontSize = 11.sp, color = ShopTextSecondary)
                                }
                            }
                            Text("FREE", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ShopGreenPrimary)
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Express Next Day
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isExpressDelivery) ShopGreenContainer else Color.Transparent)
                                .clickable { isExpressDelivery = true }
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = isExpressDelivery,
                                    onClick = { isExpressDelivery = true },
                                    colors = RadioButtonDefaults.colors(selectedColor = ShopGreenPrimary)
                                )
                                Column {
                                    Text("Priority Express Delivery", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                    Text("Delivered tomorrow before 2 PM", fontSize = 11.sp, color = ShopTextSecondary)
                                }
                            }
                            Text("₹49", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ShopTextPrimary)
                        }
                    }
                }
            }

            // 3. Payment Method Section
            item {
                Spacer(modifier = Modifier.height(12.dp))
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
                            Icon(Icons.Default.CreditCard, contentDescription = null, tint = ShopGreenPrimary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Payment Options • भुगतान का माध्यम",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = ShopTextPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        PaymentMethod.values().forEach { method ->
                            val isSelected = selectedPayment == method
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) ShopGreenContainer else Color.Transparent)
                                    .clickable { selectedPayment = method }
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { selectedPayment = method },
                                    colors = RadioButtonDefaults.colors(selectedColor = ShopGreenPrimary)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                val icon = when (method) {
                                    PaymentMethod.UPI -> Icons.Default.PhoneAndroid
                                    PaymentMethod.CARD -> Icons.Default.CreditCard
                                    PaymentMethod.NET_BANKING -> Icons.Default.AccountBalance
                                    PaymentMethod.COD -> Icons.Default.LocalAtm
                                }
                                Icon(icon, contentDescription = null, tint = ShopGreenPrimary, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(method.title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = ShopTextPrimary)
                                    Text(method.subtitle, fontSize = 11.sp, color = ShopTextSecondary)
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            }

            // 4. Price Recap Card
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Items Subtotal (${cartItems.size})", fontSize = 13.sp, color = ShopTextSecondary)
                            Text("₹${subtotal.toInt()}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                        if (discount > 0) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Discount Applied", fontSize = 13.sp, color = ShopGreenPrimary)
                                Text("-₹${discount.toInt()}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = ShopGreenPrimary)
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Delivery Fee", fontSize = 13.sp, color = ShopTextSecondary)
                            Text(
                                text = if (finalDeliveryFee == 0.0) "FREE" else "₹${finalDeliveryFee.toInt()}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (finalDeliveryFee == 0.0) ShopGreenPrimary else ShopTextPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Total Amount to Pay", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("₹${finalTotal.toInt()}", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = ShopGreenPrimary)
                        }
                    }
                }
            }
        }

        // Sticky Place Order Bar
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
                        text = "₹${finalTotal.toInt()}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = ShopGreenPrimary
                    )
                    Text(
                        text = selectedPayment.title.split("/").first().trim(),
                        fontSize = 11.sp,
                        color = ShopTextSecondary
                    )
                }

                Button(
                    onClick = {
                        val order = viewModel.placeOrder(selectedPayment)
                        onOrderSuccess(order.id)
                    },
                    modifier = Modifier
                        .height(50.dp)
                        .testTag("checkout_place_order_btn"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ShopGreenPrimary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Security, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Place Order • ऑर्डर करें", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }

        // Address Edit Modal Dialog
        if (showAddressDialog) {
            AlertDialog(
                onDismissRequest = { showAddressDialog = false },
                title = { Text("Update Delivery Address", fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = editName,
                            onValueChange = { editName = it },
                            label = { Text("Full Name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = editPhone,
                            onValueChange = { editPhone = it },
                            label = { Text("Phone Number") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = editStreet,
                            onValueChange = { editStreet = it },
                            label = { Text("House / Flat / Street") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = editCity,
                                onValueChange = { editCity = it },
                                label = { Text("City") },
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = editPincode,
                                onValueChange = { editPincode = it },
                                label = { Text("Pincode") },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.updateAddress(
                                DeliveryAddress(
                                    fullName = editName,
                                    phone = editPhone,
                                    street = editStreet,
                                    city = editCity,
                                    pincode = editPincode,
                                    state = userAddress.state
                                )
                            )
                            showAddressDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ShopGreenPrimary)
                    ) {
                        Text("Save Address")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddressDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun CheckoutStepIndicator(number: String, title: String, isActive: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(if (isActive) ShopGreenPrimary else ShopTextMuted.copy(alpha = 0.4f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
            color = if (isActive) ShopGreenPrimary else ShopTextSecondary
        )
    }
}
