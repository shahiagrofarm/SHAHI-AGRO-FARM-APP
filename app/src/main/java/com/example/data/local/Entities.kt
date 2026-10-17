package com.example.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val nameEn: String,
    val nameBn: String,
    val category: String,
    val variantEn: String,
    val variantBn: String,
    val price: Double,
    val unitEn: String,
    val unitBn: String,
    val minOrder: Int,
    val maxOrder: Int,
    val unitWeightKg: Double,
    val stockQuantity: Int,
    val rating: Double,
    val reviewCount: Int,
    val descriptionEn: String,
    val descriptionBn: String,
    val benefitsEn: String, // comma or semicolon separated
    val benefitsBn: String,
    val storageEn: String,
    val storageBn: String
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderNumber: String,
    val customerName: String,
    val customerPhone: String,
    val customerWhatsapp: String,
    val deliveryAddress: String,
    val paymentMethod: String,
    val senderPhone: String,
    val transactionId: String,
    val subtotal: Double,
    val deliveryCharge: Double,
    val discount: Double,
    val totalAmount: Double,
    val totalWeightKg: Double,
    val status: String, // PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED
    val isPaymentVerified: Boolean,
    val createdAt: Long = System.currentTimeMillis(),
    val notes: String = ""
)

@Entity(
    tableName = "order_items",
    foreignKeys = [
        ForeignKey(
            entity = OrderEntity::class,
            parentColumns = ["id"],
            childColumns = ["orderId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("orderId")]
)
data class OrderItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderId: Long,
    val productId: String,
    val productName: String,
    val variant: String,
    val unitPrice: Double,
    val quantity: Int,
    val subtotal: Double,
    val weightKg: Double
)

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val amount: Double,
    val category: String, // "Feed Production", "Packaging", "Logistics", "Farm Operations"
    val createdAt: Long = System.currentTimeMillis()
)
