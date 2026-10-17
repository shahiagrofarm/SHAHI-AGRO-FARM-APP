package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ProductEntity::class,
        OrderEntity::class,
        OrderItemEntity::class,
        ExpenseEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun orderDao(): OrderDao
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "shahi_agro_farm.db"
                ).addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            INSTANCE?.let { seedInitialData(it) }
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }

        suspend fun seedInitialData(db: AppDatabase) {
            val products = listOf(
                ProductEntity(
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
                    descriptionEn = "High nutrition forage silage prepared from fresh chopped hybrid Napier & corn fodder. Packed in heavy-duty airtight 50 kg bags to preserve maximum nutrients, protein, and energy for dairy cattle and beef fattening.",
                    descriptionBn = "তাজা হাইব্রিড নেপিয়ার ও ভুট্টা কাঁচা ঘাস থেকে বৈজ্ঞানিক পদ্ধতিতে প্রস্তুতকৃত উচ্চ পুষ্টিসমৃদ্ধ ঘাসের সাইলেজ। ৫০ কেজির এয়ারটাইট প্যাকেটে সুরক্ষিত, যা দুগ্ধ গাভী ও মোটাতাজাকরণ গবাদিপশুর দুধের উৎপাদন ও স্বাস্থ্য বৃদ্ধি করে।",
                    benefitsEn = "Rich in digestible fiber;Year-round consistent green feed;Boosts milk yield & fat percentage;Lactic fermentation preserves protein;Reduces feed cost significantly",
                    benefitsBn = "সহজে হজমযোগ্য উচ্চ ফাইবার;সারা বছর কাঁচা ঘাসের জোগান;দুধের উৎপাদন ও ফ্যাট বৃদ্ধি;ল্যাকটিক ফার্মেন্টেশনে প্রোটিন অক্ষুণ্ণ থাকে;দানাদার খাদ্যের খরচ কমায়",
                    storageEn = "Store in a dry shaded place away from direct sunlight and sharp objects. Use within 7 days once opened.",
                    storageBn = "সরাসরি রোদ ও ধারালো বস্তু থেকে দূরে শুষ্ক ছায়াযুক্ত স্থানে রাখুন। প্যাকেট খোলার ৭ দিনের মধ্যে খাইয়ে শেষ করুন।"
                ),
                ProductEntity(
                    id = "ghee-900g",
                    nameEn = "SHAHI GHEE (900 g)",
                    nameBn = "শাহী খাঁটি গাওয়া ঘি (৯০০ গ্রাম)",
                    category = "ghee",
                    variantEn = "900 g Large Jar",
                    variantBn = "৯০০ গ্রাম বড় জার",
                    price = 1450.0,
                    unitEn = "jar",
                    unitBn = "জার",
                    minOrder = 1,
                    maxOrder = 50,
                    unitWeightKg = 0.9,
                    stockQuantity = 380,
                    rating = 5.0,
                    reviewCount = 142,
                    descriptionEn = "100% pure artisan cow ghee slowly simmered from cultured organic cow milk cream at our Subarnachar farm. Fragrant, granular, golden, and completely chemical-free.",
                    descriptionBn = "আমাদের সুবর্ণচর খামারের নিজস্ব গাভীর দুধের মাখন থেকে ঐতিহ্যবাহী ধীর জ্বালে তৈরি ১০০% খাঁটি গাওয়া ঘি। দানাদার সুঘ্রাণযুক্ত, ভেজাল ও প্রিজারভেটিভমুক্ত।",
                    benefitsEn = "Boosts immunity & gut health;Rich in vitamins A, D, E & K;Natural aroma & granulated texture;Zero chemicals, colors or preservatives;Traditional slow-cooked bilona process",
                    benefitsBn = "রোগ প্রতিরোধ ক্ষমতা ও হজমে সহায়ক;প্রাকৃতিক ভিটামিন এ, ডি, ই ও কে সমৃদ্ধ;মনোরম প্রাকৃতিক সুবাস ও দানাদার টেক্সচার;কোনোরূপ রাসায়নিক বা রঙ ছাড়া প্রস্তুত;ঐতিহ্যবাহী ধীর জ্বালে তৈরি",
                    storageEn = "Keep sealed at room temperature in a cool dry place. Use clean dry spoon. No refrigeration required.",
                    storageBn = "সাধারণ তাপমাত্রায় শুকনা জায়গায় মুখ বন্ধ করে রাখুন। ভেজা চামচ ব্যবহার করবেন না। ফ্রিজে রাখার প্রয়োজন নেই।"
                ),
                ProductEntity(
                    id = "ghee-200g",
                    nameEn = "SHAHI GHEE (200 g)",
                    nameBn = "শাহী খাঁটি গাওয়া ঘি (২০০ গ্রাম)",
                    category = "ghee",
                    variantEn = "200 g Small Jar",
                    variantBn = "২০০ গ্রাম ছোট জার",
                    price = 800.0,
                    unitEn = "jar",
                    unitBn = "জার",
                    minOrder = 1,
                    maxOrder = 100,
                    unitWeightKg = 0.2,
                    stockQuantity = 520,
                    rating = 4.9,
                    reviewCount = 98,
                    descriptionEn = "Handcrafted pure farm cow ghee in a convenient 200 g size for daily family nutrition, baby food, and aromatic culinary preparation.",
                    descriptionBn = "পরিবারের নিত্যদিনের পুষ্টি, বাচ্চাদের খাবার ও রান্নার সুবাস বৃদ্ধিতে সুবিধাজনক ২০০ গ্রামের কাঁচের পাত্রে শাহী খাঁটি গাওয়া ঘি।",
                    benefitsEn = "Convenient small family size;Aromatic granulated pure cow ghee;Rich natural source of healthy fats;Safe for children & elders",
                    benefitsBn = "ছোট পরিবারের জন্য আদর্শ সাইজ;বিশুদ্ধ দানাদার গাওয়া ঘি;উপকারী ফ্যাটি অ্যাসিডে ভরপুর;শিশু ও বয়স্কদের জন্য নিরাপদ ও পুষ্টিকর",
                    storageEn = "Keep in a cool dry place with lid tightly closed. Store at room temperature.",
                    storageBn = "মুখ ভালোভাবে বন্ধ করে শীতল ও শুষ্ক স্থানে রাখুন।"
                )
            )
            db.productDao().insertProducts(products)

            // Seed sample orders matching the user mockup
            val o1Id = db.orderDao().insertOrder(
                OrderEntity(
                    orderNumber = "#SAF20261004",
                    customerName = "Rafiqul Islam (Al-Madina Dairy)",
                    customerPhone = "01712345678",
                    customerWhatsapp = "01712345678",
                    deliveryAddress = "Sonapur, Sadar, Noakhali",
                    paymentMethod = "bKash",
                    senderPhone = "01712345678",
                    transactionId = "BKH9X42A19",
                    subtotal = 7000.0,
                    deliveryCharge = 0.0,
                    discount = 0.0,
                    totalAmount = 7000.0,
                    totalWeightKg = 1000.0,
                    status = "PROCESSING",
                    isPaymentVerified = true,
                    createdAt = System.currentTimeMillis() - (2 * 86400000L),
                    notes = "Priority delivery for 20 milking cows"
                )
            )
            db.orderDao().insertOrderItems(
                listOf(
                    OrderItemEntity(
                        orderId = o1Id,
                        productId = "silage-50kg",
                        productName = "SHAHI GRASS SILAGE",
                        variant = "50 kg Packet",
                        unitPrice = 350.0,
                        quantity = 20,
                        subtotal = 7000.0,
                        weightKg = 1000.0
                    )
                )
            )

            val o2Id = db.orderDao().insertOrder(
                OrderEntity(
                    orderNumber = "#SAF20261003",
                    customerName = "Dr. Aminul Haque",
                    customerPhone = "01823456789",
                    customerWhatsapp = "01823456789",
                    deliveryAddress = "Dhanmondi Road 8A, Dhaka",
                    paymentMethod = "Nagad",
                    senderPhone = "01823456789",
                    transactionId = "NGD8827361",
                    subtotal = 1450.0,
                    deliveryCharge = 130.0,
                    discount = 0.0,
                    totalAmount = 1580.0,
                    totalWeightKg = 0.9,
                    status = "SHIPPED",
                    isPaymentVerified = true,
                    createdAt = System.currentTimeMillis() - (4 * 86400000L),
                    notes = "Steadfast Courier tracking #ST98231"
                )
            )
            db.orderDao().insertOrderItems(
                listOf(
                    OrderItemEntity(
                        orderId = o2Id,
                        productId = "ghee-900g",
                        productName = "SHAHI GHEE (900 g)",
                        variant = "900 g Large Jar",
                        unitPrice = 1450.0,
                        quantity = 1,
                        subtotal = 1450.0,
                        weightKg = 0.9
                    )
                )
            )

            val o3Id = db.orderDao().insertOrder(
                OrderEntity(
                    orderNumber = "#SAF20261001",
                    customerName = "Haji Nurul Huda Agro",
                    customerPhone = "01912345678",
                    customerWhatsapp = "01912345678",
                    deliveryAddress = "Char Bata, Subarnachar, Noakhali",
                    paymentMethod = "Cash on Delivery",
                    senderPhone = "",
                    transactionId = "",
                    subtotal = 3500.0,
                    deliveryCharge = 0.0,
                    discount = 0.0,
                    totalAmount = 3500.0,
                    totalWeightKg = 500.0,
                    status = "DELIVERED",
                    isPaymentVerified = true,
                    createdAt = System.currentTimeMillis() - (7 * 86400000L),
                    notes = "Delivered via farm pickup truck"
                )
            )
            db.orderDao().insertOrderItems(
                listOf(
                    OrderItemEntity(
                        orderId = o3Id,
                        productId = "silage-50kg",
                        productName = "SHAHI GRASS SILAGE",
                        variant = "50 kg Packet",
                        unitPrice = 350.0,
                        quantity = 10,
                        subtotal = 3500.0,
                        weightKg = 500.0
                    )
                )
            )

            // Seed initial farm expenses for realistic financial analytics
            db.expenseDao().insertExpense(
                ExpenseEntity(
                    title = "Napier & Corn Fodder Harvesting",
                    amount = 45000.0,
                    category = "Feed Production"
                )
            )
            db.expenseDao().insertExpense(
                ExpenseEntity(
                    title = "Silage Vacuum Packing Bags (50kg)",
                    amount = 18500.0,
                    category = "Packaging"
                )
            )
            db.expenseDao().insertExpense(
                ExpenseEntity(
                    title = "Food-grade Glass Jars & Safety Seals",
                    amount = 12000.0,
                    category = "Packaging"
                )
            )
            db.expenseDao().insertExpense(
                ExpenseEntity(
                    title = "Farm Chaff Cutter & Tractor Fuel",
                    amount = 9500.0,
                    category = "Farm Operations"
                )
            )
        }
    }
}
