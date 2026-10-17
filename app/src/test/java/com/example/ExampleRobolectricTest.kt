package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.CartItem
import com.example.data.model.ProductItem
import org.junit.Assert.assertEquals
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
    assertEquals("SHAHI AGRO", appName)
  }

  @Test
  fun `verify silage calculation`() {
    val silage = ProductItem(
      id = "silage-50kg",
      nameEn = "SHAHI GRASS SILAGE",
      nameBn = "শাহী ঘাসের সাইলেজ",
      category = "silage",
      variantEn = "50 kg Packet",
      variantBn = "৫০ কেজি প্যাকেট",
      price = 350.0,
      unitEn = "packet",
      unitBn = "প্যাকেট",
      minOrder = 20,
      maxOrder = 400,
      unitWeightKg = 50.0,
      stockQuantity = 2400,
      rating = 4.9,
      reviewCount = 186,
      descriptionEn = "Silage",
      descriptionBn = "সাইলেজ",
      benefitsEn = listOf("Fiber"),
      benefitsBn = listOf("ফাইবার"),
      storageInstructionsEn = "Dry place",
      storageInstructionsBn = "শুকনা স্থান"
    )

    val cartItem = CartItem(product = silage, quantity = 20)
    assertEquals(7000.0, cartItem.subtotal, 0.01)
    assertEquals(1000.0, cartItem.totalWeightKg, 0.01)
  }
}
