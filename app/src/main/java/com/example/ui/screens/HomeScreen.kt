package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AppLanguage
import com.example.data.model.ProductItem
import com.example.ui.components.ProductCard
import com.example.ui.theme.ShahiEmerald
import com.example.ui.theme.ShahiForestGreen
import com.example.ui.theme.ShahiGold
import com.example.ui.theme.ShahiLeafGreen
import com.example.ui.theme.ShahiWarmCream
import com.example.ui.util.AppStrings
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.ShahiViewModel

@Composable
fun HomeScreen(
    viewModel: ShahiViewModel,
    language: AppLanguage,
    products: List<ProductItem>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen"),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 24.dp)
    ) {
        // Hero Section
        item {
            HeroSection(
                language = language,
                onOrderNow = { viewModel.navigateTo(Screen.Products) },
                modifier = Modifier.padding(16.dp)
            )
        }

        // Trust Highlights (4 key pillars)
        item {
            TrustHighlightsRow(
                language = language,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Section: Our Products
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = AppStrings.t("our_products", language),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = AppStrings.t("slogan", language),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                TextButton(
                    onClick = { viewModel.navigateTo(Screen.Products) },
                    modifier = Modifier.testTag("view_all_products_btn")
                ) {
                    Text(
                        text = AppStrings.t("view_all", language),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Product Cards
        items(products.size) { index ->
            val product = products[index]
            ProductCard(
                product = product,
                language = language,
                onProductClick = { viewModel.navigateTo(Screen.ProductDetail(product.id)) },
                onAddToCart = { qty -> viewModel.addToCart(product, qty) },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }

        // Why Choose SHAHI Agro Silage Section
        item {
            Spacer(modifier = Modifier.height(18.dp))
            BenefitsBanner(
                language = language,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        // Farm Story Teaser
        item {
            Spacer(modifier = Modifier.height(18.dp))
            FarmStoryTeaser(
                language = language,
                onReadMore = { viewModel.navigateTo(Screen.About) },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        // FAQ Section
        item {
            Spacer(modifier = Modifier.height(20.dp))
            FaqSection(
                language = language,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        // Contact & Support Banner
        item {
            Spacer(modifier = Modifier.height(20.dp))
            ContactBanner(
                language = language,
                onContactClick = { viewModel.navigateTo(Screen.Contact) },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
fun HeroSection(
    language: AppLanguage,
    onOrderNow: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFD4EED8),
                            Color(0xFFEBF6E4),
                            Color(0xFFF9F5DE)
                        )
                    )
                )
                .padding(22.dp)
        ) {
            Column {
                Surface(
                    color = ShahiForestGreen.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = if (language == AppLanguage.BN) "প্রাকৃতিক খাবার • সুস্থ গবাদিপশু" else "NATURAL FEED • HEALTHIER HERD",
                        color = ShahiForestGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.1.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = AppStrings.t("hero_headline", language),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Black,
                    lineHeight = 32.sp,
                    color = Color(0xFF10361C)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = AppStrings.t("hero_sub", language),
                    fontSize = 14.sp,
                    color = Color(0xFF33583E),
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onOrderNow,
                        colors = ButtonDefaults.buttonColors(containerColor = ShahiForestGreen),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("hero_order_now_btn")
                    ) {
                        Text(
                            text = AppStrings.t("order_now", language),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color.White
                        )
                    }

                    Surface(
                        color = Color.White.copy(alpha = 0.85f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "50 kg · ৳350",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = ShahiForestGreen,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TrustHighlightsRow(
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TrustItem(
            icon = Icons.Default.Grass,
            title = AppStrings.t("natural_feed", language),
            modifier = Modifier.weight(1f)
        )
        TrustItem(
            icon = Icons.Default.LocalDrink,
            title = AppStrings.t("better_milk", language),
            modifier = Modifier.weight(1f)
        )
        TrustItem(
            icon = Icons.Default.Favorite,
            title = AppStrings.t("healthy_cattle", language),
            modifier = Modifier.weight(1f)
        )
        TrustItem(
            icon = Icons.Default.Verified,
            title = AppStrings.t("trusted_quality", language),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun TrustItem(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
        }
    }
}

@Composable
fun BenefitsBanner(
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Grass,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (language == AppLanguage.BN) "ঘাসের সাইলেজের বিশেষত্ব" else "Why Choose Shahi Silage?",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (language == AppLanguage.BN) "বৈজ্ঞানিক গাঁজন প্রক্রিয়ায় পুষ্টি সংরক্ষণ" else "Scientifically fermented green forage",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            val benefits = if (language == AppLanguage.BN) listOf(
                "উচ্চ ফাইবার সমৃদ্ধ, রুমেন হজম ক্ষমতা বৃদ্ধি করে",
                "সারা বছর কাঁচা ঘাসের নিরবচ্ছিন্ন যোগান নিশ্চিত",
                "গাভীর দুধের পরিমাণ ও ফ্যাটের ঘনত্ব বাড়াতে সহায়ক",
                "ল্যাকটিক ফার্মেন্টেশনের কারণে প্রোটিন নষ্ট হয় না",
                "দানাদার খাবারের অতিরিক্ত খরচ উল্লেখযোগ্যভাবে কমায়"
            ) else listOf(
                "Rich in fiber, supports healthy rumen & digestion",
                "Guarantees year-round green forage supply",
                "Boosts daily milk yield and natural fat content",
                "Lactic fermentation seals and preserves vital nutrients",
                "Significantly reduces commercial concentrated feed costs"
            )

            benefits.forEach { benefit ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .size(18.dp)
                            .padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = benefit,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
fun FarmStoryTeaser(
    language: AppLanguage,
    onReadMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = if (language == AppLanguage.BN) "আমাদের খামার সম্পর্কে" else "About SHAHI AGRO FARM",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (language == AppLanguage.BN)
                    "নোয়াখালীর সুবর্ণচরে প্রতিষ্ঠিত শাহী এগ্রো ফার্ম আধুনিক কৃষি, উন্নত গবাদিপশু পালন এবং ১০০% ভেজালমুক্ত পুষ্টিকর খাদ্য উৎপাদনে নিবেদিত।"
                else
                    "Located in Subarnachar, Noakhali, SHAHI AGRO FARM is dedicated to modern organic farming, healthy livestock feeding, and 100% pure dairy products.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onReadMore)
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (language == AppLanguage.BN) "বিস্তারিত পড়ুন →" else "Read Farm Story →",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun FaqSection(
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val faqs = if (language == AppLanguage.BN) listOf(
        Pair("সাইলেজের প্যাকেটের ওজন ও মূল্য কত?", "প্রতিটি প্যাকেট ৫০ কেজি এবং মূল্য মাত্র ৩৫০ টাকা। সর্বনিম্ন অর্ডার ২০ প্যাকেট (১,০০০ কেজি)।"),
        Pair("খামারে ডেলিভারি কীভাবে হয়?", "নোয়াখালী ও পার্শ্ববর্তী জেলায় পিকআপ ট্রাকে এবং সারাদেশে বিশ্বস্ত ট্রান্সপোর্ট বা কুরিয়ারে সরাসরি আপনার খামারে পাঠানো হয়।"),
        Pair("পেমেন্ট কীভাবে করবেন?", "বিকাশ, নগদ, উপায়, রকেট ও নির্ধারিত ডেলিভারি শর্তে ক্যাশ অন ডেলিভারি (COD) প্রযোজ্য।"),
        Pair("গাভীকে দিনে কতটুকু সাইলেজ খাওয়াবো?", "একটি প্রাপ্তবয়স্ক দুগ্ধ গাভীকে দৈনিক ১০-১৫ কেজি এবং ষাঁড়কে ৫-১০ কেজি সাইলেজ খাওয়ানো স্বাস্থ্যসম্মত।")
    ) else listOf(
        Pair("What is the packaging and price of silage?", "Each packet weighs 50 kg and costs ৳350. Minimum order is 20 packets (1,000 kg)."),
        Pair("How is silage transported to my farm?", "We ship via farm pickup trucks in Noakhali/Chittagong and reliable nationwide transport services directly to your farm."),
        Pair("Which payment methods are accepted?", "We support bKash, Nagad, Upay, Rocket, and Cash on Delivery (COD) as per farm policy."),
        Pair("What is the daily feeding ration per cow?", "Adult milking cows are typically fed 10-15 kg per day, while beef cattle consume 5-10 kg daily alongside regular ration.")
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = if (language == AppLanguage.BN) "সাধারণ জিজ্ঞাসা (FAQ)" else "Frequently Asked Questions",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(10.dp))

        faqs.forEach { (question, answer) ->
            var expanded by remember { mutableStateOf(false) }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { expanded = !expanded },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = question,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    AnimatedVisibility(visible = expanded) {
                        Column {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = answer,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ContactBanner(
    language: AppLanguage,
    onContactClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onContactClick),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Contact",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (language == AppLanguage.BN) "সরাসরি যোগাযোগ ও সহায়তা" else "Direct Farm Helpline",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "01799900886 / 01814026318",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
