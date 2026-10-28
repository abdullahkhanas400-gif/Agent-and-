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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TrackingStep
import com.example.ui.theme.ShopGreenContainer
import com.example.ui.theme.ShopGreenPrimary
import com.example.ui.theme.ShopTextMuted
import com.example.ui.theme.ShopTextPrimary
import com.example.ui.theme.ShopTextSecondary
import com.example.ui.viewmodel.ShopViewModel

@Composable
fun OrderTrackingScreen(
    orderId: String,
    viewModel: ShopViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val order = viewModel.getOrderById(orderId)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .testTag("order_tracking_screen"),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Top Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("tracking_back_btn")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = ShopTextPrimary
                    )
                }
                Text(
                    text = "Track Order • ऑर्डर ट्रैकिंग",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = ShopTextPrimary
                )
            }
        }

        // Status Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = ShopGreenPrimary)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "ORDER STATUS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.8f),
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = order?.status ?: "Confirmed",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocalShipping, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Estimated: ${order?.estimatedDeliveryDate ?: "In 2 days"}",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.95f),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // Timeline Tracking Steps
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Delivery Timeline • प्रोग्रेस",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = ShopTextPrimary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    val steps = order?.trackingSteps ?: listOf(
                        TrackingStep("Order Confirmed", "Your order has been verified", "Just now", true),
                        TrackingStep("Packed & Dispatched", "Eco-friendly packaging", "Tomorrow", false),
                        TrackingStep("Out for Delivery", "Express doorstep transit", "In 2 days", false),
                        TrackingStep("Delivered", "Delivered with happiness", "Estimated in 2 days", false)
                    )

                    steps.forEachIndexed { index, step ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            // Timeline Icon & Vertical Line
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(if (step.isCompleted) ShopGreenPrimary else ShopTextMuted.copy(alpha = 0.3f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                if (index < steps.size - 1) {
                                    Box(
                                        modifier = Modifier
                                            .width(2.dp)
                                            .height(38.dp)
                                            .background(if (step.isCompleted) ShopGreenPrimary else ShopTextMuted.copy(alpha = 0.3f))
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            // Step Details
                            Column(modifier = Modifier.padding(bottom = if (index < steps.size - 1) 18.dp else 0.dp)) {
                                Text(
                                    text = step.title,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (step.isCompleted) ShopTextPrimary else ShopTextMuted
                                )
                                Text(
                                    text = step.description,
                                    fontSize = 11.sp,
                                    color = ShopTextSecondary
                                )
                                Text(
                                    text = step.timestamp,
                                    fontSize = 10.sp,
                                    color = ShopTextMuted
                                )
                            }
                        }
                    }
                }
            }
        }

        // Delivery Address Info
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = ShopGreenPrimary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Delivery Location",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = ShopTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = order?.address?.fullName ?: "Customer",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = order?.address?.fullFormatted ?: "",
                        fontSize = 11.sp,
                        color = ShopTextSecondary
                    )
                }
            }
        }

        // Support Buttons
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = { /* Call Courier */ },
                    modifier = Modifier.weight(1f).height(46.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp), tint = ShopGreenPrimary)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Call Delivery", color = ShopGreenPrimary, fontSize = 12.sp)
                }

                Button(
                    onClick = { /* Help */ },
                    modifier = Modifier.weight(1f).height(46.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ShopGreenPrimary)
                ) {
                    Icon(Icons.Default.HelpOutline, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Need Help?", fontSize = 12.sp)
                }
            }
        }
    }
}
