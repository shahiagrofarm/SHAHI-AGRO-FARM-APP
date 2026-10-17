package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.data.model.AppLanguage
import com.example.ui.theme.ShahiForestGreen
import com.example.ui.theme.ShahiGold
import com.example.ui.util.AppStrings
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.ShahiViewModel

@Composable
fun AboutScreen(
    viewModel: ShahiViewModel,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("about_screen")
    ) {
        // Logo & Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_shahi_logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(46.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = AppStrings.t("app_title", language),
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = AppStrings.t("tagline", language),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Farm Video / Story Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/share/v/1CFdFC5hdS/"))
                    context.startActivity(intent)
                },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = ShahiForestGreen),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.PlayCircle,
                        contentDescription = "Watch Video",
                        tint = Color.White,
                        modifier = Modifier.size(52.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (language == AppLanguage.BN) "আমাদের খামারের ভিডিও গল্প দেখুন" else "Watch Our Farm Story (Video)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                    Text(
                        text = if (language == AppLanguage.BN) "ফেসবুক ভিডিওতে ক্লিক করুন" else "Watch on Facebook Video",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Mission & Vision
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = AppStrings.t("mission", language),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (language == AppLanguage.BN)
                        "শাহী এগ্রো ফার্মের মূল লক্ষ্য হলো বাংলাদেশের দুগ্ধ ও গবাদিপশু পালনকারী ভাইদের জন্য সাশ্রয়ী মূল্যে বৈজ্ঞানিক পুষ্টিগুণসমৃদ্ধ কাঁচা ঘাসের সাইলেজ সরবরাহ করা এবং প্রতিটি পরিবারে খাঁটি ও স্বাস্থ্যসম্মত গাওয়া ঘি পৌঁছে দেওয়া।"
                    else
                        "Our mission is to empower dairy and cattle farmers across Bangladesh by supplying scientifically fermented, high-nutrition grass silage at affordable farm-gate prices, and bringing 100% pure, natural cow ghee directly from our farm to your home.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Grass Silage Activities
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE8F5E9)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Eco, contentDescription = null, tint = ShahiForestGreen, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (language == AppLanguage.BN) "ঘাসের সাইলেজ উৎপাদন" else "Grass Silage Production",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (language == AppLanguage.BN)
                        "আমরা সুবর্ণচরের বিস্তৃত উর্বর জমিতে নিজস্ব তত্ত্বাবধানে হাইব্রিড নেপিয়ার ও ভুট্টা ঘাস চাষ করি। সঠিক সময়ে কর্তন করে আধুনিক চপার মেশিনে গুঁড়ো করে বৈজ্ঞানিক অ্যানারোবিক (বাতাসহীন) গাঁজন প্রক্রিয়ায় ৫০ কেজির এয়ারটাইট ব্যাগে প্যাক করা হয়।"
                    else
                        "We cultivate hybrid Napier and fodder corn on the fertile lands of Subarnachar, Noakhali. Harvested at the optimum nutritional peak, finely chopped using precision machinery, and sealed in airtight 50 kg packets through anaerobic fermentation.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 19.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Pure Ghee Production
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFF8E1)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = ShahiGold, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (language == AppLanguage.BN) "শাহী খাঁটি গাওয়া ঘি" else "SHAHI Artisan Cow Ghee",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (language == AppLanguage.BN)
                        "আমাদের নিজস্ব দুগ্ধ খামারের সুষম খাদ্যে পালিত গাভীর খাঁটি দুধ থেকে মাখন তুলে ঐতিহ্যবাহী ধীর জ্বালে প্রস্তুত করা হয়। এতে কোনো রঙ, রাসায়নিক বা সুবাস মেশানো হয় না।"
                    else
                        "Prepared from the pure milk of grass-fed cows on our farm. We follow traditional slow-simmering processes to produce granulated, golden, highly aromatic cow ghee with zero preservatives or chemical additives.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 19.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Contact Button Link
        Button(
            onClick = { viewModel.navigateTo(Screen.Contact) },
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(
                text = AppStrings.t("contact_us", language),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}
