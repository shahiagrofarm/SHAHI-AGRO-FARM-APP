package com.example.ui.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.ExpenseEntity
import com.example.data.local.OrderWithItems
import com.example.data.model.AppLanguage
import com.example.data.model.CartItem
import com.example.data.model.ProductItem
import com.example.data.repository.FinancialMetrics
import com.example.data.repository.ShahiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

sealed class Screen {
    object Home : Screen()
    object Products : Screen()
    data class ProductDetail(val productId: String) : Screen()
    object Cart : Screen()
    object Checkout : Screen()
    data class OrderSuccess(val order: OrderWithItems) : Screen()
    object Orders : Screen()
    data class OrderDetail(val orderId: Long) : Screen()
    object About : Screen()
    object Contact : Screen()
    object AdminDashboard : Screen()
    object Settings : Screen()
}

class ShahiViewModel(private val repository: ShahiRepository) : ViewModel() {

    private val _currentLanguage = MutableStateFlow(AppLanguage.EN)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _currentScreen = MutableStateFlow<Screen>(Screen.Home)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _navigationStack = mutableListOf<Screen>(Screen.Home)

    val products: StateFlow<List<ProductItem>> = repository.getAllProducts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cartItems: StateFlow<List<CartItem>> = repository.cartItems

    val orders: StateFlow<List<OrderWithItems>> = repository.getAllOrders()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val expenses: StateFlow<List<ExpenseEntity>> = repository.getAllExpenses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val financialMetrics: StateFlow<FinancialMetrics> = combine(orders, expenses) { ords, exps ->
        repository.calculateFinancialMetrics(ords, exps)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        FinancialMetrics(0.0, 0, 0, 0, 0.0, 0.0, 0.0, 0.0)
    )

    fun toggleLanguage() {
        _currentLanguage.value = if (_currentLanguage.value == AppLanguage.EN) AppLanguage.BN else AppLanguage.EN
    }

    fun toggleTheme() {
        _isDarkMode.value = !_isDarkMode.value
    }

    fun navigateTo(screen: Screen) {
        if (_currentScreen.value != screen) {
            _navigationStack.add(screen)
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        if (_navigationStack.size > 1) {
            _navigationStack.removeAt(_navigationStack.size - 1)
            _currentScreen.value = _navigationStack.last()
            return true
        }
        return false
    }

    fun addToCart(product: ProductItem, quantity: Int) {
        repository.addToCart(product, quantity)
    }

    fun updateCartQuantity(productId: String, newQuantity: Int) {
        repository.updateCartQuantity(productId, newQuantity)
    }

    fun removeFromCart(productId: String) {
        repository.removeFromCart(productId)
    }

    fun clearCart() {
        repository.clearCart()
    }

    fun placeOrder(
        customerName: String,
        customerPhone: String,
        customerWhatsapp: String,
        deliveryAddress: String,
        paymentMethod: String,
        senderPhone: String,
        transactionId: String,
        notes: String,
        onSuccess: (OrderWithItems) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val order = repository.placeOrder(
                    customerName = customerName,
                    customerPhone = customerPhone,
                    customerWhatsapp = customerWhatsapp,
                    deliveryAddress = deliveryAddress,
                    paymentMethod = paymentMethod,
                    senderPhone = senderPhone,
                    transactionId = transactionId,
                    notes = notes
                )
                onSuccess(order)
            } catch (e: Exception) {
                onError(e.localizedMessage ?: "Failed to place order")
            }
        }
    }

    fun updateOrderStatus(orderId: Long, status: String) {
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, status)
        }
    }

    fun verifyPayment(orderId: Long, verified: Boolean) {
        viewModelScope.launch {
            repository.updatePaymentVerification(orderId, verified)
        }
    }

    fun addExpense(title: String, amount: Double, category: String) {
        viewModelScope.launch {
            repository.addExpense(title, amount, category)
        }
    }

    fun updateStock(productId: String, stock: Int) {
        viewModelScope.launch {
            repository.updateProductStock(productId, stock)
        }
    }

    fun sendOrderWhatsApp(context: Context, order: OrderWithItems) {
        val o = order.order
        val itemsSummary = order.items.joinToString("\n") {
            "• ${it.productName} (${it.variant}) × ${it.quantity} = ৳${String.format(Locale.US, "%,.0f", it.subtotal)}"
        }
        val message = """
*SHAHI AGRO FARM — ORDER CONFIRMATION*
━━━━━━━━━━━━━━━━━━━━
*Order Number:* ${o.orderNumber}
*Customer:* ${o.customerName}
*Phone:* ${o.customerPhone}
${if (o.customerWhatsapp.isNotBlank() && o.customerWhatsapp != o.customerPhone) "*WhatsApp:* ${o.customerWhatsapp}\n" else ""}*Delivery Address:* ${o.deliveryAddress}

*Order Items:*
$itemsSummary

*Total Weight:* ${String.format(Locale.US, "%,.1f", o.totalWeightKg)} kg
*Subtotal:* ৳${String.format(Locale.US, "%,.0f", o.subtotal)}
*Delivery Charge:* ${if (o.deliveryCharge > 0) "৳" + String.format(Locale.US, "%,.0f", o.deliveryCharge) else "To be confirmed by seller"}
*Total Payable:* ৳${String.format(Locale.US, "%,.0f", o.totalAmount)}

*Payment Method:* ${o.paymentMethod}
${if (o.transactionId.isNotBlank()) "*Sender Phone:* ${o.senderPhone}\n*TrxID:* ${o.transactionId}\n" else ""}*Current Status:* ${o.status}

_Please confirm stock availability and dispatch schedule._
        """.trimIndent()

        val whatsappNumber = "8801814026318"
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://wa.me/$whatsappNumber?text=${Uri.encode(message)}")
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Could not open WhatsApp: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}

class ShahiViewModelFactory(private val repository: ShahiRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShahiViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ShahiViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
