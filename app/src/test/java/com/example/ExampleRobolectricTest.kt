package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
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
    assertEquals("Aura", appName)
  }

  @Test
  fun `verify cart total and coupon application`() {
    val repo = com.example.data.repository.EcommerceRepository()
    val product = repo.allProducts.first()
    repo.clearCart()
    repo.addToCart(product, quantity = 2)

    val subtotal = repo.cartItems.value.sumOf { it.totalPrice }
    assertEquals(product.price * 2, subtotal, 0.01)

    // Apply coupon
    val couponResult = repo.applyCoupon("AURA20")
    assertTrue(couponResult.isSuccess)
    assertEquals(20, repo.appliedCoupon.value?.discountPercent)
  }
}
