package com.example.data.model

enum class AppLanguage {
    EN, BN
}

enum class OrderStatus(val labelEn: String, val labelBn: String) {
    PENDING("Pending", "অপেক্ষমান"),
    CONFIRMED("Confirmed", "নিশ্চিত"),
    PROCESSING("Processing", "প্রক্রিয়াধীন"),
    SHIPPED("Shipped", "পাঠানো হয়েছে"),
    DELIVERED("Delivered", "ডেলিভার সম্পন্ন"),
    CANCELLED("Cancelled", "বাতিল")
}

enum class PaymentType(
    val title: String,
    val accountNumber: String,
    val typeName: String,
    val instructionsEn: String,
    val instructionsBn: String
) {
    BKASH(
        title = "bKash",
        accountNumber = "01799900886",
        typeName = "Merchant / Personal",
        instructionsEn = "Send payment to bKash: 01799900886. Enter your Sender Number & TrxID below. Verification is done before dispatch.",
        instructionsBn = "বিকাশ নম্বর ০১৭৯৯৯০০৮৮৬-এ টাকা পাঠান। নিচে প্রেরক নম্বর ও ট্রানজেকশন আইডি দিন। পাঠানোর পূর্বে পেমেন্ট যাচাই করা হয়।"
    ),
    NAGAD(
        title = "Nagad",
        accountNumber = "01814026318",
        typeName = "Personal",
        instructionsEn = "Send payment to Nagad: 01814026318. Enter your Sender Number & TrxID below.",
        instructionsBn = "নগদ নম্বর ০১৮১৪০২৬৩১৮-এ টাকা পাঠান। নিচে প্রেরক নম্বর ও ট্রানজেকশন আইডি দিন।"
    ),
    UPAY(
        title = "Upay",
        accountNumber = "01814026318",
        typeName = "Personal",
        instructionsEn = "Send payment to Upay: 01814026318. Enter your Sender Number & TrxID below.",
        instructionsBn = "উপায় নম্বর ০১৮১৪০২৬৩১৮-এ টাকা পাঠান। নিচে প্রেরক নম্বর ও ট্রানজেকশন আইডি দিন।"
    ),
    ROCKET(
        title = "Rocket",
        accountNumber = "01604985164",
        typeName = "Personal",
        instructionsEn = "Send payment to Rocket: 01604985164. Enter your Sender Number & TrxID below.",
        instructionsBn = "রকেট নম্বর ০১৬০৪৯৮৫১৬৪-এ টাকা পাঠান। নিচে প্রেরক নম্বর ও ট্রানজেকশন আইডি দিন।"
    ),
    COD(
        title = "Cash on Delivery",
        accountNumber = "",
        typeName = "Pay on receipt",
        instructionsEn = "Pay with cash when your shipment arrives at your farm or home.",
        instructionsBn = "পণ্য আপনার খামার বা ঠিকানায় পৌঁছালে নগদে মূল্য পরিশোধ করুন।"
    )
}

data class ProductItem(
    val id: String,
    val nameEn: String,
    val nameBn: String,
    val category: String, // "silage", "ghee"
    val variantEn: String,
    val variantBn: String,
    val price: Double,
    val unitEn: String,
    val unitBn: String,
    val minOrder: Int,
    val maxOrder: Int,
    val unitWeightKg: Double,
    val stockQuantity: Int,
    val rating: Double = 4.9,
    val reviewCount: Int = 128,
    val descriptionEn: String,
    val descriptionBn: String,
    val benefitsEn: List<String>,
    val benefitsBn: List<String>,
    val storageInstructionsEn: String,
    val storageInstructionsBn: String
)

data class CartItem(
    val product: ProductItem,
    val quantity: Int
) {
    val subtotal: Double get() = product.price * quantity
    val totalWeightKg: Double get() = product.unitWeightKg * quantity
}
