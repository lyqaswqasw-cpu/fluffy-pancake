package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.data.model.ChannelCategory
import com.example.data.model.LiveChannel
import com.example.data.model.XtreamAccount
import com.example.data.model.XtreamAccountLiveCatalog
import com.example.data.model.XtreamLiveStreamItem
import com.example.ui.components.OrderedColorfulDivider
import com.example.ui.theme.FoulBlackBg
import com.example.ui.theme.FoulCardDark
import com.example.ui.theme.FoulCardElevated
import com.example.ui.theme.FoulDivider
import com.example.ui.theme.FoulGoldStar
import com.example.ui.theme.FoulLiveGreen
import com.example.ui.theme.FoulLiveRed
import com.example.ui.theme.FoulPurpleBright
import com.example.ui.theme.FoulPurpleDeep
import com.example.ui.theme.FoulTeamAwayCyan
import com.example.ui.theme.FoulTextSecondary

/**
 * Developer Tab: "نشر فرع حساب" (Publish Account Branch)
 *
 * Allows the developer to:
 * 1. Enter Host, Username, and Password (or pick from saved Xtream accounts) and fetch the account's live categories & channels.
 * 2. Browse Account Categories ("تصنيفات الحساب"), fetch/inspect a specific category's content ("جلب ومحتوى تصنيف جلب واختيار").
 * 3. Select specific channels ("إمكانية إختيار قنوات محددة") or select all channels in a category.
 * 4. Reorder the selected channels ("وأيضا ترتيب القنوات") with Up / Down / Remove controls.
 * 5. Enter Branch Name ("اسم الفرع") and Branch Image URL ("رابط صورة الفرع"), choose main or sub-branch placement, and publish the branch with all selected channels to Firebase.
 */
@Composable
fun DeveloperPublishAccountBranchTab(
    branches: List<ChannelCategory>,
    channels: List<LiveChannel>,
    xtreamAccounts: List<XtreamAccount>,
    isActionLoading: Boolean,
    isFetchingCatalog: Boolean,
    isFetchingCategoryStreams: Boolean,
    fetchError: String?,
    fetchedCatalog: XtreamAccountLiveCatalog?,
    onFetchAccountCatalog: (host: String, username: String, password: String) -> Unit,
    onFetchCategoryStreams: (host: String, username: String, password: String, categoryId: String, categoryName: String) -> Unit,
    onClearCatalog: () -> Unit,
    onPublishAccountBranch: (
        existingBranchId: String?,
        branchName: String,
        branchImageUrl: String,
        parentBranchId: String,
        orderedSelectedStreams: List<XtreamLiveStreamItem>
    ) -> Unit
) {
    var hostInput by remember { mutableStateOf(fetchedCatalog?.host ?: xtreamAccounts.firstOrNull()?.host.orEmpty()) }
    var usernameInput by remember { mutableStateOf(fetchedCatalog?.username ?: xtreamAccounts.firstOrNull()?.username.orEmpty()) }
    var passwordInput by remember { mutableStateOf(fetchedCatalog?.password ?: xtreamAccounts.firstOrNull()?.password.orEmpty()) }

    var selectedCategoryId by remember { mutableStateOf("ALL") }
    var categorySearchQuery by remember { mutableStateOf("") }
    var channelSearchQuery by remember { mutableStateOf("") }

    // Ordered list of selected channels to be published into the branch
    val selectedOrderedStreams = remember { mutableStateListOf<XtreamLiveStreamItem>() }

    // Branch metadata for publishing
    var branchNameInput by remember { mutableStateOf("") }
    var branchImageUrlInput by remember { mutableStateOf("") }
    var selectedParentBranchId by remember { mutableStateOf("") }
    var targetExistingBranchId by remember { mutableStateOf<String?>(null) }

    // Auto-select first category when catalog arrives if current selection is invalid
    LaunchedEffect(fetchedCatalog?.categories) {
        val cats = fetchedCatalog?.categories.orEmpty()
        if (cats.isNotEmpty() && selectedCategoryId != "ALL" && cats.none { it.categoryId == selectedCategoryId }) {
            selectedCategoryId = cats.first().categoryId
        }
    }

    val filteredCategories = remember(fetchedCatalog?.categories, categorySearchQuery) {
        val cats = fetchedCatalog?.categories.orEmpty()
        val q = categorySearchQuery.trim()
        if (q.isEmpty()) cats else cats.filter { it.categoryName.contains(q, ignoreCase = true) }
    }

    val selectedCategoryObj = remember(fetchedCatalog?.categories, selectedCategoryId) {
        fetchedCatalog?.categories?.firstOrNull { it.categoryId == selectedCategoryId }
    }

    val displayedStreams = remember(fetchedCatalog?.streams, selectedCategoryId, channelSearchQuery) {
        val all = fetchedCatalog?.streams.orEmpty()
        val byCategory = if (selectedCategoryId == "ALL") {
            all
        } else {
            all.filter { it.categoryId == selectedCategoryId }
        }
        val q = channelSearchQuery.trim()
        if (q.isEmpty()) {
            byCategory
        } else {
            byCategory.filter {
                it.name.contains(q, ignoreCase = true) ||
                    it.categoryName.contains(q, ignoreCase = true)
            }
        }
    }

    val selectedStreamIdsSet = remember(selectedOrderedStreams.size, selectedOrderedStreams.toList()) {
        selectedOrderedStreams.map { it.streamId }.toSet()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("dev_publish_account_branch_tab"),
        contentPadding = PaddingValues(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // ==================== STEP 1: ACCOUNT CREDENTIALS & FETCH ====================
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = FoulCardDark),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        brush = Brush.horizontalGradient(
                            colors = listOf(FoulPurpleBright, FoulTeamAwayCyan)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountTree,
                                contentDescription = null,
                                tint = FoulPurpleBright,
                                modifier = Modifier.size(22.dp)
                            )
                            Column {
                                Text(
                                    text = "نشر فرع حساب (جلب تصنيفات وقنوات الحساب)",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Text(
                                    text = "أدخل الهوست واسم المستخدم وكلمة المرور لجلب تصنيفات الحساب واختيار القنوات وترتيبها ونشرها كفرع",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = FoulTextSecondary
                                )
                            }
                        }

                        if (fetchedCatalog != null) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = FoulCardElevated,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable {
                                        selectedOrderedStreams.clear()
                                        onClearCatalog()
                                    }
                            ) {
                                Text(
                                    text = "تفريغ الجلب",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    // Quick Fill from Saved Xtream Accounts if available
                    if (xtreamAccounts.isNotEmpty()) {
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = "تعبئة سريعة من حسابات Xtreme المحفوظة في التطبيق:",
                            style = MaterialTheme.typography.labelSmall,
                            color = FoulTeamAwayCyan,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(6.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(xtreamAccounts, key = { it.id }) { acc ->
                                val isCurrentAcc = hostInput.trim() == acc.host.trim() &&
                                    usernameInput.trim() == acc.username.trim()
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isCurrentAcc) FoulPurpleBright else FoulCardElevated,
                                    border = BorderStroke(
                                        1.dp,
                                        if (isCurrentAcc) Color.White else FoulDivider
                                    ),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable {
                                            hostInput = acc.host
                                            usernameInput = acc.username
                                            passwordInput = acc.password
                                        }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CloudQueue,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(15.dp)
                                        )
                                        Text(
                                            text = "${acc.name} (${acc.username})",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(10.dp))

                    AccountBranchTextField(
                        value = hostInput,
                        onValueChange = { hostInput = it },
                        label = "الهوست (Host / URL) مثال: http://example.com:8080",
                        testTag = "dev_acc_branch_host_input"
                    )

                    Spacer(Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            AccountBranchTextField(
                                value = usernameInput,
                                onValueChange = { usernameInput = it },
                                label = "اسم المستخدم (Username)",
                                testTag = "dev_acc_branch_username_input"
                            )
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            AccountBranchTextField(
                                value = passwordInput,
                                onValueChange = { passwordInput = it },
                                label = "كلمة المرور (Password)",
                                testTag = "dev_acc_branch_password_input"
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    Button(
                        onClick = {
                            onFetchAccountCatalog(hostInput, usernameInput, passwordInput)
                        },
                        enabled = !isFetchingCatalog && !isActionLoading,
                        colors = ButtonDefaults.buttonColors(containerColor = FoulPurpleBright),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("dev_acc_branch_fetch_button")
                    ) {
                        if (isFetchingCatalog) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.5.dp,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                text = "جاري جلب تصنيفات وقنوات الحساب...",
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.CloudDownload,
                                contentDescription = null,
                                tint = Color.White
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = if (fetchedCatalog == null) {
                                    "جلب تصنيفات وقنوات الحساب الآن"
                                } else {
                                    "إعادة جلب وتحديث تصنيفات وقنوات الحساب"
                                },
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                    }

                    if (!fetchError.isNullOrBlank()) {
                        Spacer(Modifier.height(10.dp))
                        Surface(
                            color = FoulLiveRed.copy(alpha = 0.16f),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, FoulLiveRed.copy(alpha = 0.6f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = fetchError,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            }
        }

        // ==================== STEP 2: ACCOUNT CATEGORIES ("تصنيفات الحساب - جلب واختيار") ====================
        if (fetchedCatalog != null) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = FoulCardDark),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, FoulDivider, RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Category,
                                    contentDescription = null,
                                    tint = FoulTeamAwayCyan,
                                    modifier = Modifier.size(20.dp)
                                )
                                Column {
                                    Text(
                                        text = "1. تصنيفات الحساب (${fetchedCatalog.categories.size} تصنيف)",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = Color.White,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    Text(
                                        text = "اختر تصنيفاً لعرض وجلب قنواته وتحديد ما تريد نشره",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = FoulTextSecondary
                                    )
                                }
                            }

                            Surface(
                                color = FoulPurpleDeep,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "إجمالي القنوات: ${fetchedCatalog.streams.size}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(Modifier.height(10.dp))

                        AccountBranchTextField(
                            value = categorySearchQuery,
                            onValueChange = { categorySearchQuery = it },
                            label = "بحث في تصنيفات الحساب (مثال: beIN أو Sports أو Arabic)...",
                            testTag = "dev_acc_branch_category_search_input"
                        )

                        Spacer(Modifier.height(10.dp))

                        // Horizontal Scrollable Category Pills + "All Categories"
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            item(key = "cat_all") {
                                val isSelected = selectedCategoryId == "ALL"
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) FoulPurpleBright else FoulCardElevated,
                                    border = BorderStroke(
                                        1.dp,
                                        if (isSelected) Color.White else FoulDivider
                                    ),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable { selectedCategoryId = "ALL" }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = "كل التصنيفات",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = Color.White,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                        Surface(
                                            color = Color.Black.copy(alpha = 0.35f),
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = "${fetchedCatalog.streams.size}",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = FoulTeamAwayCyan,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            items(filteredCategories, key = { it.categoryId }) { cat ->
                                val isSelected = selectedCategoryId == cat.categoryId
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) FoulPurpleBright else FoulCardElevated,
                                    border = BorderStroke(
                                        1.dp,
                                        if (isSelected) Color.White else FoulDivider
                                    ),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable {
                                            selectedCategoryId = cat.categoryId
                                            if (branchNameInput.isBlank()) {
                                                branchNameInput = cat.categoryName
                                            }
                                            // If this category has 0 cached channels, automatically fetch its content
                                            val currentCatCount = fetchedCatalog.streams.count { it.categoryId == cat.categoryId }
                                            if (currentCatCount == 0) {
                                                onFetchCategoryStreams(
                                                    hostInput.ifBlank { fetchedCatalog.host },
                                                    usernameInput.ifBlank { fetchedCatalog.username },
                                                    passwordInput.ifBlank { fetchedCatalog.password },
                                                    cat.categoryId,
                                                    cat.categoryName
                                                )
                                            }
                                        }
                                        .testTag("dev_acc_cat_chip_${cat.categoryId}")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = cat.categoryName,
                                            style = MaterialTheme.typography.labelLarge,
                                            color = Color.White,
                                            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium
                                        )
                                        Surface(
                                            color = Color.Black.copy(alpha = 0.35f),
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = "${cat.channelCount}",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = if (isSelected) FoulGoldStar else FoulTeamAwayCyan,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Selected Category Action Row: Fetch Category Content Directly + Use Category Name as Branch Name
                        if (selectedCategoryObj != null) {
                            Spacer(Modifier.height(10.dp))
                            Surface(
                                color = FoulCardElevated,
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, FoulPurpleBright.copy(alpha = 0.45f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 9.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "التصنيف المختار: ${selectedCategoryObj.categoryName}",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = Color.White,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                        Text(
                                            text = "عدد قنوات التصنيف: ${displayedStreams.size} قناة جاهزة للاختيار",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = FoulTeamAwayCyan
                                        )
                                    }

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        OutlinedButton(
                                            onClick = {
                                                branchNameInput = selectedCategoryObj.categoryName
                                                val firstLogo = displayedStreams.firstOrNull { it.streamIcon.isNotBlank() }?.streamIcon
                                                if (branchImageUrlInput.isBlank() && !firstLogo.isNullOrBlank()) {
                                                    branchImageUrlInput = firstLogo
                                                }
                                            },
                                            shape = RoundedCornerShape(10.dp),
                                            border = BorderStroke(1.dp, FoulGoldStar),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                                        ) {
                                            Text(
                                                text = "اعتماد كاسم للفرع",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = FoulGoldStar,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }

                                        Button(
                                            onClick = {
                                                onFetchCategoryStreams(
                                                    hostInput.ifBlank { fetchedCatalog.host },
                                                    usernameInput.ifBlank { fetchedCatalog.username },
                                                    passwordInput.ifBlank { fetchedCatalog.password },
                                                    selectedCategoryObj.categoryId,
                                                    selectedCategoryObj.categoryName
                                                )
                                            },
                                            enabled = !isFetchingCategoryStreams,
                                            colors = ButtonDefaults.buttonColors(containerColor = FoulPurpleBright),
                                            shape = RoundedCornerShape(10.dp),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                            modifier = Modifier.testTag("dev_acc_fetch_category_content_button")
                                        ) {
                                            if (isFetchingCategoryStreams) {
                                                CircularProgressIndicator(
                                                    color = Color.White,
                                                    strokeWidth = 2.dp,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                                Spacer(Modifier.width(6.dp))
                                            } else {
                                                Icon(
                                                    imageVector = Icons.Default.Refresh,
                                                    contentDescription = null,
                                                    tint = Color.White,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                                Spacer(Modifier.width(4.dp))
                                            }
                                            Text(
                                                text = "جلب محتوى التصنيف",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = Color.White,
                                                fontWeight = FontWeight.ExtraBold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // ==================== STEP 3: CATEGORY CHANNELS SELECTION ("إمكانية إختيار قنوات محددة") ====================
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = FoulCardDark),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, FoulDivider, RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LiveTv,
                                    contentDescription = null,
                                    tint = FoulLiveGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                                Column {
                                    Text(
                                        text = "2. اختيار القنوات من التصنيف (${displayedStreams.size} قناة معروضة)",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = Color.White,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    Text(
                                        text = "اضغط على أي قناة لإضافتها أو إزالتها، أو حدد كل قنوات التصنيف دفعة واحدة",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = FoulTextSecondary
                                    )
                                }
                            }

                            Surface(
                                color = if (selectedOrderedStreams.isNotEmpty()) FoulLiveGreen.copy(alpha = 0.2f) else FoulCardElevated,
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(
                                    1.dp,
                                    if (selectedOrderedStreams.isNotEmpty()) FoulLiveGreen else FoulDivider
                                )
                            ) {
                                Text(
                                    text = "المختار: ${selectedOrderedStreams.size}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (selectedOrderedStreams.isNotEmpty()) FoulLiveGreen else Color.White,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                        }

                        Spacer(Modifier.height(10.dp))

                        AccountBranchTextField(
                            value = channelSearchQuery,
                            onValueChange = { channelSearchQuery = it },
                            label = "بحث داخل القنوات بالاسم (مثال: beIN Sports 1 أو SSC أو الكأس)...",
                            testTag = "dev_acc_branch_channel_search_input"
                        )

                        Spacer(Modifier.height(10.dp))

                        // Bulk Selection Buttons Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    val existingIds = selectedOrderedStreams.map { it.streamId }.toHashSet()
                                    for (stream in displayedStreams) {
                                        if (existingIds.add(stream.streamId)) {
                                            selectedOrderedStreams.add(stream)
                                        }
                                    }
                                    if (branchNameInput.isBlank() && selectedCategoryObj != null) {
                                        branchNameInput = selectedCategoryObj.categoryName
                                    }
                                    if (branchImageUrlInput.isBlank()) {
                                        branchImageUrlInput = selectedOrderedStreams.firstOrNull { it.streamIcon.isNotBlank() }?.streamIcon.orEmpty()
                                    }
                                },
                                enabled = displayedStreams.isNotEmpty(),
                                colors = ButtonDefaults.buttonColors(containerColor = FoulPurpleDeep),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("dev_acc_select_all_category_channels_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DoneAll,
                                    contentDescription = null,
                                    tint = FoulLiveGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    text = "تحديد كل المعروض (${displayedStreams.size})",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            OutlinedButton(
                                onClick = {
                                    val displayedIds = displayedStreams.map { it.streamId }.toHashSet()
                                    selectedOrderedStreams.removeAll { it.streamId in displayedIds }
                                },
                                enabled = selectedOrderedStreams.isNotEmpty(),
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, FoulLiveRed.copy(alpha = 0.7f)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DeleteSweep,
                                    contentDescription = null,
                                    tint = FoulLiveRed,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    text = "إلغاء تحديد المعروض",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(Modifier.height(10.dp))

                        // Scrollable Box of Channels in the Selected Category
                        if (displayedStreams.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 22.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "لا توجد قنوات مطابقة في هذا التصنيف حالياً",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = FoulTextSecondary
                                )
                            }
                        } else {
                            val visibleSlice = remember(displayedStreams) {
                                displayedStreams.take(250)
                            }
                            Column(
                                verticalArrangement = Arrangement.spacedBy(7.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                visibleSlice.forEach { stream ->
                                    val isSelected = selectedStreamIdsSet.contains(stream.streamId)
                                    val orderIndex = if (isSelected) {
                                        selectedOrderedStreams.indexOfFirst { it.streamId == stream.streamId } + 1
                                    } else 0

                                    Surface(
                                        color = if (isSelected) FoulPurpleDeep.copy(alpha = 0.65f) else FoulCardElevated,
                                        shape = RoundedCornerShape(12.dp),
                                        border = BorderStroke(
                                            width = 1.dp,
                                            color = if (isSelected) FoulPurpleBright else FoulDivider
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .clickable {
                                                if (isSelected) {
                                                    selectedOrderedStreams.removeAll { it.streamId == stream.streamId }
                                                } else {
                                                    selectedOrderedStreams.add(stream)
                                                    if (branchImageUrlInput.isBlank() && stream.streamIcon.isNotBlank()) {
                                                        branchImageUrlInput = stream.streamIcon
                                                    }
                                                }
                                            }
                                            .testTag("dev_acc_stream_item_${stream.streamId}")
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 10.dp, vertical = 8.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Icon(
                                                    imageVector = if (isSelected) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
                                                    contentDescription = null,
                                                    tint = if (isSelected) FoulLiveGreen else Color.White.copy(alpha = 0.55f),
                                                    modifier = Modifier.size(22.dp)
                                                )

                                                Surface(
                                                    shape = RoundedCornerShape(8.dp),
                                                    color = FoulBlackBg,
                                                    modifier = Modifier.size(40.dp)
                                                ) {
                                                    if (stream.streamIcon.isNotBlank()) {
                                                        AsyncImage(
                                                            model = stream.streamIcon,
                                                            contentDescription = stream.name,
                                                            contentScale = ContentScale.Fit,
                                                            modifier = Modifier
                                                                .fillMaxSize()
                                                                .padding(3.dp)
                                                        )
                                                    } else {
                                                        Box(contentAlignment = Alignment.Center) {
                                                            Icon(
                                                                imageVector = Icons.Default.LiveTv,
                                                                contentDescription = null,
                                                                tint = FoulPurpleBright,
                                                                modifier = Modifier.size(20.dp)
                                                            )
                                                        }
                                                    }
                                                }

                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = stream.name,
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        color = Color.White,
                                                        fontWeight = FontWeight.ExtraBold,
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis
                                                    )
                                                    Text(
                                                        text = "${stream.categoryName} • M3U8 + TS",
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = FoulTextSecondary,
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis
                                                    )
                                                }
                                            }

                                            if (isSelected && orderIndex > 0) {
                                                Surface(
                                                    color = FoulLiveGreen,
                                                    shape = RoundedCornerShape(8.dp)
                                                ) {
                                                    Text(
                                                        text = "#$orderIndex",
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = Color.Black,
                                                        fontWeight = FontWeight.ExtraBold,
                                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                if (displayedStreams.size > visibleSlice.size) {
                                    Text(
                                        text = "يتم عرض أول ${visibleSlice.size} قناة من أصل ${displayedStreams.size} — استخدم مربع البحث أعلاه للوصول الفوري لأي قناة محددة",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = FoulGoldStar,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // ==================== STEP 4: REORDER SELECTED CHANNELS ("وأيضا ترتيب القنوات") ====================
        if (selectedOrderedStreams.isNotEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = FoulCardDark),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, FoulPurpleBright.copy(alpha = 0.7f), RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "3. ترتيب القنوات المختارة قبل النشر (${selectedOrderedStreams.size} قناة)",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Text(
                                    text = "رتّب القنوات للأعلى أو للأسفل ليتم عرضها بنفس هذا الترتيب داخل الفرع",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = FoulTextSecondary
                                )
                            }

                            Surface(
                                color = FoulLiveRed.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, FoulLiveRed.copy(alpha = 0.65f)),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { selectedOrderedStreams.clear() }
                            ) {
                                Text(
                                    text = "مسح الكل",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                        }

                        Spacer(Modifier.height(10.dp))

                        Column(
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            selectedOrderedStreams.forEachIndexed { idx, item ->
                                Surface(
                                    color = FoulCardElevated,
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, FoulDivider),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Surface(
                                                color = FoulPurpleBright,
                                                shape = RoundedCornerShape(6.dp)
                                            ) {
                                                Text(
                                                    text = "#${idx + 1}",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = Color.White,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                                                )
                                            }
                                            Text(
                                                text = item.name,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                                        ) {
                                            IconButton(
                                                onClick = {
                                                    if (idx > 0) {
                                                        val removed = selectedOrderedStreams.removeAt(idx)
                                                        selectedOrderedStreams.add(idx - 1, removed)
                                                    }
                                                },
                                                enabled = idx > 0,
                                                modifier = Modifier.size(30.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.ArrowUpward,
                                                    contentDescription = "تحريك للأعلى",
                                                    tint = if (idx > 0) Color.White else Color.White.copy(alpha = 0.25f),
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }

                                            IconButton(
                                                onClick = {
                                                    if (idx < selectedOrderedStreams.lastIndex) {
                                                        val removed = selectedOrderedStreams.removeAt(idx)
                                                        selectedOrderedStreams.add(idx + 1, removed)
                                                    }
                                                },
                                                enabled = idx < selectedOrderedStreams.lastIndex,
                                                modifier = Modifier.size(30.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.ArrowDownward,
                                                    contentDescription = "تحريك للأسفل",
                                                    tint = if (idx < selectedOrderedStreams.lastIndex) Color.White else Color.White.copy(alpha = 0.25f),
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }

                                            IconButton(
                                                onClick = {
                                                    selectedOrderedStreams.removeAt(idx)
                                                },
                                                modifier = Modifier.size(30.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Close,
                                                    contentDescription = "إزالة القناة",
                                                    tint = FoulLiveRed,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // ==================== STEP 5: BRANCH DETAILS & FINAL PUBLISH ("نشر فرع بعد إضافة اسم الفرع ورابط صورة الفرع") ====================
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = FoulCardDark),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        brush = Brush.horizontalGradient(
                            colors = listOf(FoulLiveGreen, FoulPurpleBright)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FolderSpecial,
                            contentDescription = null,
                            tint = FoulGoldStar,
                            modifier = Modifier.size(22.dp)
                        )
                        Column {
                            Text(
                                text = "4. بيانات الفرع والنشر في الرئيسية",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "أضف اسم الفرع ورابط صورة الفرع ثم اضغط نشر الفرع مع القنوات المختارة",
                                style = MaterialTheme.typography.bodySmall,
                                color = FoulTextSecondary
                            )
                        }
                    }

                    Spacer(Modifier.height(10.dp))

                    AccountBranchTextField(
                        value = branchNameInput,
                        onValueChange = { branchNameInput = it },
                        label = "اسم الفرع (مثال: باقة قنوات بي إن سبورت الحساب)",
                        testTag = "dev_acc_branch_name_input"
                    )

                    Spacer(Modifier.height(8.dp))

                    AccountBranchTextField(
                        value = branchImageUrlInput,
                        onValueChange = { branchImageUrlInput = it },
                        label = "رابط صورة الفرع (http://... أو https://...)",
                        testTag = "dev_acc_branch_image_input"
                    )

                    Spacer(Modifier.height(10.dp))

                    Text(
                        text = "مكان النشر (فرع رئيسي جديد أو داخل فرع موجود):",
                        style = MaterialTheme.typography.labelMedium,
                        color = FoulTextSecondary,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(6.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item {
                            val isRoot = selectedParentBranchId.isBlank() && targetExistingBranchId == null
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isRoot) FoulPurpleBright else FoulCardElevated,
                                border = BorderStroke(1.dp, if (isRoot) Color.White else FoulDivider),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        selectedParentBranchId = ""
                                        targetExistingBranchId = null
                                    }
                            ) {
                                Text(
                                    text = "فرع رئيسي جديد (في الرئيسية)",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White,
                                    fontWeight = if (isRoot) FontWeight.ExtraBold else FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                )
                            }
                        }

                        items(branches.sortedBy { it.order }, key = { "parent_${it.id}" }) { br ->
                            val isSelectedParent = selectedParentBranchId == br.id && targetExistingBranchId == null
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelectedParent) FoulPurpleBright else FoulCardElevated,
                                border = BorderStroke(1.dp, if (isSelectedParent) Color.White else FoulDivider),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        selectedParentBranchId = br.id
                                        targetExistingBranchId = null
                                    }
                            ) {
                                Text(
                                    text = "فرع داخل: ${br.name}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White,
                                    fontWeight = if (isSelectedParent) FontWeight.ExtraBold else FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }

                    // Optional: Update/Append to an existing branch directly
                    if (branches.isNotEmpty()) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "أو تحديث فرع حالي موجود مباشرة:",
                            style = MaterialTheme.typography.labelSmall,
                            color = FoulTeamAwayCyan,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(6.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(branches.sortedBy { it.order }, key = { "existing_${it.id}" }) { br ->
                                val isTarget = targetExistingBranchId == br.id
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isTarget) FoulLiveGreen.copy(alpha = 0.25f) else FoulCardElevated,
                                    border = BorderStroke(1.dp, if (isTarget) FoulLiveGreen else FoulDivider),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable {
                                            if (isTarget) {
                                                targetExistingBranchId = null
                                            } else {
                                                targetExistingBranchId = br.id
                                                branchNameInput = br.name
                                                branchImageUrlInput = br.imageUrl
                                                selectedParentBranchId = br.parentId
                                            }
                                        }
                                ) {
                                    Text(
                                        text = "تحديث: ${br.name}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White,
                                        fontWeight = if (isTarget) FontWeight.ExtraBold else FontWeight.Medium,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    Button(
                        onClick = {
                            onPublishAccountBranch(
                                targetExistingBranchId,
                                branchNameInput,
                                branchImageUrlInput,
                                selectedParentBranchId,
                                selectedOrderedStreams.toList()
                            )
                        },
                        enabled = !isActionLoading && selectedOrderedStreams.isNotEmpty() && branchNameInput.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = FoulLiveGreen),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("dev_acc_branch_publish_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Publish,
                            contentDescription = null,
                            tint = Color.Black
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "نشر فرع الحساب مع (${selectedOrderedStreams.size} قناة مرتبة) الآن",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.Black,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AccountBranchTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    testTag: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, style = MaterialTheme.typography.bodySmall) },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = FoulPurpleBright,
            unfocusedBorderColor = FoulDivider,
            focusedContainerColor = FoulCardElevated,
            unfocusedContainerColor = FoulCardElevated,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedLabelColor = FoulPurpleBright,
            unfocusedLabelColor = FoulTextSecondary
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag)
    )
}
