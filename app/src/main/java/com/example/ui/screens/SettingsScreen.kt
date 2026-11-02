package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.GppGood
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppVersionConfig
import com.example.data.model.SocialUserProfile
import com.example.data.repository.SocialFirebaseRepository
import com.example.ui.components.CompactTopBarIconButton
import com.example.ui.components.FoulCompactTopBar
import com.example.ui.components.OrderedColorfulDivider
import com.example.ui.components.tvFocusableClickable
import com.example.ui.theme.FoulBlackBg
import com.example.ui.theme.FoulCardDark
import com.example.ui.theme.FoulCardElevated
import com.example.ui.theme.FoulDivider
import com.example.ui.theme.FoulGoldStar
import com.example.ui.theme.FoulLiveGreen
import com.example.ui.theme.FoulLiveRed
import com.example.ui.theme.FoulPurpleBright
import com.example.ui.theme.FoulPurpleDeep
import com.example.ui.theme.FoulPurplePrimary
import com.example.ui.theme.FoulSurfaceDark
import com.example.ui.theme.FoulTeamAwayCyan
import com.example.ui.theme.FoulTextSecondary

private enum class SettingsSubPage {
    NONE,
    PRIVACY_POLICY,
    APP_DESCRIPTION
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    telegramUrl: String = "https://t.me/jdj_q",
    currentUser: SocialUserProfile? = null,
    isDevLoginSheetOpen: Boolean = false,
    isLoggingInDeveloper: Boolean = false,
    devLoginError: String? = null,
    onOpenContactUsScreen: () -> Unit = {},
    onOpenDeveloperSheet: () -> Unit = {},
    onDismissDeveloperSheet: () -> Unit = {},
    onSubmitDeveloperLogin: (String, String) -> Unit = { _, _ -> }
) {
    val context = LocalContext.current
    val targetUrl = telegramUrl.ifBlank { "https://t.me/jdj_q" }
    var activeSubPage by remember { mutableStateOf(SettingsSubPage.NONE) }

    // Independent Full-Screen Page 1: Privacy Policy ("قسم سياسات الخصوصية للتطبيق وتفتح مستقلة للمشاهدة وبشكل احترافي جدا جدا ومرتب")
    if (activeSubPage == SettingsSubPage.PRIVACY_POLICY) {
        PrivacyPolicyIndependentScreen(
            onBack = { activeSubPage = SettingsSubPage.NONE }
        )
        return
    }

    // Independent Full-Screen Page 2: App Description ("خيار وصف عن التطبيق وبشكل أقسام وتفتح مستقلة للمشاهدة وبشكل احترافي جدا جدا ومرتب")
    if (activeSubPage == SettingsSubPage.APP_DESCRIPTION) {
        AppDescriptionIndependentScreen(
            onBack = { activeSubPage = SettingsSubPage.NONE }
        )
        return
    }

    // Half-Screen Developer Login Modal Bottom Sheet
    if (isDevLoginSheetOpen) {
        DeveloperLoginBottomSheet(
            isLoading = isLoggingInDeveloper,
            errorMessage = devLoginError,
            onDismiss = onDismissDeveloperSheet,
            onLogin = onSubmitDeveloperLogin
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FoulBlackBg)
    ) {
        // Sleek Compact Top Bar (Downloads icon permanently removed)
        FoulCompactTopBar(
            title = "الإعدادات",
            subtitle = "التواصل، القناة الرسمية، السياسات والمعلومات",
            leadingIcon = Icons.Default.Settings,
            actions = {
                CompactTopBarIconButton(
                    icon = Icons.Default.AdminPanelSettings,
                    contentDescription = "لوحة تحكم المطور",
                    onClick = onOpenDeveloperSheet,
                    modifier = Modifier.testTag("settings_top_dev_icon_button")
                )
            }
        )

        // Compact, Well-Organized Settings List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("settings_list"),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Section 1 Header: التواصل والقناة الرسمية
            item {
                SettingsGroupHeader(title = "الحساب والقناة الرسمية")
            }

            // 1. Contact Us ("تواصل معنا") Compact Card
            item {
                Card(
                    shape = RoundedCornerShape(13.dp),
                    colors = CardDefaults.cardColors(containerColor = FoulCardDark),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(13.dp))
                        .tvFocusableClickable(
                            shape = RoundedCornerShape(13.dp),
                            scaleOnFocus = 1.02f,
                            onClick = onOpenContactUsScreen
                        )
                        .border(
                            width = 1.dp,
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFF00E5FF).copy(alpha = 0.7f), FoulPurpleBright, FoulDivider)
                            ),
                            shape = RoundedCornerShape(13.dp)
                        )
                        .testTag("settings_contact_us_button")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(FoulPurpleDeep.copy(alpha = 0.75f), FoulCardDark)
                                )
                            )
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AnimatedBotIcon(size = 38.dp)

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = "تواصل معنا",
                                style = MaterialTheme.typography.titleSmall,
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold
                            )
                            if (currentUser != null) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    val avatarSpec = remember(currentUser.avatarId) {
                                        SocialFirebaseRepository.getAvatarById(currentUser.avatarId)
                                    }
                                    InAppCharacterAvatar(
                                        avatarSpec = avatarSpec,
                                        size = 18.dp
                                    )
                                    Text(
                                        text = "متصل: ${currentUser.username}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = FoulLiveGreen,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            } else {
                                Text(
                                    text = "تسجيل الدخول أو إنشاء حساب لمراسلة الدعم المباشر",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = FoulTextSecondary,
                                    maxLines = 1
                                )
                            }
                        }

                        CompactTrailingChevron(accentColor = FoulPurpleBright)
                    }
                }
            }

            // 2. Official Telegram Channel Compact Card
            item {
                Card(
                    shape = RoundedCornerShape(13.dp),
                    colors = CardDefaults.cardColors(containerColor = FoulCardDark),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(13.dp))
                        .tvFocusableClickable(
                            shape = RoundedCornerShape(13.dp),
                            scaleOnFocus = 1.02f,
                            onClick = {
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(targetUrl))
                                    context.startActivity(intent)
                                } catch (_: Exception) {
                                }
                            }
                        )
                        .border(1.dp, FoulDivider, RoundedCornerShape(13.dp))
                        .testTag("settings_telegram_button")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(FoulPurpleDeep.copy(alpha = 0.55f), FoulCardDark)
                                )
                            )
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TelegramVectorBadge(sizeDp = 38)

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = "قناة التيلجرام الرسمية",
                                style = MaterialTheme.typography.titleSmall,
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "تابع آخر التحديثات والقنوات المباشرة (${targetUrl.removePrefix("https://").removePrefix("http://")})",
                                style = MaterialTheme.typography.labelSmall,
                                color = FoulTextSecondary,
                                maxLines = 1
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(FoulCardElevated)
                                .border(1.dp, FoulDivider, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                contentDescription = "فتح قناة التيلجرام",
                                tint = Color.White,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }
                }
            }

            // Section 2 Header: عن التطبيق والسياسات
            item {
                Spacer(modifier = Modifier.height(4.dp))
                SettingsGroupHeader(title = "معلومات التطبيق والخصوصية")
            }

            // 4. App Description ("خيار وصف عن التطبيق وبشكل أقسام وتفتح مستقلة للمشاهدة")
            item {
                CompactSettingsOptionCard(
                    title = "وصف عن التطبيق",
                    subtitle = "تعرّف على مميزات وأقسام تطبيق Foul TV الكاملة",
                    icon = Icons.Default.Description,
                    iconBgColors = listOf(FoulTeamAwayCyan.copy(alpha = 0.85f), FoulPurpleDeep),
                    badgeText = "معلومات",
                    badgeColor = FoulTeamAwayCyan,
                    onClick = { activeSubPage = SettingsSubPage.APP_DESCRIPTION },
                    testTag = "settings_app_description_button"
                )
            }

            // 5. Privacy Policy ("قسم سياسات الخصوصية للتطبيق وتفتح مستقلة للمشاهدة")
            item {
                CompactSettingsOptionCard(
                    title = "سياسة الخصوصية",
                    subtitle = "معايير الأمان، حماية البيانات وحقوق المستخدم",
                    icon = Icons.Default.PrivacyTip,
                    iconBgColors = listOf(FoulLiveGreen.copy(alpha = 0.8f), FoulPurpleDeep),
                    badgeText = "آمن",
                    badgeColor = FoulLiveGreen,
                    onClick = { activeSubPage = SettingsSubPage.PRIVACY_POLICY },
                    testTag = "settings_privacy_policy_button"
                )
            }

            // Section 3 Header: الإدارة والنظام
            item {
                Spacer(modifier = Modifier.height(4.dp))
                SettingsGroupHeader(title = "إدارة النظام والمطور")
            }

            // 6. Developer Control Panel Compact Card
            item {
                CompactSettingsOptionCard(
                    title = "لوحة تحكم المطور",
                    subtitle = "إدارة القنوات، الأفرع، الرسائل والتحكم المباشر بالتطبيق",
                    icon = Icons.Default.AdminPanelSettings,
                    iconBgColors = listOf(FoulPurpleBright, FoulPurpleDeep),
                    badgeText = "إصدار ${AppVersionConfig.APP_CURRENT_VERSION}",
                    badgeColor = FoulPurplePrimary,
                    onClick = onOpenDeveloperSheet,
                    testTag = "settings_developer_panel_button"
                )
            }
        }
    }
}

@Composable
private fun SettingsGroupHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelMedium,
        color = Color.White.copy(alpha = 0.78f),
        fontWeight = FontWeight.ExtraBold,
        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
    )
}

@Composable
private fun CompactSettingsOptionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconBgColors: List<Color>,
    badgeText: String? = null,
    badgeColor: Color = FoulPurpleDeep,
    onClick: () -> Unit,
    testTag: String
) {
    Card(
        shape = RoundedCornerShape(13.dp),
        colors = CardDefaults.cardColors(containerColor = FoulCardDark),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(13.dp))
            .tvFocusableClickable(
                shape = RoundedCornerShape(13.dp),
                scaleOnFocus = 1.02f,
                onClick = onClick
            )
            .border(1.dp, FoulDivider, RoundedCornerShape(13.dp))
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(FoulPurpleDeep.copy(alpha = 0.45f), FoulCardDark)
                    )
                )
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Brush.linearGradient(colors = iconBgColors))
                    .border(1.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = FoulTextSecondary,
                    maxLines = 1
                )
            }

            if (!badgeText.isNullOrBlank()) {
                Surface(
                    color = badgeColor.copy(alpha = 0.22f),
                    shape = RoundedCornerShape(7.dp),
                    border = BorderStroke(1.dp, badgeColor.copy(alpha = 0.55f))
                ) {
                    Text(
                        text = badgeText,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                    )
                }
            }

            CompactTrailingChevron(accentColor = FoulCardElevated)
        }
    }
}

@Composable
private fun CompactTrailingChevron(accentColor: Color) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(accentColor)
            .border(1.dp, Color.White.copy(alpha = 0.18f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(16.dp)
        )
    }
}

/**
 * Independent Full-Screen Page: App Description ("خيار وصف عن التطبيق وبشكل أقسام وتفتح مستقلة للمشاهدة وبشكل احترافي جدا جدا ومرتب")
 */
@Composable
fun AppDescriptionIndependentScreen(
    onBack: () -> Unit
) {
    BackHandler(onBack = onBack)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FoulBlackBg)
            .testTag("app_description_screen")
    ) {
        // Top Header with Back Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(FoulSurfaceDark)
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = FoulPurpleDeep.copy(alpha = 0.65f),
                border = BorderStroke(1.dp, FoulPurpleBright.copy(alpha = 0.45f)),
                modifier = Modifier.size(38.dp)
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("app_description_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "رجوع",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Column {
                Text(
                    text = "وصف عن التطبيق",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "تعرّف على منظومة Foul TV المتكاملة",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.72f)
                )
            }
        }

        OrderedColorfulDivider(thickness = 1.5.dp)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // App Identity Hero Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = FoulCardDark),
                    border = BorderStroke(1.dp, FoulPurpleBright.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    listOf(FoulPurpleDeep.copy(alpha = 0.7f), FoulCardDark)
                                )
                            )
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(FoulPurpleBright)
                                    .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LiveTv,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "تطبيق Foul TV الرسمي",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Text(
                                    text = "الإصدار ${AppVersionConfig.APP_CURRENT_VERSION} • منصة البث والترفيه والرياضة",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = FoulGoldStar
                                )
                            }
                        }

                        Text(
                            text = "يُعد تطبيق Foul TV منصة عربية شاملة ومتطورة تجمع بين البث التلفزيوني المباشر عالي الدقة، مكتبة الأفلام والمسلسلات الضخمة، وتغطية حية لجدول المباريات وإحصائيات اللاعبين والأندية.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White,
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            item {
                InfoSectionCard(
                    icon = Icons.Default.LiveTv,
                    accentColor = FoulPurpleBright,
                    title = "1. قسم القنوات المباشرة (الرئيسية)",
                    bulletPoints = listOf(
                        "تنظيم احترافي للأفرع والأقسام الفرعية مع عرض القنوات بشبكة منظمة 3×3.",
                        "دعم تعدد السيرفرات والجودات (m3u8 / ts) مع التبديل الفوري داخل المشغل.",
                        "عدادات تفاعلية للمشاهدات والإعجابات لكل قناة بشكل مباشر."
                    )
                )
            }

            item {
                InfoSectionCard(
                    icon = Icons.Default.Movie,
                    accentColor = FoulTeamAwayCyan,
                    title = "2. قسم المكتبة (الأفلام والمسلسلات)",
                    bulletPoints = listOf(
                        "مكتبة متجددة من الأفلام والمسلسلات مع محرك بحث شامل فائق السرعة.",
                        "عرض المواسم والحلقات والتفاصيل الكاملة مع تشغيل فوري وسلس."
                    )
                )
            }

            item {
                InfoSectionCard(
                    icon = Icons.Default.SportsSoccer,
                    accentColor = FoulLiveGreen,
                    title = "3. قسم جدول المباريات واللاعبين والأندية",
                    bulletPoints = listOf(
                        "متابعة نتائج ومواعيد مباريات أمس واليوم وغداً مع تقويم تفاعلي شامل.",
                        "عرض أحداث المباراة، التشكيلة الرسمية على الملعب، نسب الاستحواذ، جدول الترتيب والأخبار.",
                        "إمكانية الضغط على أي لاعب في التشكيلة أو البحث عن أي لاعب أو فريق لعرض مسيرته في الدوريات، إحصائياته الكاملة، وسجل انتقالاته."
                    )
                )
            }
        }
    }
}

/**
 * Independent Full-Screen Page: Privacy Policy ("قسم سياسات الخصوصية للتطبيق وتفتح مستقلة للمشاهدة وبشكل احترافي جدا جدا ومرتب")
 */
@Composable
fun PrivacyPolicyIndependentScreen(
    onBack: () -> Unit
) {
    BackHandler(onBack = onBack)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FoulBlackBg)
            .testTag("privacy_policy_screen")
    ) {
        // Top Header with Back Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(FoulSurfaceDark)
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = FoulPurpleDeep.copy(alpha = 0.65f),
                border = BorderStroke(1.dp, FoulPurpleBright.copy(alpha = 0.45f)),
                modifier = Modifier.size(38.dp)
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("privacy_policy_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "رجوع",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Column {
                Text(
                    text = "سياسة الخصوصية",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "حماية البيانات ومعايير الأمان والخصوصية في Foul TV",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.72f)
                )
            }
        }

        OrderedColorfulDivider(thickness = 1.5.dp)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                InfoSectionCard(
                    icon = Icons.Default.Security,
                    accentColor = FoulLiveGreen,
                    title = "1. التزامنا بحماية خصوصيتك",
                    bulletPoints = listOf(
                        "يحرص تطبيق Foul TV على حماية خصوصية جميع المستخدمين وتأمين بياناتهم وفق أعلى معايير التشفير.",
                        "لا يطلب التطبيق أي أذونات حساسة غير ضرورية لتشغيل البث أو التحميل."
                    )
                )
            }

            item {
                InfoSectionCard(
                    icon = Icons.Default.VerifiedUser,
                    accentColor = FoulTeamAwayCyan,
                    title = "2. البيانات التي يتم جمعها واستخدامها",
                    bulletPoints = listOf(
                        "عند إنشاء حساب في قسم (تواصل معنا)، يتم حفظ البريد الإلكتروني واسم المستخدم والصورة الرمزية المختارة فقط لتمكينك من مراسلة الدعم الفني واستلام الردود.",
                        "يتم تسجيل عدادات المشاهدة والإعجابات بشكل إحصائي لتحسين جودة القنوات والسيرفرات."
                    )
                )
            }

            item {
                InfoSectionCard(
                    icon = Icons.Default.GppGood,
                    accentColor = FoulPurpleBright,
                    title = "3. أمان الاتصالات وعدم مشاركة البيانات",
                    bulletPoints = listOf(
                        "لا نقوم ببيع أو مشاركة أي بيانات خاصة بالمستخدمين مع أي أطراف خارجية.",
                        "جميع الاتصالات بقواعد البيانات تتم عبر قنوات مشفرة وآمنة (HTTPS)."
                    )
                )
            }
        }
    }
}

@Composable
private fun InfoSectionCard(
    icon: ImageVector,
    accentColor: Color,
    title: String,
    bulletPoints: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = FoulCardDark),
        border = BorderStroke(1.dp, FoulDivider)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = accentColor.copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, accentColor.copy(alpha = 0.55f)),
                    modifier = Modifier.size(30.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            bulletPoints.forEach { point ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier
                            .padding(top = 3.dp)
                            .size(14.dp)
                    )
                    Text(
                        text = point,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.9f),
                        lineHeight = 19.sp
                    )
                }
            }
        }
    }
}

/**
 * Reusable Professional Telegram Vector Emblem Badge
 */
@Composable
fun TelegramVectorBadge(sizeDp: Int = 50) {
    Box(
        modifier = Modifier
            .size(sizeDp.dp)
            .clip(CircleShape)
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF2AABEE), Color(0xFF1E88E5))
                )
            )
            .border(1.5.dp, Color.White.copy(alpha = 0.28f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size((sizeDp * 0.52f).dp)) {
            val w = size.width
            val h = size.height
            val path = Path().apply {
                moveTo(w * 0.92f, h * 0.14f)
                lineTo(w * 0.08f, h * 0.47f)
                lineTo(w * 0.34f, h * 0.58f)
                lineTo(w * 0.76f, h * 0.28f)
                lineTo(w * 0.45f, h * 0.64f)
                lineTo(w * 0.45f, h * 0.86f)
                lineTo(w * 0.58f, h * 0.72f)
                lineTo(w * 0.78f, h * 0.86f)
                close()
            }
            drawPath(path = path, color = Color.White)
        }
    }
}

/**
 * Half-Screen Modal Bottom Sheet for Developer Email & Password Verification
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeveloperLoginBottomSheet(
    isLoading: Boolean,
    errorMessage: String?,
    onDismiss: () -> Unit,
    onLogin: (String, String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = FoulCardDark,
        contentColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            OrderedColorfulDivider(thickness = 1.5.dp)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(FoulPurpleBright)
                        .border(1.dp, Color.White.copy(alpha = 0.25f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Column {
                    Text(
                        text = "تحقق هوية المطور",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "أدخل البريد الإلكتروني وكلمة المرور لفتح لوحة التحكم",
                        style = MaterialTheme.typography.bodySmall,
                        color = FoulTextSecondary
                    )
                }
            }

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("البريد الإلكتروني للمطور", color = FoulTextSecondary) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = Color.White
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = FoulCardElevated,
                    unfocusedContainerColor = FoulCardElevated,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = FoulDivider,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("dev_login_email_input")
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("كلمة المرور", color = FoulTextSecondary) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color.White
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "إظهار كلمة المرور",
                            tint = FoulTextSecondary
                        )
                    }
                },
                singleLine = true,
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = FoulCardElevated,
                    unfocusedContainerColor = FoulCardElevated,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = FoulDivider,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("dev_login_password_input")
            )

            if (!errorMessage.isNullOrBlank()) {
                Surface(
                    color = FoulLiveRed.copy(alpha = 0.18f),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, FoulLiveRed, RoundedCornerShape(10.dp))
                ) {
                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }

            Button(
                onClick = { onLogin(email, password) },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = FoulPurpleBright),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .border(1.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                    .testTag("dev_login_submit_button")
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("جاري التحقق...", color = Color.White, fontWeight = FontWeight.Bold)
                } else {
                    Text(
                        text = "تسجيل الدخول وفتح لوحة التحكم",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
