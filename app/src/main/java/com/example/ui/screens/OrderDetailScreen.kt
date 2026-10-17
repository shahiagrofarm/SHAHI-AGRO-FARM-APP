package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.OrderWithItems
import com.example.data.model.AppLanguage
import com.example.ui.components.StatusBadge
import com.example.ui.theme.WhatsAppGreen
import com.example.ui.util.AppStrings
import com.example.ui.viewmodel.ShahiViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OrderDetailScreen(
    orderId: Long,
    viewModel: ShahiViewModel,
    language: AppLanguage,
    orders: List<OrderWithItems>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val orderWithItems = orders.find { it.order.id == orderId } ?: return
    val o = orderWithItems.order

    var showCancelDialog by remember { mutableStateOf(false) }

    val dateFormatted = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        .format(Date(o.createdAt))

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("order_detail_screen")
    ) {
        // Top Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = o.orderNumber,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = dateFormatted,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    StatusBadge(status = o.status)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Timeline Step Progress Tracker
                OrderTimelineTracker(currentStatus = o.status, language = language)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Order Items Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (language == AppLanguage.BN) "পণ্যের তালিকা" else "Order Items",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                orderWithItems.items.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.productName,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "${item.variant} × ${item.quantity} (${item.weightKg} kg)",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "৳${String.format(Locale.US, "%,.0f", item.subtotal)}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = AppStrings.t("total_weight", language), fontSize = 13.sp)
                    Text(text = "${String.format(Locale.US, "%,.1f", o.totalWeightKg)} kg", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = AppStrings.t("subtotal", language), fontSize = 13.sp)
                    Text(text = "৳${String.format(Locale.US, "%,.0f", o.subtotal)}", fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = AppStrings.t("delivery_charge", language), fontSize = 13.sp)
                    Text(
                        text = if (o.deliveryCharge > 0) "৳${String.format(Locale.US, "%,.0f", o.deliveryCharge)}" else AppStrings.t("delivery_confirm_seller", language),
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = AppStrings.t("total_amount", language), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = "৳${String.format(Locale.US, "%,.0f", o.totalAmount)}",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Delivery & Payment Information
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (language == AppLanguage.BN) "ডেলিভারি ও পেমেন্ট তথ্য" else "Delivery & Payment",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Customer: ${o.customerName}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
                Text(
                    text = "Phone: ${o.customerPhone}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (o.customerWhatsapp.isNotBlank() && o.customerWhatsapp != o.customerPhone) {
                    Text(
                        text = "WhatsApp: ${o.customerWhatsapp}",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = "Address: ${o.deliveryAddress}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Payment: ${o.paymentMethod}",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    if (o.isPaymentVerified) {
                        Surface(
                            color = Color(0xFFE8F5E9),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "Verified",
                                color = Color(0xFF2E7D32),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    } else if (o.paymentMethod != "Cash on Delivery") {
                        Surface(
                            color = Color(0xFFFFF3CD),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "Pending Verification",
                                color = Color(0xFF856404),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                if (o.transactionId.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Sender: ${o.senderPhone} | TrxID: ${o.transactionId}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (o.notes.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Notes: ${o.notes}",
                        fontSize = 12.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // WhatsApp Help Action Button
        Button(
            onClick = { viewModel.sendOrderWhatsApp(context, orderWithItems) },
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreen),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_whatsapp),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (language == AppLanguage.BN) "হোয়াটসঅ্যাপে অর্ডার সহায়তা" else "Contact Support on WhatsApp",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.White
            )
        }

        // Cancel Order Button (if Pending or Confirmed)
        if (o.status == "PENDING" || o.status == "CONFIRMED") {
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
                onClick = { showCancelDialog = true },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD32F2F)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Icon(imageVector = Icons.Default.Cancel, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (language == AppLanguage.BN) "অর্ডার বাতিল করার অনুরোধ" else "Request Order Cancellation",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }
    }

    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            title = { Text(text = if (language == AppLanguage.BN) "অর্ডার বাতিল" else "Cancel Order") },
            text = {
                Text(
                    text = if (language == AppLanguage.BN)
                        "আপনি কি নিশ্চিত যে অর্ডারটি (${o.orderNumber}) বাতিল করতে চান?"
                    else
                        "Are you sure you want to cancel order ${o.orderNumber}?"
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateOrderStatus(o.id, "CANCELLED")
                        showCancelDialog = false
                        Toast.makeText(context, "Order marked as cancelled", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) {
                    Text(if (language == AppLanguage.BN) "হ্যাঁ, বাতিল করুন" else "Yes, Cancel", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCancelDialog = false }) {
                    Text(if (language == AppLanguage.BN) "না" else "Back")
                }
            }
        )
    }
}

@Composable
fun OrderTimelineTracker(
    currentStatus: String,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val steps = listOf("PENDING", "CONFIRMED", "PROCESSING", "SHIPPED", "DELIVERED")
    val currentIdx = steps.indexOf(currentStatus.uppercase()).coerceAtLeast(0)
    val isCancelled = currentStatus.equals("CANCELLED", ignoreCase = true)

    if (isCancelled) {
        Surface(
            color = Color(0xFFFFEBEE),
            shape = RoundedCornerShape(10.dp),
            modifier = modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Cancel,
                    contentDescription = null,
                    tint = Color(0xFFC62828),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (language == AppLanguage.BN) "এই অর্ডারটি বাতিল করা হয়েছে।" else "This order was cancelled.",
                    color = Color(0xFFC62828),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        return
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        steps.forEachIndexed { index, step ->
            val isPassed = index <= currentIdx
            val isCurrent = index == currentIdx

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(
                            if (isPassed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isPassed) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color.Gray.copy(alpha = 0.5f))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                val label = when (step) {
                    "PENDING" -> if (language == AppLanguage.BN) "পেন্ডিং" else "Pending"
                    "CONFIRMED" -> if (language == AppLanguage.BN) "নিশ্চিত" else "Confirmed"
                    "PROCESSING" -> if (language == AppLanguage.BN) "প্রসেস" else "Process"
                    "SHIPPED" -> if (language == AppLanguage.BN) "শিপড" else "Shipped"
                    "DELIVERED" -> if (language == AppLanguage.BN) "ডেলিভারড" else "Delivered"
                    else -> step
                }

                Text(
                    text = label,
                    fontSize = 10.sp,
                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                    color = if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
