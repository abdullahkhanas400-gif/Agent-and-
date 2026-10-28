package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.ui.components.ShopBottomNavBar
import com.example.ui.navigation.BottomTab
import com.example.ui.navigation.Screen
import com.example.ui.screens.CartScreen
import com.example.ui.screens.CategoriesScreen
import com.example.ui.screens.CheckoutScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.OrderSuccessScreen
import com.example.ui.screens.OrderTrackingScreen
import com.example.ui.screens.ProductDetailScreen
import com.example.ui.screens.ProductListScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.ShopViewModel

class MainActivity : ComponentActivity() {
  private val viewModel: ShopViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        ShopEaseApp(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun ShopEaseApp(viewModel: ShopViewModel) {
  val currentScreen by viewModel.currentScreen.collectAsState()
  val activeTab by viewModel.activeTab.collectAsState()
  val cartItemCount by viewModel.cartItemCount.collectAsState()
  val userMessage by viewModel.userMessage.collectAsState()

  val snackbarHostState = remember { SnackbarHostState() }

  LaunchedEffect(userMessage) {
    userMessage?.let { msg ->
      snackbarHostState.showSnackbar(msg)
      viewModel.clearUserMessage()
    }
  }

  val showBottomBar = when (currentScreen) {
    is Screen.Home,
    is Screen.Categories,
    is Screen.Cart,
    is Screen.Profile -> true
    else -> false
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    contentWindowInsets = WindowInsets(0, 0, 0, 0),
    bottomBar = {
      if (showBottomBar) {
        ShopBottomNavBar(
          activeTab = activeTab,
          cartItemCount = cartItemCount,
          onTabSelected = { tab -> viewModel.selectBottomTab(tab) }
        )
      }
    },
    snackbarHost = { SnackbarHost(snackbarHostState) }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      AnimatedContent(
        targetState = currentScreen,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "screen_transition"
      ) { targetScreen ->
        when (targetScreen) {
          is Screen.Splash -> {
            SplashScreen(
              onSplashFinished = {
                viewModel.navigateTo(Screen.Onboarding, addToBackstack = false)
              }
            )
          }

          is Screen.Onboarding -> {
            OnboardingScreen(
              onFinish = {
                viewModel.navigateTo(Screen.Login, addToBackstack = false)
              }
            )
          }

          is Screen.Login -> {
            LoginScreen(
              onLoginSuccess = { name, email ->
                viewModel.loginAs(name, email)
              },
              onContinueAsGuest = {
                viewModel.continueAsGuest()
              }
            )
          }

          is Screen.Home -> {
            HomeScreen(
              viewModel = viewModel,
              onNavigateToCategory = { categoryId ->
                viewModel.navigateTo(Screen.ProductList(categoryId = categoryId))
              },
              onNavigateToProduct = { productId ->
                viewModel.navigateTo(Screen.ProductDetail(productId = productId))
              },
              onNavigateToSearch = { query ->
                viewModel.navigateTo(Screen.ProductList(initialQuery = query))
              },
              onNavigateToCart = {
                viewModel.navigateTo(Screen.Cart)
              }
            )
          }

          is Screen.Categories -> {
            CategoriesScreen(
              viewModel = viewModel,
              onCategorySelected = { categoryId, categoryName ->
                viewModel.navigateTo(
                  Screen.ProductList(categoryId = categoryId, categoryName = categoryName)
                )
              }
            )
          }

          is Screen.ProductList -> {
            ProductListScreen(
              categoryId = targetScreen.categoryId,
              categoryName = targetScreen.categoryName,
              initialQuery = targetScreen.initialQuery,
              viewModel = viewModel,
              onProductClick = { productId ->
                viewModel.navigateTo(Screen.ProductDetail(productId = productId))
              },
              onBack = { viewModel.navigateBack() }
            )
          }

          is Screen.ProductDetail -> {
            ProductDetailScreen(
              productId = targetScreen.productId,
              viewModel = viewModel,
              onBack = { viewModel.navigateBack() },
              onProceedToCheckout = {
                viewModel.navigateTo(Screen.Checkout)
              }
            )
          }

          is Screen.Cart -> {
            CartScreen(
              viewModel = viewModel,
              onProceedToCheckout = {
                viewModel.navigateTo(Screen.Checkout)
              },
              onContinueShopping = {
                viewModel.navigateTo(Screen.Home, addToBackstack = false)
              }
            )
          }

          is Screen.Checkout -> {
            CheckoutScreen(
              viewModel = viewModel,
              onBack = { viewModel.navigateBack() },
              onOrderSuccess = { orderId ->
                viewModel.navigateTo(Screen.OrderSuccess(orderId = orderId), addToBackstack = false)
              }
            )
          }

          is Screen.OrderSuccess -> {
            OrderSuccessScreen(
              orderId = targetScreen.orderId,
              viewModel = viewModel,
              onTrackOrder = { orderId ->
                viewModel.navigateTo(Screen.OrderTracking(orderId = orderId))
              },
              onContinueShopping = {
                viewModel.navigateTo(Screen.Home, addToBackstack = false)
              }
            )
          }

          is Screen.OrderTracking -> {
            OrderTrackingScreen(
              orderId = targetScreen.orderId,
              viewModel = viewModel,
              onBack = { viewModel.navigateBack() }
            )
          }

          is Screen.Profile -> {
            ProfileScreen(
              viewModel = viewModel,
              onTrackOrder = { orderId ->
                viewModel.navigateTo(Screen.OrderTracking(orderId = orderId))
              },
              onLogout = {
                viewModel.navigateTo(Screen.Login, addToBackstack = false)
              }
            )
          }
        }
      }
    }
  }
}
