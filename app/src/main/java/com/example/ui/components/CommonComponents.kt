package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AppLanguage
import com.example.data.model.OrderStatus
import com.example.data.model.ProductItem
import com.example.ui.theme.ShahiForestGreen
import com.example.ui.theme.ShahiGold
import com.example.ui.theme.StatusCancelledBg
import com.example.ui.theme.StatusCancelledText
import com.example.ui.theme.StatusDeliveredBg
import com.example.ui.theme.StatusDeliveredText
import com.example.ui.theme.StatusPendingBg
import com.example.ui.theme.StatusPendingText
import com.example.ui.theme.StatusProcessingBg
import com.example.ui.theme.StatusProcessingText
import com.example.ui.theme.StatusShippedBg
import com.example.ui.theme.StatusShippedText
import com.example.ui.util.AppStrings
import java.util.Locale

@Composable
fun StatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, textEn, textBn) = when (status.uppercase()) {
        "PENDING" -> Quadruple(StatusPendingBg, StatusPendingText, "Pending", "অপেক্ষমান")
        "CONFIRMED" -> Quadruple(StatusProcessingBg, StatusProcessingText, "Confirmed", "নিশ্চিত")
        "PROCESSING" -> Quadruple(StatusProcessingBg, StatusProcessingText, "Processing", "প্রক্রিয়াধীন")
        "SHIPPED" -> Quadruple(StatusShippedBg, StatusShippedText, "Shipped", "পাঠানো হয়েছে")
        "DELIVERED" -> Quadruple(StatusDeliveredBg, StatusDeliveredText, "Delivered", "ডেলিভারড")
        "CANCELLED" -> Quadruple(StatusCancelledBg, StatusCancelledText, "Cancelled", "বাতিল")
        else -> Quadruple(StatusPendingBg, StatusPendingText, status, status)
    }

    Surface(
        modifier = modifier,
        color = bgColor,
        shape = RoundedCornerShape(50)
    ) {
        Text(
            text = textEn,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

@Composable
fun QuantityPicker(
    quantity: Int,
    min: Int,
    max: Int,
    step: Int = 1,
    unit: String = "packet",
    onQuantityChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 6.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        IconButton(
            onClick = {
                val next = (quantity - step).coerceAtLeast(min)
                onQuantityChanged(next)
            },
            enabled = quantity > min,
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (quantity > min) MaterialTheme.colorScheme.primaryContainer else Color.LightGray.copy(alpha = 0.3f))
                .testTag("qty_decrease_btn")
        ) {
            Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = "Decrease",
                tint = if (quantity > min) MaterialTheme.colorScheme.primary else Color.Gray,
                modifier = Modifier.size(18.dp)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(60.dp)
        ) {
            Text(
                text = "$quantity",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = unit,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        IconButton(
            onClick = {
                val next = (quantity + step).coerceAtMost(max)
                onQuantityChanged(next)
            },
            enabled = quantity < max,
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (quantity < max) MaterialTheme.colorScheme.primaryContainer else Color.LightGray.copy(alpha = 0.3f))
                .testTag("qty_increase_btn")
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Increase",
                tint = if (quantity < max) MaterialTheme.colorScheme.primary else Color.Gray,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun ProductCard(
    product: ProductItem,
    language: AppLanguage,
    onProductClick: () -> Unit,
    onAddToCart: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onProductClick)
            .testTag("product_card_${product.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Product Icon / Illustration
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            if (product.category == "silage") Color(0xFFE8F5E9) else Color(0xFFFFF8E1)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    val iconRes = if (product.category == "silage") R.drawable.ic_silage_packet else R.drawable.ic_ghee_jar
                    Image(
                        painter = painterResource(id = iconRes),
                        contentDescription = product.nameEn,
                        modifier = Modifier.size(54.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Surface(
                        color = if (product.category == "silage") Color(0xFFD4EED8) else Color(0xFFFFECC8),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = if (product.category == "silage") AppStrings.t("main_product_badge", language) else AppStrings.t("secondary_product_badge", language),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (product.category == "silage") ShahiForestGreen else Color(0xFF8A5300),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (language == AppLanguage.BN) product.nameBn else product.nameEn,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = if (language == AppLanguage.BN) product.variantBn else product.variantEn,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "৳${String.format(Locale.US, "%,.0f", product.price)}",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = " / ${if (language == AppLanguage.BN) product.unitBn else product.unitEn}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Rating & Min order
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = ShahiGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${product.rating} (${product.reviewCount}+)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                if (product.minOrder > 1) {
                    Text(
                        text = "Min: ${product.minOrder} ${product.unitEn}s",
                        fontSize = 11.sp,
                        color = Color(0xFFC2410C),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onProductClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (language == AppLanguage.BN) "বিস্তারিত" else "Details",
                        fontSize = 13.sp
                    )
                }

                Button(
                    onClick = { onAddToCart(product.minOrder) },
                    modifier = Modifier
                        .weight(1.2f)
                        .testTag("add_cart_btn_${product.id}"),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = AppStrings.t("add_to_cart", language),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
