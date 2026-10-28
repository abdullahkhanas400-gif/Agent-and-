package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.PaymentMethod
import com.example.data.repository.ShopRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("ShopEase", appName)
  }

  @Test
  fun `verify shop repository product catalog and cart calculation`() {
    val repository = ShopRepository()
    val products = repository.products.value
    assertTrue("Products catalog should not be empty", products.isNotEmpty())

    val (subtotal, discount, delivery) = repository.calculateCartSummary()
    assertTrue("Subtotal should be greater than zero with default cart items", subtotal > 0.0)

    val (success, _) = repository.applyCoupon("GREEN20")
    assertTrue("Coupon GREEN20 should be accepted", success)

    val order = repository.placeOrder(PaymentMethod.UPI)
    assertNotNull("Placed order must not be null", order)
    assertTrue("Order ID should start with SE-", order.id.startsWith("SE-"))
    assertTrue("Cart should be empty after order placement", repository.cart.value.isEmpty())
  }
}
