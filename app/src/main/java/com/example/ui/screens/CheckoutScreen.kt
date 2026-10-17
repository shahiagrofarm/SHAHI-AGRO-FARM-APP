package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.CartItem
import com.example.data.model.PaymentType
import com.example.ui.theme.BKashPink
import com.example.ui.theme.NagadOrange
import com.example.ui.theme.RocketViolet
import com.example.ui.theme.ShahiForestGreen
import com.example.ui.theme.UpayNavy
import com.example.ui.util.AppStrings
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.ShahiViewModel
import java.util.Locale

@Composable
fun CheckoutScreen(
    viewModel: ShahiViewModel,
    language: AppLanguage,
    cartItems: List<CartItem>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    var customerName by remember { mutableStateOf("") }
    var customerPhone by remember { mutableStateOf("") }
    var customerWhatsapp by remember { mutableStateOf("") }
    var deliveryAddress by remember { mutableStateOf("") }
    var selectedPayment by remember { mutableStateOf(PaymentType.BKASH) }
    var senderPhone by remember { mutableStateOf("") }
    var transactionId by remember { mutableStateOf("") }
    var orderNotes by remember { mutableStateOf("") }

    var isSubmitting by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val subtotal = cartItems.sumOf { it.subtotal }
    val totalWeightKg = cartItems.sumOf { it.totalWeightKg }
    val deliveryEstimate = if (cartItems.any { it.product.category == "silage" }) 0.0 else 130.0
    val totalAmount = subtotal + deliveryEstimate

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("checkout_screen")
    ) {
        Text(
            text = if (language == AppLanguage.BN) "অর্ডার সম্পন্ন করুন" else "Complete Your Order",
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = if (language == AppLanguage.BN) "সঠিক তথ্য দিন যাতে আপনার খামারে সময়মতো পৌঁছানো যায়" else "Please provide accurate delivery and contact information",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Order Summary Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (language == AppLanguage.BN) "অর্ডারের সারসংক্ষেপ" else "Order Summary",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                cartItems.forEach { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${if (language == AppLanguage.BN) item.product.nameBn else item.product.nameEn} × ${item.quantity}",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "৳${String.format(Locale.US, "%,.0f", item.subtotal)}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = AppStrings.t("total_weight", language), fontSize = 13.sp)
                    Text(text = "${String.format(Locale.US, "%,.1f", totalWeightKg)} kg", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = AppStrings.t("delivery_charge", language), fontSize = 13.sp)
                    Text(
                        text = if (deliveryEstimate > 0) "৳${String.format(Locale.US, "%,.0f", deliveryEstimate)}" else AppStrings.t("delivery_confirm_seller", language),
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 13.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = AppStrings.t("total_amount", language), fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = "৳${String.format(Locale.US, "%,.0f", totalAmount)}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Customer Details Form
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = AppStrings.t("customer_info", language),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = customerName,
                    onValueChange = { customerName = it },
                    label = { Text(AppStrings.t("full_name", language) + " *") },
                    placeholder = { Text(AppStrings.t("full_name_hint", language)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("checkout_name_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = customerPhone,
                    onValueChange = { customerPhone = it },
                    label = { Text(AppStrings.t("phone_number", language) + " *") },
                    placeholder = { Text(AppStrings.t("phone_hint", language)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("checkout_phone_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = customerWhatsapp,
                    onValueChange = { customerWhatsapp = it },
                    label = { Text(AppStrings.t("whatsapp_number", language)) },
                    placeholder = { Text("01XXXXXXXXX") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("checkout_whatsapp_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = deliveryAddress,
                    onValueChange = { deliveryAddress = it },
                    label = { Text(AppStrings.t("delivery_address", language) + " *") },
                    placeholder = { Text(AppStrings.t("address_hint", language)) },
                    minLines = 2,
                    maxLines = 4,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("checkout_address_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = orderNotes,
                    onValueChange = { orderNotes = it },
                    label = { Text(if (language == AppLanguage.BN) "বিশেষ নির্দেশনা (ঐচ্ছিক)" else "Delivery Notes / Instructions") },
                    placeholder = { Text(if (language == AppLanguage.BN) "গাভীর সংখ্যা বা ট্রানজিট দিকনির্দেশনা" else "e.g. Landmark, cow shed gate, delivery timing") },
                    minLines = 1,
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Payment Method Selector
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = AppStrings.t("payment_method", language),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.height(12.dp))

                val paymentOptions = PaymentType.values()
                paymentOptions.forEach { type ->
                    val isSelected = selectedPayment == type
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { selectedPayment = type }
                            .testTag("payment_method_${type.name.lowercase()}"),
                        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f) else Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { selectedPayment = type },
                                colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = type.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                if (type.accountNumber.isNotBlank()) {
                                    Text(
                                        text = "${type.typeName}: ${type.accountNumber}",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                } else {
                                    Text(
                                        text = type.typeName,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            if (type.accountNumber.isNotBlank()) {
                                IconButton(
                                    onClick = {
                                        clipboardManager.setText(AnnotatedString(type.accountNumber))
                                        Toast.makeText(context, "Copied ${type.title} number: ${type.accountNumber}", Toast.LENGTH_SHORT).show()
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Copy number",
                                        modifier = Modifier.size(18.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }

                // If Prepaid MFS: instructions + TrxID + Sender Number
                AnimatedVisibility(visible = selectedPayment != PaymentType.COD) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                            .padding(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (language == AppLanguage.BN) selectedPayment.instructionsBn else selectedPayment.instructionsEn,
                                fontSize = 12.sp,
                                lineHeight = 17.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = senderPhone,
                            onValueChange = { senderPhone = it },
                            label = { Text(AppStrings.t("sender_phone", language) + " *") },
                            placeholder = { Text("01XXXXXXXXX") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("checkout_sender_phone_input"),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = transactionId,
                            onValueChange = { transactionId = it },
                            label = { Text(AppStrings.t("transaction_id", language) + " *") },
                            placeholder = { Text(AppStrings.t("transaction_hint", language)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("checkout_trx_id_input"),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )
                    }
                }
            }
        }

        // Verification Trust Notice
        Spacer(modifier = Modifier.height(14.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFFFFF3CD),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = Color(0xFF856404),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = AppStrings.t("payment_verification_notice", language),
                    fontSize = 11.sp,
                    color = Color(0xFF654A00),
                    lineHeight = 16.sp
                )
            }
        }

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Place Order Button
        Button(
            onClick = {
                // Validation
                if (customerName.isBlank()) {
                    errorMessage = if (language == AppLanguage.BN) "দয়া করে আপনার পুরো নাম লিখুন" else "Please enter your full name"
                    return@Button
                }
                if (customerPhone.isBlank() || customerPhone.length < 10) {
                    errorMessage = if (language == AppLanguage.BN) "সঠিক মোবাইল নম্বর প্রদান করুন" else "Please enter a valid phone number"
                    return@Button
                }
                if (deliveryAddress.isBlank()) {
                    errorMessage = if (language == AppLanguage.BN) "ডেলিভারি ঠিকানা প্রদান করুন" else "Please enter your delivery address"
                    return@Button
                }
                if (selectedPayment != PaymentType.COD) {
                    if (senderPhone.isBlank()) {
                        errorMessage = if (language == AppLanguage.BN) "যে নম্বর থেকে টাকা পাঠিয়েছেন তা লিখুন" else "Please enter sender mobile number"
                        return@Button
                    }
                    if (transactionId.isBlank()) {
                        errorMessage = if (language == AppLanguage.BN) "পেমেন্টের ট্রানজেকশন আইডি (TrxID) লিখুন" else "Please enter payment transaction ID"
                        return@Button
                    }
                }

                errorMessage = null
                isSubmitting = true

                viewModel.placeOrder(
                    customerName = customerName.trim(),
                    customerPhone = customerPhone.trim(),
                    customerWhatsapp = customerWhatsapp.trim(),
                    deliveryAddress = deliveryAddress.trim(),
                    paymentMethod = selectedPayment.title,
                    senderPhone = senderPhone.trim(),
                    transactionId = transactionId.trim(),
                    notes = orderNotes.trim(),
                    onSuccess = { createdOrder ->
                        isSubmitting = false
                        viewModel.navigateTo(Screen.OrderSuccess(createdOrder))
                    },
                    onError = { err ->
                        isSubmitting = false
                        errorMessage = err
                    }
                )
            },
            enabled = !isSubmitting,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("submit_order_btn")
        ) {
            if (isSubmitting) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
            } else {
                Text(
                    text = "${AppStrings.t("place_order", language)} (৳${String.format(Locale.US, "%,.0f", totalAmount)})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.White
                )
            }
        }
    }
}
