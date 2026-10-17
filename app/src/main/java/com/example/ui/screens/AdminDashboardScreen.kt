package com.example.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ExpenseEntity
import com.example.data.local.OrderWithItems
import com.example.data.model.AppLanguage
import com.example.data.model.ProductItem
import com.example.data.repository.FinancialMetrics
import com.example.ui.components.StatusBadge
import com.example.ui.theme.ShahiForestGreen
import com.example.ui.util.AppStrings
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.ShahiViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AdminDashboardScreen(
    viewModel: ShahiViewModel,
    language: AppLanguage,
    orders: List<OrderWithItems>,
    products: List<ProductItem>,
    expenses: List<ExpenseEntity>,
    metrics: FinancialMetrics,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showAddExpenseDialog by remember { mutableStateOf(false) }
    var selectedOrderForStatus by remember { mutableStateOf<OrderWithItems?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("admin_dashboard_screen")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = AppStrings.t("admin_dashboard", language),
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = if (language == AppLanguage.BN) "বাস্তব ডাটাবেজ হিসাব ও খামার ব্যবস্থাপনা" else "Real Database Accounting & Farm Operations",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Export CSV Button
            IconButton(
                onClick = {
                    val csv = generateCsvReport(orders, expenses, metrics)
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_SUBJECT, "SHAHI AGRO FARM Orders Report.csv")
                        putExtra(Intent.EXTRA_TEXT, csv)
                    }
                    context.startActivity(Intent.createChooser(intent, "Export / Share Report"))
                },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Export Report",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Financial Metrics Grid (Revenue, COGS, Gross Profit, Net Profit)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MetricCard(
                title = AppStrings.t("total_sales", language),
                amount = "৳${String.format(Locale.US, "%,.0f", metrics.totalRevenue)}",
                subtitle = "${metrics.totalOrdersCount} orders total",
                icon = Icons.Default.AttachMoney,
                cardColor = Color(0xFFE8F5E9),
                iconColor = ShahiForestGreen,
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                title = AppStrings.t("gross_profit", language),
                amount = "৳${String.format(Locale.US, "%,.0f", metrics.grossProfit)}",
                subtitle = "COGS: ৳${String.format(Locale.US, "%,.0f", metrics.cogs)}",
                icon = Icons.Default.TrendingUp,
                cardColor = Color(0xFFE1F5FE),
                iconColor = Color(0xFF0277BD),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MetricCard(
                title = AppStrings.t("expenses", language),
                amount = "৳${String.format(Locale.US, "%,.0f", metrics.totalExpenses)}",
                subtitle = "${expenses.size} recorded costs",
                icon = Icons.Default.Receipt,
                cardColor = Color(0xFFFFEBEE),
                iconColor = Color(0xFFC62828),
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                title = AppStrings.t("net_profit", language),
                amount = "৳${String.format(Locale.US, "%,.0f", metrics.netProfit)}",
                subtitle = "Gross profit minus expenses",
                icon = Icons.Default.TrendingUp,
                cardColor = Color(0xFFFFF8E1),
                iconColor = Color(0xFFE65100),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Inventory & Stock Levels
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = AppStrings.t("stock_inventory", language),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Icon(
                        imageVector = Icons.Default.Inventory,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                products.forEach { prod ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = prod.nameEn,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "${prod.variantEn} · ৳${String.format(Locale.US, "%,.0f", prod.price)}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Surface(
                            color = if (prod.stockQuantity > 50) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "${prod.stockQuantity} ${prod.unitEn}s in stock",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (prod.stockQuantity > 50) Color(0xFF2E7D32) else Color(0xFFC62828),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Farm Expenses & Add Expense Button
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (language == AppLanguage.BN) "খামারের সাম্প্রতিক ব্যয়" else "Recorded Farm Expenses",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Button(
                        onClick = { showAddExpenseDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = if (language == AppLanguage.BN) "যোগ" else "Add", fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                expenses.take(4).forEach { exp ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = exp.title, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                            Text(text = exp.category, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(
                            text = "৳${String.format(Locale.US, "%,.0f", exp.amount)}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFC62828)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Order Status & Payment Verification Manager
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (language == AppLanguage.BN) "অর্ডার ও পেমেন্ট যাচাই ব্যবস্থাপনা" else "Manage Orders & Verify Payments",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Text(
                    text = if (language == AppLanguage.BN) "স্ট্যাটাস পরিবর্তন বা পেমেন্ট ভেরিফাই করতে অর্ডারে ক্লিক করুন" else "Tap an order to update status or mark payment verified",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                orders.forEach { orderWithItems ->
                    val o = orderWithItems.order
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { selectedOrderForStatus = orderWithItems },
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${o.orderNumber} · ${o.customerName}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                StatusBadge(status = o.status)
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${o.paymentMethod}: ৳${String.format(Locale.US, "%,.0f", o.totalAmount)}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = if (o.isPaymentVerified) "✓ Verified" else "⚠ Unverified",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (o.isPaymentVerified) Color(0xFF2E7D32) else Color(0xFFE65100)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Add Expense Dialog
    if (showAddExpenseDialog) {
        var expenseTitle by remember { mutableStateOf("") }
        var expenseAmount by remember { mutableStateOf("") }
        var expenseCategory by remember { mutableStateOf("Feed Production") }

        AlertDialog(
            onDismissRequest = { showAddExpenseDialog = false },
            title = { Text(if (language == AppLanguage.BN) "নতুন খরচ যুক্ত করুন" else "Record Farm Expense") },
            text = {
                Column {
                    OutlinedTextField(
                        value = expenseTitle,
                        onValueChange = { expenseTitle = it },
                        label = { Text("Expense Title") },
                        placeholder = { Text("e.g. Silage Bags, Tractor Fuel") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = expenseAmount,
                        onValueChange = { expenseAmount = it },
                        label = { Text("Amount (৳)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amount = expenseAmount.toDoubleOrNull()
                        if (expenseTitle.isNotBlank() && amount != null && amount > 0) {
                            viewModel.addExpense(expenseTitle, amount, expenseCategory)
                            showAddExpenseDialog = false
                            Toast.makeText(context, "Expense recorded", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddExpenseDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Status Update & Payment Verification Dialog
    selectedOrderForStatus?.let { item ->
        val o = item.order
        var currentStatus by remember { mutableStateOf(o.status) }
        var isVerified by remember { mutableStateOf(o.isPaymentVerified) }

        AlertDialog(
            onDismissRequest = { selectedOrderForStatus = null },
            title = { Text("Manage ${o.orderNumber}") },
            text = {
                Column {
                    Text("Customer: ${o.customerName}")
                    Text("Phone: ${o.customerPhone}")
                    if (o.transactionId.isNotBlank()) {
                        Text("TrxID: ${o.transactionId} (${o.senderPhone})")
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Update Status:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    val allStatuses = listOf("PENDING", "CONFIRMED", "PROCESSING", "SHIPPED", "DELIVERED", "CANCELLED")
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        allStatuses.take(3).forEach { st ->
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable { currentStatus = st },
                                color = if (currentStatus == st) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = st,
                                    fontSize = 11.sp,
                                    color = if (currentStatus == st) Color.White else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(6.dp)
                                )
                            }
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        allStatuses.drop(3).forEach { st ->
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable { currentStatus = st },
                                color = if (currentStatus == st) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = st,
                                    fontSize = 11.sp,
                                    color = if (currentStatus == st) Color.White else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(6.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { isVerified = !isVerified }
                    ) {
                        androidx.compose.material3.Checkbox(
                            checked = isVerified,
                            onCheckedChange = { isVerified = it }
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Payment Verified & Matched", fontSize = 13.sp)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateOrderStatus(o.id, currentStatus)
                        viewModel.verifyPayment(o.id, isVerified)
                        selectedOrderForStatus = null
                        Toast.makeText(context, "Order updated", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Text("Apply Changes")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedOrderForStatus = null }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
fun MetricCard(
    title: String,
    amount: String,
    subtitle: String,
    icon: ImageVector,
    cardColor: Color,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = iconColor)
                Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = amount,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF1E293B)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = Color(0xFF64748B)
            )
        }
    }
}

fun generateCsvReport(
    orders: List<OrderWithItems>,
    expenses: List<ExpenseEntity>,
    metrics: FinancialMetrics
): String {
    val sb = StringBuilder()
    sb.appendLine("SHAHI AGRO FARM — FINANCIAL & ORDER REPORT")
    sb.appendLine("Generated At,${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date())}")
    sb.appendLine("Total Revenue,${metrics.totalRevenue}")
    sb.appendLine("Total Orders,${metrics.totalOrdersCount}")
    sb.appendLine("Estimated COGS,${metrics.cogs}")
    sb.appendLine("Gross Profit,${metrics.grossProfit}")
    sb.appendLine("Total Expenses,${metrics.totalExpenses}")
    sb.appendLine("Net Profit,${metrics.netProfit}")
    sb.appendLine()
    sb.appendLine("Order Number,Date,Customer Name,Phone,Address,Payment Method,TrxID,Status,Total (BDT)")
    orders.forEach { item ->
        val o = item.order
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date(o.createdAt))
        val escapedAddress = "\"${o.deliveryAddress.replace("\"", "\"\"")}\""
        sb.appendLine("${o.orderNumber},$date,${o.customerName},${o.customerPhone},$escapedAddress,${o.paymentMethod},${o.transactionId},${o.status},${o.totalAmount}")
    }
    return sb.toString()
}
