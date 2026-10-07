package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.CoralPrimary
import com.example.ui.theme.GoldenAmber
import kotlinx.coroutines.delay

@Composable
fun HeroBanner(
    onExploreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Simulated countdown timer for flash deals
    var secondsLeft by remember { mutableIntStateOf(14 * 3600 + 42 * 60 + 19) }

    LaunchedEffect(Unit) {
        while (secondsLeft > 0) {
            delay(1000)
            secondsLeft--
        }
    }

    val hours = secondsLeft / 3600
    val minutes = (secondsLeft % 3600) / 60
    val seconds = secondsLeft % 60
    val timerText = String.format("%02d:%02d:%02d", hours, minutes, seconds)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 16.dp)
            .testTag("hero_promo_banner"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background image
            Image(
                painter = painterResource(id = R.drawable.img_hero_promo_1791340678447),
                contentDescription = "Flash sale promo banner",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Gradient Overlay for contrast & readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xDD0F172A),
                                Color(0x990F172A),
                                Color(0x440F172A)
                            )
                        )
                    )
            )

            // Banner Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top row with Flash Sale Badge & Timer
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = CoralPrimary
                    ) {
                        Text(
                            text = "FLASH SALE",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0x99000000)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = "Timer",
                                tint = GoldenAmber,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = timerText,
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                // Title & Subtitle
                Column {
                    Text(
                        text = "Autumn Tech & Style",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        lineHeight = 24.sp
                    )
                    Text(
                        text = "Up to 40% off curated audio, wearables & kicks",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }

                // CTA Button
                Button(
                    onClick = onExploreClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF0F172A)
                    ),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                    modifier = Modifier.testTag("hero_shop_now_btn")
                ) {
                    Text(
                        text = "Claim Deals",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Arrow",
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}
