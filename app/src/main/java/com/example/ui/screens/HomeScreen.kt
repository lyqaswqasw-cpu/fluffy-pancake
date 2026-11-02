package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.ChannelCategory
import com.example.data.model.ChannelEngagementStats
import com.example.data.model.EventBanner
import com.example.data.model.LiveChannel
import com.example.data.model.PublishedEventItem
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.ui.components.CompactTopBarIconButton
import com.example.ui.components.FoulCompactTopBar
import com.example.ui.components.TelegramTopBarIconButton
import com.example.ui.components.tvFocusableClickable
import com.example.ui.theme.FoulBlackBg
import com.example.ui.theme.FoulCardDark
import com.example.ui.theme.FoulCardElevated
import com.example.ui.theme.FoulDivider
import com.example.ui.theme.FoulLiveRed
import com.example.ui.theme.FoulPurpleBright
import com.example.ui.theme.FoulPurpleDeep
import com.example.ui.theme.FoulTextSecondary

/**
 * Home Screen:
 * - Main Branches and Inner Sub-Branches appear in an elegant vertical list strip ("بشكل شريط عامودي وبحجم جيد وانيق وإزالة نص فوقه فرع")
 * - Clicking a Branch or Inner Sub-Branch displays its Channels in a 3x3 Grid ("وعند الضغط تضهر القنوات بشكل شبكة 3*3 حتى الفرع الداخلي أيضا كذلك")
 * - Each channel in the 3x3 Grid displays its separate Views Counter and Likes Counter + Like button ("لكل قناة في قسم الرئيسية يضهر كم شخص شاهد القناة وأيضا عداد آخر كم شخص ضغط لايك على القناة لكل قناة بشكل منفصل")
 */
@Composable
fun HomeScreen(
    isLoading: Boolean,
    error: String?,
    currentBranch: ChannelCategory?,
    visibleBranches: List<ChannelCategory>,
    visibleChannels: List<LiveChannel>,
    allChannels: List<LiveChannel>,
    allBranches: List<ChannelCategory>,
    banners: List<EventBanner>,
    publishedEvents: List<PublishedEventItem> = emptyList(),
    channelStatsMap: Map<String, ChannelEngagementStats> = emptyMap(),
    showChannelViewsAndLikes: Boolean = true,
    telegramUrl: String = "https://t.me/jdj_q",
    onOpenBranch: (ChannelCategory) -> Unit,
    onBackFromBranch: () -> Unit,
    onChannelClick: (LiveChannel) -> Unit,
    onPlayPublishedEventVideo: (PublishedEventItem) -> Unit = {},
    onToggleChannelLike: (LiveChannel) -> Unit = {},
    onOpenContactUs: () -> Unit = {},
    onBannerClick: (EventBanner) -> Unit,
    onRefresh: () -> Unit
) {
    // Support hardware/gesture back when inside a branch or inner sub-branch
    BackHandler(enabled = currentBranch != null) {
        onBackFromBranch()
    }

    var pickerTitle by remember { mutableStateOf("") }
    var pickerSubtitle by remember { mutableStateOf("") }
    var pickerChannels by remember { mutableStateOf<List<LiveChannel>>(emptyList()) }

    if (pickerChannels.isNotEmpty()) {
        MatchAndEventChannelPickerDialog(
            title = pickerTitle,
            subtitle = pickerSubtitle,
            channels = pickerChannels,
            onSelectChannel = { ch ->
                pickerChannels = emptyList()
                onChannelClick(ch)
            },
            onDismiss = { pickerChannels = emptyList() }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FoulBlackBg)
    ) {
        // Sleek Compact Top Bar (with Telegram Icon, Back Arrow when inside a Branch, and Refresh)
        FoulCompactTopBar(
            title = currentBranch?.name ?: "Foul TV",
            subtitle = if (currentBranch != null) "القنوات المباشرة" else "البث المباشر والقنوات",
            leadingIcon = if (currentBranch == null) Icons.Default.LiveTv else null,
            actions = {
                TelegramTopBarIconButton(
                    telegramUrl = telegramUrl,
                    modifier = Modifier.testTag("home_telegram_button")
                )
                if (currentBranch != null) {
                    CompactTopBarIconButton(
                        icon = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "الرجوع للخلف",
                        onClick = onBackFromBranch,
                        modifier = Modifier.testTag("home_branch_back_button")
                    )
                }
                CompactTopBarIconButton(
                    icon = Icons.Default.Refresh,
                    contentDescription = "تحديث",
                    onClick = onRefresh,
                    modifier = Modifier.testTag("home_refresh_button")
                )
            }
        )

        // Content Body
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CircularProgressIndicator(color = Color.White, strokeWidth = 2.5.dp)
                        Text(
                            text = "جاري تحميل القنوات المباشرة...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = FoulTextSecondary
                        )
                    }
                }
            }

            error != null && visibleBranches.isEmpty() && visibleChannels.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = error,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Button(
                            onClick = onRefresh,
                            colors = ButtonDefaults.buttonColors(containerColor = FoulPurpleBright)
                        ) {
                            Text("إعادة المحاولة", color = Color.White)
                        }
                    }
                }
            }

            visibleBranches.isEmpty() && visibleChannels.isEmpty() && publishedEvents.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = if (currentBranch == null) {
                                "لا توجد باقات منشورة حالياً"
                            } else {
                                "لا توجد قنوات داخل (${currentBranch.name}) حالياً"
                            },
                            style = MaterialTheme.typography.titleMedium,
                            color = FoulTextSecondary,
                            textAlign = TextAlign.Center
                        )
                        if (currentBranch != null) {
                            Button(
                                onClick = onBackFromBranch,
                                colors = ButtonDefaults.buttonColors(containerColor = FoulCardElevated)
                            ) {
                                Text("العودة للخلف", color = Color.White)
                            }
                        }
                    }
                }
            }

            else -> {
                BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                    val gridColumns = when {
                        maxWidth >= 880.dp -> 5
                        maxWidth >= 640.dp -> 4
                        else -> 3
                    }
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(gridColumns), // 3x3 Grid on phones, 4-5 columns on TV / PC monitors
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("home_grid_3x3")
                    ) {
                    // Unified Single-Slot Auto-Advancing Events Carousel at Root ("تكون الأحداث بشكل في مكان واحد وبشكل تنقل كل ثلاث ثواني وبشكل مرتب جدا")
                    if (currentBranch == null && (publishedEvents.isNotEmpty() || banners.isNotEmpty())) {
                        item(
                            key = "home_unified_events_carousel",
                            contentType = "published_events_carousel",
                            span = { GridItemSpan(maxLineSpan) }
                        ) {
                            val unifiedEvents = remember(publishedEvents, banners) {
                                val legacyAsEvents = banners
                                    .filter { b -> publishedEvents.none { it.id == b.id } }
                                    .map { b ->
                                        PublishedEventItem(
                                            id = b.id,
                                            title = b.title,
                                            description = b.channelName,
                                            imageUrl = b.imageUrl,
                                            eventType = "CHANNELS",
                                            channelIds = if (b.channelIds.isNotEmpty()) b.channelIds else listOfNotNull(b.channelId.takeIf { it.isNotBlank() }),
                                            showWatchNow = b.showWatchNow
                                        )
                                    }
                                publishedEvents + legacyAsEvents
                            }

                            PublishedEventsAutoPagerCarousel(
                                events = unifiedEvents,
                                allChannels = allChannels,
                                onWatchEvent = { eventItem, resolvedEventChannels ->
                                    if (eventItem.eventType == "VIDEO" && eventItem.videoUrl.isNotBlank()) {
                                        onPlayPublishedEventVideo(eventItem)
                                    } else if (resolvedEventChannels.size == 1) {
                                        onChannelClick(resolvedEventChannels.first())
                                    } else if (resolvedEventChannels.size > 1) {
                                        pickerTitle = eventItem.title.ifBlank { "قنوات مشاهدة الحدث" }
                                        pickerSubtitle = eventItem.description.ifBlank { "اختر القناة لبدء المشاهدة المباشرة" }
                                        pickerChannels = resolvedEventChannels
                                    } else {
                                        val matchingBanner = banners.firstOrNull { it.id == eventItem.id }
                                        if (matchingBanner != null) {
                                            onBannerClick(matchingBanner)
                                        }
                                    }
                                }
                            )
                        }
                    }

                    // Active Branch Header Banner when inside a Branch
                    if (currentBranch != null) {
                        item(
                            key = "active_branch_header_${currentBranch.id}",
                            span = { GridItemSpan(maxLineSpan) }
                        ) {
                            ActiveBranchShowcaseHeader(
                                branch = currentBranch,
                                subBranchesCount = visibleBranches.size,
                                channelsCount = visibleChannels.size,
                                onBack = onBackFromBranch
                            )
                        }
                    } else if (visibleBranches.isNotEmpty()) {
                        // Root Section Header
                        item(
                            key = "root_branches_section_header",
                            span = { GridItemSpan(maxLineSpan) }
                        ) {
                            HomeSectionHeaderRow(
                                title = "الأقسام والباقات المباشرة",
                                badgeText = "${visibleBranches.size} قسم"
                            )
                        }
                    }

                    // 1. Branches & Inner Sub-Branches rendered as a sleek vertical list of full-width strips
                    // ("اجعل الأفرع بشكل تظهر في قسم الرئيسية بشكل أفرع ليس شبكة 3*3 بشكل تكون بشكل شريط عامودي وبحجم جيد وانيق وايضا ترتيب جميل جدا جدا وإزالة نص فوقه فرع")
                    items(
                        items = visibleBranches,
                        key = { "branch_${it.id}" },
                        contentType = { "branch" },
                        span = { GridItemSpan(maxLineSpan) }
                    ) { branch ->
                        val childChannelsCount = allChannels.count { ch ->
                            ch.branchId == branch.id ||
                                (ch.branchId.isBlank() && ch.category.equals(branch.name, ignoreCase = true))
                        }
                        val subBranchesCount = allBranches.count { it.parentId == branch.id }
                        // Fallback image from first channel inside branch if branch imageUrl is empty
                        val fallbackImg = branch.imageUrl.ifBlank {
                            allChannels.firstOrNull { ch ->
                                ch.branchId == branch.id ||
                                    ch.category.equals(branch.name, ignoreCase = true)
                            }?.imageUrl.orEmpty()
                        }

                        BranchVerticalStripCard(
                            branch = branch,
                            displayImageUrl = fallbackImg,
                            channelCount = childChannelsCount,
                            subBranchesCount = subBranchesCount,
                            onClick = { onOpenBranch(branch) }
                        )
                    }

                    // 2. Channels inside the selected Branch (or Inner Sub-Branch) in a strict 3x3 Grid
                    // ("وعند الضغط تضهر القنوات بشكل شبكة 3*3 حتى الفرع الداخلي أيضا كذلك")
                    if (currentBranch != null && visibleChannels.isNotEmpty()) {
                        if (visibleBranches.isNotEmpty()) {
                            item(
                                key = "channels_grid_section_header_${currentBranch.id}",
                                span = { GridItemSpan(maxLineSpan) }
                            ) {
                                HomeSectionHeaderRow(
                                    title = "القنوات المباشرة",
                                    badgeText = "${visibleChannels.size} قناة"
                                )
                            }
                        }

                        items(
                            items = visibleChannels,
                            key = { "channel_${it.id}" },
                            contentType = { "channel" }
                        ) { channel ->
                            val stats = channelStatsMap[channel.id]
                            ChannelGrid3x3Card(
                                channel = channel,
                                stats = stats,
                                showViewsAndLikes = showChannelViewsAndLikes,
                                onClick = { onChannelClick(channel) },
                                onLikeClick = { onToggleChannelLike(channel) }
                            )
                        }
                    }
                }
                }
            }
        }
    }
}

@Composable
private fun HomeSectionHeaderRow(
    title: String,
    badgeText: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(18.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF38BDF8), FoulPurpleBright)
                        )
                    )
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Surface(
            shape = RoundedCornerShape(8.dp),
            color = FoulPurpleDeep,
            modifier = Modifier.border(1.dp, FoulDivider, RoundedCornerShape(8.dp))
        ) {
            Text(
                text = badgeText,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun ActiveBranchShowcaseHeader(
    branch: ChannelCategory,
    subBranchesCount: Int,
    channelsCount: Int,
    onBack: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = FoulCardDark),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    colors = listOf(FoulPurpleBright, Color.White.copy(alpha = 0.28f), FoulDivider)
                ),
                shape = RoundedCornerShape(18.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(FoulPurpleDeep, FoulCardDark)
                    )
                )
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Studio-framed Branch Image
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            Brush.radialGradient(
                                colors = listOf(FoulPurpleBright, FoulPurpleDeep)
                            )
                        )
                        .border(1.dp, Color.White.copy(alpha = 0.35f), RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (branch.imageUrl.isNotBlank()) {
                        AsyncImage(
                            model = branch.imageUrl,
                            contentDescription = branch.name,
                            contentScale = ContentScale.Crop,
                            alpha = 0.25f,
                            modifier = Modifier.fillMaxSize()
                        )
                        AsyncImage(
                            model = branch.imageUrl,
                            contentDescription = branch.name,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(5.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.LiveTv,
                            contentDescription = branch.name,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = branch.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(3.dp))
                    val statsSummary = buildList {
                        if (channelsCount > 0) add("$channelsCount قناة مباشرة")
                        if (subBranchesCount > 0) add("$subBranchesCount قسم فرعي")
                    }.joinToString(" • ").ifBlank { "القنوات المباشرة" }
                    Text(
                        text = statsSummary,
                        style = MaterialTheme.typography.labelSmall,
                        color = FoulTextSecondary
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = FoulCardElevated,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .tvFocusableClickable(
                        shape = RoundedCornerShape(10.dp),
                        scaleOnFocus = 1.05f,
                        onClick = onBack
                    )
                    .border(1.dp, FoulDivider, RoundedCornerShape(10.dp))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "رجوع",
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = "رجوع",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * Sleek, well-sized vertical strip card for a Branch or Inner Sub-Branch.
 * Features a dual-layer crystal image showcase so any branch image (square logo or wide banner) renders crisply and elegantly.
 * No "فرع" badge/label above the name ("وإزالة نص فوقه فرع").
 */
@Composable
private fun BranchVerticalStripCard(
    branch: ChannelCategory,
    displayImageUrl: String,
    channelCount: Int,
    subBranchesCount: Int = 0,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = FoulCardDark),
        modifier = Modifier
            .fillMaxWidth()
            .height(86.dp)
            .clip(RoundedCornerShape(18.dp))
            .tvFocusableClickable(
                shape = RoundedCornerShape(18.dp),
                scaleOnFocus = 1.015f,
                onClick = onClick
            )
            .border(
                width = 1.2.dp,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.26f),
                        FoulPurpleBright.copy(alpha = 0.55f),
                        FoulDivider
                    )
                ),
                shape = RoundedCornerShape(18.dp)
            )
            .testTag("branch_card_${branch.id}")
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            FoulPurpleDeep.copy(alpha = 0.80f),
                            FoulCardDark,
                            Color(0xFF090812)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            // Subtle ambient side backdrop from the branch image for extra visual depth
            if (displayImageUrl.isNotBlank()) {
                AsyncImage(
                    model = displayImageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    alpha = 0.08f,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    // Dual-Layer Crystal Emblem Frame for Branch Image ("ضهور الصور بشكل أنيق جدا وبترتيب جميل للغاية")
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        FoulPurpleBright.copy(alpha = 0.85f),
                                        FoulPurpleDeep,
                                        Color(0xFF0A0614)
                                    )
                                )
                            )
                            .border(
                                width = 1.2.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.45f),
                                        Color(0xFF38BDF8).copy(alpha = 0.40f),
                                        FoulPurpleBright
                                    )
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (displayImageUrl.isNotBlank()) {
                            // Soft cropped background fill so non-square images never look empty
                            AsyncImage(
                                model = displayImageUrl,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                alpha = 0.24f,
                                modifier = Modifier.fillMaxSize()
                            )
                            // Crisp full logo/image in the foreground
                            AsyncImage(
                                model = displayImageUrl,
                                contentDescription = branch.name,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(6.dp)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.LiveTv,
                                contentDescription = branch.name,
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    Spacer(Modifier.width(14.dp))

                    // Clean Branch Title & Metadata Pills (Without any "فرع" text above it!)
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = branch.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(Modifier.height(6.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (channelCount > 0) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = FoulCardElevated.copy(alpha = 0.9f),
                                    modifier = Modifier.border(0.8.dp, FoulDivider, RoundedCornerShape(6.dp))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(6.dp)
                                                .clip(CircleShape)
                                                .background(FoulLiveRed)
                                        )
                                        Text(
                                            text = "$channelCount قناة مباشرة",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            if (subBranchesCount > 0) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = FoulPurpleDeep.copy(alpha = 0.9f),
                                    modifier = Modifier.border(0.8.dp, FoulPurpleBright.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                                ) {
                                    Text(
                                        text = "$subBranchesCount باقة داخلية",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            if (channelCount == 0 && subBranchesCount == 0) {
                                Text(
                                    text = "اضغط لاستعراض القنوات",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = FoulTextSecondary
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.width(10.dp))

                // Sleek Action Arrow Pill
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(FoulPurpleBright, FoulPurpleDeep)
                            )
                        )
                        .border(1.dp, Color.White.copy(alpha = 0.32f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "فتح",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ChannelGrid3x3Card(
    channel: LiveChannel,
    stats: ChannelEngagementStats?,
    showViewsAndLikes: Boolean = true,
    onClick: () -> Unit,
    onLikeClick: () -> Unit
) {
    val viewsCount = (stats?.views ?: channel.views).toLong()
    val likesCount = (stats?.likes ?: 0).toLong()
    val isLiked = stats?.isLikedByMe == true

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = FoulCardDark),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .tvFocusableClickable(
                shape = RoundedCornerShape(16.dp),
                scaleOnFocus = 1.04f,
                onClick = onClick
            )
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.22f),
                        FoulDivider,
                        FoulPurpleBright.copy(alpha = 0.38f)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .testTag("channel_card_${channel.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(FoulCardDark, Color(0xFF080710))
                    )
                )
                .padding(7.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Dual-Layer Studio Image Box so Channel Logos & Thumbnails Pop Vivaciously
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.06f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                FoulPurpleBright.copy(alpha = 0.42f),
                                FoulPurpleDeep.copy(alpha = 0.78f),
                                FoulCardElevated
                            )
                        )
                    )
                    .border(
                        width = 0.8.dp,
                        color = Color.White.copy(alpha = 0.14f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (channel.imageUrl.isNotBlank()) {
                    // Ambient soft backdrop
                    AsyncImage(
                        model = channel.imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        alpha = 0.18f,
                        modifier = Modifier.fillMaxSize()
                    )
                    // Crisp foreground channel logo/image
                    AsyncImage(
                        model = channel.imageUrl,
                        contentDescription = channel.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(9.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.LiveTv,
                        contentDescription = channel.name,
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                // Live indicator badge (Top-End)
                Surface(
                    color = FoulLiveRed.copy(alpha = 0.92f),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(5.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Sensors,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(9.dp)
                        )
                        Text(
                            text = "LIVE",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                // Multi-Server count badge (Top-Start) if channel has multiple servers
                if (channel.servers.size > 1) {
                    Surface(
                        color = FoulBlackBg.copy(alpha = 0.75f),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(5.dp)
                            .border(0.6.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(6.dp))
                    ) {
                        Text(
                            text = "${channel.servers.size} سيرفر",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.5.sp),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.5.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(7.dp))

            Text(
                text = channel.name,
                style = MaterialTheme.typography.labelLarge,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )

            // Per-channel separate Views Counter & Interactive Like Button + Counter (Controlled online via Developer App Control)
            if (showViewsAndLikes) {
                Spacer(Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Views Counter Badge
                    Surface(
                        color = FoulCardElevated,
                        shape = RoundedCornerShape(7.dp),
                        modifier = Modifier
                            .weight(1f)
                            .border(0.6.dp, FoulDivider, RoundedCornerShape(7.dp))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 3.5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = "عدد المشاهدات",
                                tint = Color(0xFF38BDF8),
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(Modifier.width(3.dp))
                            Text(
                                text = formatCompactCount(viewsCount),
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                        }
                    }

                    Spacer(Modifier.width(4.dp))

                    // Interactive Like Button + Counter Badge
                    Surface(
                        color = if (isLiked) FoulLiveRed.copy(alpha = 0.25f) else FoulCardElevated,
                        shape = RoundedCornerShape(7.dp),
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(7.dp))
                            .tvFocusableClickable(
                                shape = RoundedCornerShape(7.dp),
                                scaleOnFocus = 1.06f,
                                onClick = onLikeClick
                            )
                            .border(
                                width = 0.8.dp,
                                color = if (isLiked) FoulLiveRed else FoulDivider,
                                shape = RoundedCornerShape(7.dp)
                            )
                            .testTag("channel_like_${channel.id}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 3.5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "إعجاب بالقناة",
                                tint = if (isLiked) FoulLiveRed else Color.White,
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(Modifier.width(3.dp))
                            Text(
                                text = formatCompactCount(likesCount),
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}


private fun formatCompactCount(count: Long): String {
    return when {
        count >= 1_000_000L -> String.format(java.util.Locale.US, "%.1fM", count / 1_000_000.0)
        count >= 1_000L -> String.format(java.util.Locale.US, "%.1fK", count / 1_000.0)
        else -> count.toString()
    }
}

@Composable
private fun FeaturedBannerCard(
    banner: EventBanner,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = FoulCardElevated),
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .border(1.dp, Color.White.copy(alpha = 0.14f), RoundedCornerShape(14.dp))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = banner.imageUrl,
                contentDescription = banner.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                FoulBlackBg.copy(alpha = 0.94f)
                            )
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        color = FoulLiveRed,
                        shape = RoundedCornerShape(5.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(11.dp)
                            )
                            Text(
                                text = "مباشر",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White
                            )
                        }
                    }
                    if (banner.channelName.isNotEmpty()) {
                        Text(
                            text = banner.channelName,
                            style = MaterialTheme.typography.labelMedium,
                            color = FoulTextSecondary
                        )
                    }
                }
                Spacer(Modifier.height(3.dp))
                Text(
                    text = banner.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
