package com.example.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.SettingsBrightness
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MaterialEntity
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassTextField
import com.example.ui.theme.AppThemeMode
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonViolet
import com.example.viewmodel.BakriViewModel

@Composable
fun SettingsScreen(
    viewModel: BakriViewModel,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val themeMode by viewModel.themeMode.collectAsState()
    val defaultDesignFee by viewModel.defaultDesignFee.collectAsState()
    val exchangeRate by viewModel.usdExchangeRate.collectAsState()
    val materials by viewModel.materials.collectAsState()
    val summary by viewModel.financialSummary.collectAsState()

    var showAddMaterialDialog by remember { mutableStateOf(false) }
    var materialToEdit by remember { mutableStateOf<MaterialEntity?>(null) }

    // Dialog state for add material
    var newMatNameAr by remember { mutableStateOf("") }
    var newMatPriceStr by remember { mutableStateOf("") }
    var newMatCategory by remember { mutableStateOf("") }

    // State for default fee & exchange rate edits
    var showFeeDialog by remember { mutableStateOf(false) }
    var editFeeInput by remember { mutableStateOf(defaultDesignFee.toInt().toString()) }

    var showRateDialog by remember { mutableStateOf(false) }
    var editRateInput by remember { mutableStateOf(exchangeRate.toInt().toString()) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "⚙️ لوحة التحكم والإعدادات المالية",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "تعديل تسعيرة الخامات بالمتر، رسوم التصميم، سعر الصرف، والمظهر",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // 1. Financial KPI Summary Card
        item {
            GlassCard {
                Text(
                    text = "المؤشرات المالية الإجمالية",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "إجمالي مبيعات الطلبات:",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = viewModel.formatMoney(summary.totalBillingsYer),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "المبالغ المحصلة (كاش):",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = viewModel.formatMoney(summary.totalCollectedYer),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = EmeraldSuccess
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Divider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.8.dp)
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "مستحقات معلقة لدى العملاء:",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = viewModel.formatMoney(summary.pendingClientBalancesYer),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) NeonCyan else Color(0xFF0284C7)
                            )
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "ديون المطابع الوسيطة:",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = viewModel.formatMoney(summary.totalVendorDebtsYer),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFEF4444)
                            )
                        )
                    }
                }
            }
        }

        // 2. Theme Preferences
        item {
            GlassCard {
                Text(
                    text = "مظهر التطبيق (Liquid Glassmorphism)",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "اختر النمط المفضل لأسطح الزجاج العائم والإضاءات النيونية",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ThemeOptionButton(
                        title = "تلقائي النظام",
                        icon = Icons.Default.SettingsBrightness,
                        isSelected = themeMode == AppThemeMode.SYSTEM,
                        onClick = { viewModel.setThemeMode(AppThemeMode.SYSTEM) },
                        modifier = Modifier.weight(1f)
                    )
                    ThemeOptionButton(
                        title = "داكن (Obsidian)",
                        icon = Icons.Default.DarkMode,
                        isSelected = themeMode == AppThemeMode.DARK,
                        onClick = { viewModel.setThemeMode(AppThemeMode.DARK) },
                        modifier = Modifier.weight(1f)
                    )
                    ThemeOptionButton(
                        title = "فاتح (Crystal)",
                        icon = Icons.Default.LightMode,
                        isSelected = themeMode == AppThemeMode.LIGHT,
                        onClick = { viewModel.setThemeMode(AppThemeMode.LIGHT) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // 3. Financial Presets: Design Fee & Exchange Rate
        item {
            GlassCard {
                Text(
                    text = "إعدادات التسعير والعملات",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(10.dp))

                // Default Design Fee row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isDark) Color(0x20162033) else Color(0x15E2E8F0))
                        .clickable { showFeeDialog = true }
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Brush, contentDescription = null, tint = NeonViolet, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "رسوم التصميم الافتراضية",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "تطبق عند إلغاء زر (التصميم جاهز)",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = viewModel.formatMoney(defaultDesignFee),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = NeonViolet
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // USD Exchange Rate row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isDark) Color(0x20162033) else Color(0x15E2E8F0))
                        .clickable { showRateDialog = true }
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.AttachMoney, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "سعر صرف الدولار (USD / YER)",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "للتحويل الفوري بين الريال والدولار",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "1$ = ${exchangeRate.toInt()} ر.ي",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = EmeraldSuccess
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        // 4. Materials Pricing Catalog
        item {
            GlassCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "قائمة أسعار الخامات والمطبوعات",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "تعديل سعر المتر المربع (م²) أو إضافة خامات جديدة",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Button(
                        onClick = { showAddMaterialDialog = true },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("إضافة خامة", style = MaterialTheme.typography.labelSmall)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    materials.forEach { mat ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isDark) Color(0x20162033) else Color(0x10000000))
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = mat.nameAr,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "التصنيف: ${mat.category}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${viewModel.formatMoney(mat.pricePerSqmYer)} / م²",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isDark) NeonCyan else Color(0xFF0284C7)
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                IconButton(
                                    onClick = { materialToEdit = mat },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
            }
        }

        // About Card
        item {
            GlassCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "بكري برنت (Bakri Print) v1.0",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "نظام محلي متكامل يعمل 100% بدون إنترنت عبر قاعدة بيانات Room المدمجة.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }

    // Modal: Edit Design Fee
    if (showFeeDialog) {
        AlertDialog(
            onDismissRequest = { showFeeDialog = false },
            title = { Text("تعديل رسوم التصميم الافتراضية") },
            text = {
                GlassTextField(
                    value = editFeeInput,
                    onValueChange = { editFeeInput = it },
                    label = "رسوم التصميم (ر.ي)",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
            },
            confirmButton = {
                Button(onClick = {
                    val fee = editFeeInput.toDoubleOrNull() ?: 2000.0
                    viewModel.setDefaultDesignFee(fee)
                    showFeeDialog = false
                }) { Text("تحديث") }
            },
            dismissButton = {
                TextButton(onClick = { showFeeDialog = false }) { Text("إلغاء") }
            }
        )
    }

    // Modal: Edit Exchange Rate
    if (showRateDialog) {
        AlertDialog(
            onDismissRequest = { showRateDialog = false },
            title = { Text("تعديل سعر صرف الدولار") },
            text = {
                GlassTextField(
                    value = editRateInput,
                    onValueChange = { editRateInput = it },
                    label = "سعر الدولار مقابل الريال (YER)",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
            },
            confirmButton = {
                Button(onClick = {
                    val rate = editRateInput.toDoubleOrNull() ?: 530.0
                    viewModel.setUsdExchangeRate(rate)
                    showRateDialog = false
                }) { Text("تحديث") }
            },
            dismissButton = {
                TextButton(onClick = { showRateDialog = false }) { Text("إلغاء") }
            }
        )
    }

    // Modal: Add Material
    if (showAddMaterialDialog) {
        AlertDialog(
            onDismissRequest = { showAddMaterialDialog = false },
            title = { Text("إضافة خامة طباعة جديدة") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    GlassTextField(
                        value = newMatNameAr,
                        onValueChange = { newMatNameAr = it },
                        label = "اسم الخامة باللغة العربية",
                        placeholder = "مثال: استكر حراري للسيارات"
                    )
                    GlassTextField(
                        value = newMatPriceStr,
                        onValueChange = { newMatPriceStr = it },
                        label = "سعر المتر المربع (ر.ي)",
                        placeholder = "مثال: 3000",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                    GlassTextField(
                        value = newMatCategory,
                        onValueChange = { newMatCategory = it },
                        label = "التصنيف",
                        placeholder = "استكرات، بنرات، فلكس، لوحات..."
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    val price = newMatPriceStr.toDoubleOrNull() ?: 0.0
                    if (newMatNameAr.isNotBlank() && price > 0) {
                        viewModel.addMaterial(newMatNameAr, price, newMatCategory.ifBlank { "عام" })
                        newMatNameAr = ""
                        newMatPriceStr = ""
                        newMatCategory = ""
                        showAddMaterialDialog = false
                    }
                }) { Text("إضافة") }
            },
            dismissButton = {
                TextButton(onClick = { showAddMaterialDialog = false }) { Text("إلغاء") }
            }
        )
    }

    // Modal: Edit Material Price
    materialToEdit?.let { mat ->
        var editPriceStr by remember { mutableStateOf(mat.pricePerSqmYer.toInt().toString()) }
        AlertDialog(
            onDismissRequest = { materialToEdit = null },
            title = { Text("تعديل سعر خامة: ${mat.nameAr}") },
            text = {
                GlassTextField(
                    value = editPriceStr,
                    onValueChange = { editPriceStr = it },
                    label = "سعر المتر المربع (ر.ي)",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
            },
            confirmButton = {
                Button(onClick = {
                    val price = editPriceStr.toDoubleOrNull() ?: mat.pricePerSqmYer
                    viewModel.updateMaterial(mat.copy(pricePerSqmYer = price))
                    materialToEdit = null
                }) { Text("حفظ السعر") }
            },
            dismissButton = {
                TextButton(onClick = { materialToEdit = null }) { Text("إلغاء") }
            }
        )
    }
}

@Composable
private fun ThemeOptionButton(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val bgColor = if (isSelected) {
        if (isDark) Color(0x3300F2FE) else Color(0x260284C7)
    } else {
        if (isDark) Color(0x15FFFFFF) else Color(0x10000000)
    }
    val textColor = if (isSelected) {
        if (isDark) NeonCyan else Color(0xFF0284C7)
    } else MaterialTheme.colorScheme.onSurfaceVariant

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(
                1.dp,
                if (isSelected) (if (isDark) NeonCyan else Color(0xFF0284C7)) else Color.Transparent,
                RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(imageVector = icon, contentDescription = null, tint = textColor, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                ),
                color = textColor
            )
        }
    }
}
