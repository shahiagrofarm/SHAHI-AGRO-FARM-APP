package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.ExpenseEntity
import com.example.data.local.OrderEntity
import com.example.data.local.OrderItemEntity
import com.example.data.local.OrderWithItems
import com.example.data.local.ProductEntity
import com.example.data.model.CartItem
import com.example.data.model.ProductItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

data class FinancialMetrics(
    val totalRevenue: Double,
    val totalOrdersCount: Int,
    val pendingOrdersCount: Int,
    val completedOrdersCount: Int,
    val cogs: Double,
    val grossProfit: Double,
    val totalExpenses: Double,
    val netProfit: Double
)

class ShahiRepository(private val database: AppDatabase) {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems = _cartItems.asStateFlow()

    fun getAllProducts(): Flow<List<ProductItem>> {
        return database.productDao().getAllProducts().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun getProductById(productId: String): ProductItem? {
        return database.productDao().getProductById(productId)?.toDomain()
    }

    fun getAllOrders(): Flow<List<OrderWithItems>> {
        return database.orderDao().getAllOrdersWithItems()
    }

    suspend fun getOrderByNumber(orderNumber: String): OrderWithItems? {
        return database.orderDao().getOrderByNumber(orderNumber)
    }

    fun getAllExpenses(): Flow<List<ExpenseEntity>> {
        return database.expenseDao().getAllExpenses()
    }

    // Cart Operations
    fun addToCart(product: ProductItem, quantity: Int) {
        val currentList = _cartItems.value.toMutableList()
        val index = currentList.indexOfFirst { it.product.id == product.id }
        if (index != -1) {
            val existing = currentList[index]
            val newQty = (existing.quantity + quantity).coerceIn(product.minOrder, product.maxOrder)
            currentList[index] = existing.copy(quantity = newQty)
        } else {
            val validQty = quantity.coerceIn(product.minOrder, product.maxOrder)
            currentList.add(CartItem(product, validQty))
        }
        _cartItems.value = currentList
    }

    fun updateCartQuantity(productId: String, newQuantity: Int) {
        val currentList = _cartItems.value.toMutableList()
        val index = currentList.indexOfFirst { it.product.id == productId }
        if (index != -1) {
            val item = currentList[index]
            if (newQuantity <= 0) {
                currentList.removeAt(index)
            } else {
                val clamped = newQuantity.coerceIn(item.product.minOrder, item.product.maxOrder)
                currentList[index] = item.copy(quantity = clamped)
            }
            _cartItems.value = currentList
        }
    }

    fun removeFromCart(productId: String) {
        _cartItems.value = _cartItems.value.filter { it.product.id != productId }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    // Order Placement
    suspend fun placeOrder(
        customerName: String,
        customerPhone: String,
        customerWhatsapp: String,
        deliveryAddress: String,
        paymentMethod: String,
        senderPhone: String,
        transactionId: String,
        notes: String = ""
    ): OrderWithItems {
        val cart = _cartItems.value
        require(cart.isNotEmpty()) { "Cart is empty" }

        val subtotal = cart.sumOf { it.subtotal }
        val totalWeight = cart.sumOf { it.totalWeightKg }
        val deliveryCharge = if (cart.any { it.product.category == "silage" }) {
            // Silage delivery: bulk transport estimation or confirmed with seller
            0.0 // shown as Confirm with seller / ৳0 at checkout
        } else {
            130.0 // Standard courier for ghee
        }
        val discount = 0.0
        val totalAmount = subtotal + deliveryCharge - discount

        // Generate clean unique Order ID like #SAF20261019
        val datePrefix = SimpleDateFormat("yyyyMMdd", Locale.US).format(Date())
        val randomSuffix = Random.nextInt(100, 999)
        val orderNumber = "#SAF$datePrefix$randomSuffix"

        val orderEntity = OrderEntity(
            orderNumber = orderNumber,
            customerName = customerName,
            customerPhone = customerPhone,
            customerWhatsapp = customerWhatsapp.ifBlank { customerPhone },
            deliveryAddress = deliveryAddress,
            paymentMethod = paymentMethod,
            senderPhone = senderPhone,
            transactionId = transactionId,
            subtotal = subtotal,
            deliveryCharge = deliveryCharge,
            discount = discount,
            totalAmount = totalAmount,
            totalWeightKg = totalWeight,
            status = if (paymentMethod == "Cash on Delivery") "PENDING" else "PENDING",
            isPaymentVerified = false,
            createdAt = System.currentTimeMillis(),
            notes = notes
        )

        val insertedOrderId = database.orderDao().insertOrder(orderEntity)

        val items = cart.map { cartItem ->
            OrderItemEntity(
                orderId = insertedOrderId,
                productId = cartItem.product.id,
                productName = cartItem.product.nameEn,
                variant = cartItem.product.variantEn,
                unitPrice = cartItem.product.price,
                quantity = cartItem.quantity,
                subtotal = cartItem.subtotal,
                weightKg = cartItem.totalWeightKg
            )
        }
        database.orderDao().insertOrderItems(items)

        // Decrement stock for inventory accuracy
        for (item in cart) {
            val currentProduct = database.productDao().getProductById(item.product.id)
            if (currentProduct != null) {
                val newStock = (currentProduct.stockQuantity - item.quantity).coerceAtLeast(0)
                database.productDao().updateStock(item.product.id, newStock)
            }
        }

        clearCart()
        return database.orderDao().getOrderById(insertedOrderId)!!
    }

    suspend fun updateOrderStatus(orderId: Long, status: String) {
        database.orderDao().updateOrderStatus(orderId, status)
    }

    suspend fun updatePaymentVerification(orderId: Long, verified: Boolean) {
        database.orderDao().updatePaymentVerification(orderId, verified)
    }

    suspend fun addExpense(title: String, amount: Double, category: String) {
        database.expenseDao().insertExpense(
            ExpenseEntity(
                title = title,
                amount = amount,
                category = category
            )
        )
    }

    suspend fun updateProductStock(productId: String, newStock: Int) {
        database.productDao().updateStock(productId, newStock)
    }

    fun calculateFinancialMetrics(
        orders: List<OrderWithItems>,
        expenses: List<ExpenseEntity>
    ): FinancialMetrics {
        val nonCancelled = orders.filter { it.order.status != "CANCELLED" }
        val revenue = nonCancelled.sumOf { it.order.totalAmount }
        val totalCount = orders.size
        val pendingCount = orders.count { it.order.status == "PENDING" || it.order.status == "PROCESSING" }
        val completedCount = orders.count { it.order.status == "DELIVERED" }

        // Estimate Cost of Goods Sold (approx 55% of product revenue for grass farming + packing)
        val cogs = nonCancelled.sumOf { it.order.subtotal * 0.55 }
        val grossProfit = revenue - cogs
        val totalExpenses = expenses.sumOf { it.amount }
        val netProfit = grossProfit - totalExpenses

        return FinancialMetrics(
            totalRevenue = revenue,
            totalOrdersCount = totalCount,
            pendingOrdersCount = pendingCount,
            completedOrdersCount = completedCount,
            cogs = cogs,
            grossProfit = grossProfit,
            totalExpenses = totalExpenses,
            netProfit = netProfit
        )
    }
}

fun ProductEntity.toDomain(): ProductItem {
    return ProductItem(
        id = id,
        nameEn = nameEn,
        nameBn = nameBn,
        category = category,
        variantEn = variantEn,
        variantBn = variantBn,
        price = price,
        unitEn = unitEn,
        unitBn = unitBn,
        minOrder = minOrder,
        maxOrder = maxOrder,
        unitWeightKg = unitWeightKg,
        stockQuantity = stockQuantity,
        rating = rating,
        reviewCount = reviewCount,
        descriptionEn = descriptionEn,
        descriptionBn = descriptionBn,
        benefitsEn = benefitsEn.split(";").filter { it.isNotBlank() },
        benefitsBn = benefitsBn.split(";").filter { it.isNotBlank() },
        storageInstructionsEn = storageEn,
        storageInstructionsBn = storageBn
    )
}
