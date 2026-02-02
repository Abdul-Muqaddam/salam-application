package com.webinane.salam.ui.zakat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webinane.salam.R
import com.webinane.salam.ui.theme.DarkBlueNavy
import com.webinane.salam.ui.theme.LightBlueTeal
import com.webinane.salam.ui.viewmodel.ZakatViewModel
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import org.koin.androidx.compose.koinViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ZakatCalculatorScreen(
    viewModel: ZakatViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            ZakatHeader(onNavigateBack)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
                .verticalScroll(scrollState)
        ) {
            HeroSection()
            
            Spacer(modifier = Modifier.height((-24).sdp)) // Overlap effect
            
            Column(modifier = Modifier.padding(horizontal = 16.sdp)) {
                QuickStatsSection(state.goldNisab, state.silverNisab)
                Spacer(modifier = Modifier.height(16.sdp))
                
                AssetsInputSection(viewModel, state)
                Spacer(modifier = Modifier.height(16.sdp))
                
                LiabilitiesSection(viewModel, state)
                Spacer(modifier = Modifier.height(16.sdp))
                
                NisabReferenceSection(state.goldNisab, state.silverNisab)
                Spacer(modifier = Modifier.height(16.sdp))
                
                // Calculation Summary (always visible, updates dynamically could be better but sticking to design flow)
                 CalculationSummarySection(state)
                 Spacer(modifier = Modifier.height(16.sdp))
                 
                CalculateButton(onClick = { viewModel.calculateZakat() })
                
                AnimatedVisibility(visible = state.showResult) {
                     Column {
                        Spacer(modifier = Modifier.height(16.sdp))
                        ResultSection(state)
                     }
                }
                
                Spacer(modifier = Modifier.height(16.sdp))
                ZakatInfoSection()
                
                Spacer(modifier = Modifier.height(16.sdp))
                EligibleRecipientsSection()
                
                Spacer(modifier = Modifier.height(16.sdp))
                FAQSection()
                
                Spacer(modifier = Modifier.height(16.sdp))
                PaymentOptionsSection()
                
                Spacer(modifier = Modifier.height(16.sdp))
                ReminderSection()
                
                Spacer(modifier = Modifier.height(16.sdp))
                HadithSection()
                
                Spacer(modifier = Modifier.height(24.sdp))
                FooterSection()
                Spacer(modifier = Modifier.height(24.sdp))
            }
        }
    }
}

@Composable
fun ZakatHeader(onBack: () -> Unit) {
    Row(
        modifier = Modifier

            .fillMaxWidth()
            .background(LightBlueTeal)
            .padding(16.sdp)
            .statusBarsPadding()
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onBack) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(24.sdp)
            )
        }
        Text(
            text = "Salam",
            color = Color.White,
            fontSize = 18.ssp,
            fontWeight = FontWeight.SemiBold
        )
        IconButton(onClick = { /* TODO */ }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_notifications), // Using existing bell/notification icon
                contentDescription = "Notifications",
                tint = Color.White,
                modifier = Modifier.size(24.sdp)
            )
        }
    }
}

@Composable
fun HeroSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(LightBlueTeal, DarkBlueNavy)
                )
            )
            .padding(top = 16.sdp, bottom = 40.sdp, start = 16.sdp, end = 16.sdp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.sdp)
                    .background(Color.White.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_hand_holding_heart),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.sdp)
                )
            }
            Spacer(modifier = Modifier.height(12.sdp))
            Text(
                text = "Zakat Calculator",
                color = Color.White,
                fontSize = 20.ssp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.sdp))
            Text(
                text = "Calculate your Zakat according to Islamic principles",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 12.ssp,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(200.sdp)
            )
        }
    }
}

@Composable
fun QuickStatsSection(goldNisab: Double, silverNisab: Double) {
    Card(
        shape = RoundedCornerShape(12.sdp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.sdp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.sdp)) {
                StatCard(
                    title = "Gold Nisab",
                    subtitle = "87.48 grams",
                    value = formatCurrency(goldNisab / 280.0), // Convert PKR to USD
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Silver Nisab",
                    subtitle = "612.36 grams",
                    value = formatCurrency(silverNisab / 280.0), // Convert PKR to USD
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(12.sdp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE3F2FD), RoundedCornerShape(8.sdp))
                    .padding(8.sdp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_info),
                    contentDescription = null,
                    tint = LightBlueTeal,
                    modifier = Modifier.size(14.sdp).padding(top = 2.dp)
                )
                Spacer(modifier = Modifier.width(8.sdp))
                Text(
                    text = "Nisab is the minimum wealth required before Zakat becomes obligatory",
                    color = DarkBlueNavy,
                    fontSize = 10.ssp,
                    lineHeight = 14.sp
                )
            }
        }
    }
}

@Composable
fun StatCard(title: String, subtitle: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(Color(0xFFF1F3F5), RoundedCornerShape(10.sdp))
            .padding(10.sdp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = title, fontSize = 10.ssp, color = Color.Gray, fontWeight = FontWeight.Medium)
        Text(text = subtitle, fontSize = 8.ssp, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.sdp))
        Text(text = value, fontSize = 12.ssp, fontWeight = FontWeight.Bold, color = LightBlueTeal)
    }
}

@Composable
fun AssetsInputSection(viewModel: ZakatViewModel, state: com.webinane.salam.ui.viewmodel.ZakatState) {
    Column {
        Text(text = "Your Assets", fontSize = 16.ssp, fontWeight = FontWeight.Bold, color = DarkBlueNavy)
        Text(text = "Enter all your zakatable assets", fontSize = 11.ssp, color = Color.Gray)
        Spacer(modifier = Modifier.height(12.sdp))
        
        InputItem(
            label = "Cash in Hand",
            iconRes = R.drawable.ic_cash,
            value = state.cashInHand,
            onValueChange = viewModel::onCashInHandChange
        )
        Spacer(modifier = Modifier.height(8.sdp))
        InputItem(
            label = "Bank Balance",
            iconRes = R.drawable.ic_bank,
            value = state.bankBalance,
            onValueChange = viewModel::onBankBalanceChange
        )
         Spacer(modifier = Modifier.height(8.sdp))
        InputItem(
            label = "Gold Value",
            iconRes = R.drawable.ic_coins,
            value = state.goldValue,
            onValueChange = viewModel::onGoldValueChange,
            showHelp = true
        )
         Spacer(modifier = Modifier.height(8.sdp))
        InputItem(
            label = "Silver Value",
            iconRes = R.drawable.ic_gem,
            value = state.silverValue,
            onValueChange = viewModel::onSilverValueChange,
             showHelp = true
        )
         Spacer(modifier = Modifier.height(8.sdp))
        InputItem(
            label = "Business Assets",
            iconRes = R.drawable.ic_briefcase,
            value = state.businessAssets,
            onValueChange = viewModel::onBusinessAssetsChange
        )
         Spacer(modifier = Modifier.height(8.sdp))
        InputItem(
            label = "Other Savings",
            iconRes = R.drawable.ic_piggy_bank,
            value = state.otherSavings,
            onValueChange = viewModel::onOtherSavingsChange
        )
    }
}

@Composable
fun LiabilitiesSection(viewModel: ZakatViewModel, state: com.webinane.salam.ui.viewmodel.ZakatState) {
    Column {
        Text(text = "Liabilities", fontSize = 16.ssp, fontWeight = FontWeight.Bold, color = DarkBlueNavy)
        Text(text = "Deduct your immediate debts", fontSize = 11.ssp, color = Color.Gray)
        Spacer(modifier = Modifier.height(12.sdp))
        
        Card(
             shape = RoundedCornerShape(12.sdp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.sdp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                         Icon(
                            painter = painterResource(id = R.drawable.ic_credit_card),
                            contentDescription = null,
                            tint = LightBlueTeal, // Changed from Red
                            modifier = Modifier.size(16.sdp)
                        )
                        Spacer(modifier = Modifier.width(8.sdp))
                        Text(text = "Debts / Liabilities", fontSize = 12.ssp, fontWeight = FontWeight.Medium, color = DarkBlueNavy)
                    }
                     Box(
                        modifier = Modifier
                            .size(20.sdp)
                            .background(LightBlueTeal.copy(alpha = 0.1f), CircleShape)
                            .clickable { /* TODO show help */ },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_question),
                            contentDescription = "Help",
                            tint = LightBlueTeal,
                            modifier = Modifier.size(10.sdp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.sdp))
                OutlinedTextField(
                    value = state.debts,
                    onValueChange = viewModel::onDebtsChange,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.sdp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LightBlueTeal,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = Color(0xFFF8F9FA),
                        unfocusedContainerColor = Color(0xFFF8F9FA)
                    ),
                    leadingIcon = {
                        Text(text = "$", color = Color.Gray, fontSize = 12.ssp)
                    },
                    placeholder = {
                        Text(text = "0.00", color = Color.Gray)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                
                 Spacer(modifier = Modifier.height(8.sdp))
                 Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(LightBlueTeal.copy(alpha = 0.1f), RoundedCornerShape(8.sdp))
                        .padding(8.sdp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_info),
                        contentDescription = null,
                        tint = LightBlueTeal,
                        modifier = Modifier.size(14.sdp).padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(8.sdp))
                    Text(
                        text = "Only immediate debts due within the year are deductible",
                        color = DarkBlueNavy,
                        fontSize = 10.ssp
                    )
                }
            }
        }
    }
}

@Composable
fun InputItem(
    label: String, 
    iconRes: Int, 
    value: String, 
    onValueChange: (String) -> Unit,
    showHelp: Boolean = false
) {
    Card(
        shape = RoundedCornerShape(12.sdp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.sdp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = null,
                        tint = LightBlueTeal,
                        modifier = Modifier.size(16.sdp)
                    )
                    Spacer(modifier = Modifier.width(8.sdp))
                    Text(text = label, fontSize = 12.ssp, fontWeight = FontWeight.Medium, color = DarkBlueNavy)
                }
                if (showHelp) {
                    Box(
                        modifier = Modifier
                            .size(20.sdp)
                            .background(LightBlueTeal.copy(alpha = 0.1f), CircleShape)
                            .clickable { /* TODO show help */ },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_question),
                            contentDescription = "Help",
                            tint = LightBlueTeal,
                            modifier = Modifier.size(10.sdp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.sdp))
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.sdp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = LightBlueTeal,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color(0xFFF8F9FA), // bg-bgLight
                    unfocusedContainerColor = Color(0xFFF8F9FA)
                ),
                leadingIcon = {
                    Text(text = "$", color = Color.Gray, fontSize = 12.ssp)
                },
                placeholder = {
                    Text(text = "0.00", color = Color.Gray)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        }
    }
}

@Composable
fun NisabReferenceSection(goldNisab: Double, silverNisab: Double) {
    Card(
        shape = RoundedCornerShape(12.sdp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        colors = listOf(LightBlueTeal, DarkBlueNavy)
                    )
                )
                .padding(16.sdp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Nisab Reference", fontSize = 14.ssp, fontWeight = FontWeight.Bold, color = Color.White)
                    Icon(
                        painter = painterResource(id = R.drawable.ic_scale_balanced),
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(24.sdp)
                    )
                }
                Spacer(modifier = Modifier.height(12.sdp))
                
                NisabItem(label = "Gold", quantity = "87.48 grams", value = formatCurrency(goldNisab / 280.0), iconRes = R.drawable.ic_coins)
                Spacer(modifier = Modifier.height(8.sdp))
                NisabItem(label = "Silver", quantity = "612.36 grams", value = formatCurrency(silverNisab / 280.0), iconRes = R.drawable.ic_gem)
                
                Spacer(modifier = Modifier.height(12.sdp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(8.sdp))
                        .padding(12.sdp)
                ) {
                    Text(
                        text = "Most scholars recommend using the silver nisab as it benefits more people in need",
                        color = Color.White,
                        fontSize = 10.ssp,
                        lineHeight = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun NisabItem(label: String, quantity: String, value: String, iconRes: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(10.sdp))
            .padding(12.sdp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(painter = painterResource(id = iconRes), contentDescription = null, tint = Color.White, modifier = Modifier.size(14.sdp))
                Spacer(modifier = Modifier.width(8.sdp))
                Text(text = label, color = Color.White, fontSize = 12.ssp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(4.sdp))
            Text(text = quantity, color = Color.White.copy(alpha = 0.9f), fontSize = 10.ssp)
            Text(text = "Current market value", color = Color.White.copy(alpha = 0.8f), fontSize = 9.ssp)
        }
        Text(text = value, color = Color.White, fontSize = 14.ssp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun CalculationSummarySection(state: com.webinane.salam.ui.viewmodel.ZakatState) {
    Card(
        shape = RoundedCornerShape(12.sdp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F3F5)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.sdp)) {
            Text(text = "Calculation Summary", fontSize = 13.ssp, fontWeight = FontWeight.Bold, color = DarkBlueNavy)
            Spacer(modifier = Modifier.height(12.sdp))
            
            SummaryRow(label = "Total Assets", value = formatCurrency(state.totalAssets), isBold = false)
            HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.sdp))
            
            SummaryRow(label = "Less: Liabilities", value = "-${formatCurrency(state.totalLiabilities)}", isBold = false, valueColor = DarkBlueNavy)
            HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.sdp))
            
            SummaryRow(label = "Net Zakatable Wealth", value = formatCurrency(state.netWealth), isBold = true)
            HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.sdp))
            
            SummaryRow(label = "Zakat Rate", value = "2.5%", isBold = true, valueColor = LightBlueTeal)
        }
    }
}

@Composable
fun SummaryRow(label: String, value: String, isBold: Boolean, valueColor: Color = DarkBlueNavy) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 11.ssp, color = if(isBold) DarkBlueNavy else Color.Gray, fontWeight = if(isBold) FontWeight.SemiBold else FontWeight.Normal)
        Text(text = value, fontSize = 11.ssp, color = valueColor, fontWeight = if(isBold) FontWeight.Bold else FontWeight.SemiBold)
    }
}

@Composable
fun CalculateButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.sdp),
        shape = RoundedCornerShape(12.sdp),
        colors = ButtonDefaults.buttonColors(containerColor = LightBlueTeal)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(painter = painterResource(id = R.drawable.ic_calculator), contentDescription = null, modifier = Modifier.size(16.sdp))
            Spacer(modifier = Modifier.width(8.sdp))
            Text(text = "Calculate Zakat", fontSize = 12.ssp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun ResultSection(state: com.webinane.salam.ui.viewmodel.ZakatState) {
    val context = LocalContext.current
    
    // Function to share Zakat results
    fun shareResults() {
        val shareText = buildString {
            appendLine("🕌 Zakat Calculation Results")
            appendLine()
            appendLine("📊 Summary:")
            appendLine("Total Assets: ${formatCurrency(state.totalAssets)}")
            appendLine("Total Liabilities: ${formatCurrency(state.totalLiabilities)}")
            appendLine("Net Wealth: ${formatCurrency(state.netWealth)}")
            appendLine()
            appendLine("💰 Zakat Payable: ${formatCurrency(state.zakatPayable)}")
            appendLine("Rate: 2.5% of net zakatable wealth")
            appendLine()
            if (state.isNisabReached) {
                appendLine("✅ Nisab threshold reached - Zakat is obligatory")
            } else {
                appendLine("❌ Below Nisab threshold - Zakat not obligatory")
            }
            appendLine()
            appendLine("Gold Nisab: ${formatCurrency(state.goldNisab / 280.0)} (87.48g)")
            appendLine("Silver Nisab: ${formatCurrency(state.silverNisab / 280.0)} (612.36g)")
            appendLine()
            appendLine("Calculated with Salam App 🌙")
        }
        
        val shareIntent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(android.content.Intent.EXTRA_SUBJECT, "My Zakat Calculation")
            putExtra(android.content.Intent.EXTRA_TEXT, shareText)
        }
        context.startActivity(android.content.Intent.createChooser(shareIntent, "Share Zakat Results"))
    }
    
    // Function to save results
    fun saveResults() {
        // Save to SharedPreferences for now (could be enhanced to save to file)
        val prefs = context.getSharedPreferences("ZakatCalculations", android.content.Context.MODE_PRIVATE)
        val timestamp = System.currentTimeMillis()
        
        prefs.edit().apply {
            putString("last_calculation_date", java.text.SimpleDateFormat("MMM dd, yyyy HH:mm", java.util.Locale.getDefault()).format(java.util.Date(timestamp)))
            putString("last_total_assets", state.totalAssets.toString())
            putString("last_total_liabilities", state.totalLiabilities.toString())
            putString("last_net_wealth", state.netWealth.toString())
            putString("last_zakat_payable", state.zakatPayable.toString())
            putBoolean("last_nisab_reached", state.isNisabReached)
            apply()
        }
        
        // Show toast confirmation
        android.widget.Toast.makeText(context, "✅ Calculation saved successfully!", android.widget.Toast.LENGTH_SHORT).show()
    }
    
    Card(
        shape = RoundedCornerShape(12.sdp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        colors = listOf(LightBlueTeal, DarkBlueNavy)
                    )
                )
                .padding(20.sdp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .size(56.sdp)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_hand_holding_dollar),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(28.sdp)
                    )
                }
                Spacer(modifier = Modifier.height(12.sdp))
                Text(text = "Your Zakat Payable", color = Color.White.copy(alpha = 0.9f), fontSize = 11.ssp)
                Spacer(modifier = Modifier.height(4.sdp))
                Text(text = formatCurrency(state.zakatPayable), color = Color.White, fontSize = 32.ssp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.sdp))
                Text(text = "2.5% of net zakatable wealth", color = Color.White.copy(alpha = 0.8f), fontSize = 10.ssp)
                
                Spacer(modifier = Modifier.height(16.sdp))
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(10.sdp))
                        .padding(12.sdp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text(text = if(state.isNisabReached) "Zakat Due" else "Zakat Not Due", color = Color.White, fontSize = 10.ssp)
                        Icon(
                            painter = painterResource(id = if(state.isNisabReached) R.drawable.ic_info else R.drawable.ic_info), // Check circle ideally
                             contentDescription = null, 
                             tint = if(state.isNisabReached) Color(0xFF86EFAC) else Color.White, // Green 300
                             modifier = Modifier.size(12.sdp)
                        )
                    }
                     Text(
                        text = if(state.isNisabReached) "Your wealth is above Nisab. Zakat is obligatory on you." else "Your wealth is below Nisab. Zakat is not obligatory.",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 10.ssp,
                        modifier = Modifier.padding(top = 20.dp) // Hack for layout, should be Column
                    )
                }
            }
        }
    }
    
    Spacer(modifier = Modifier.height(12.sdp))
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.sdp)) {
        OutlinedButton(
            onClick = { shareResults() },
            modifier = Modifier.weight(1f).height(48.sdp),
            shape = RoundedCornerShape(12.sdp),
            border = androidx.compose.foundation.BorderStroke(2.dp, LightBlueTeal),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = LightBlueTeal)
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_share_nodes), contentDescription = null, modifier = Modifier.size(14.sdp))
             Spacer(modifier = Modifier.width(8.sdp))
            Text(text = "Share", fontWeight = FontWeight.SemiBold)
        }
        OutlinedButton(
            onClick = { saveResults() },
            modifier = Modifier.weight(1f).height(48.sdp),
            shape = RoundedCornerShape(12.sdp),
            border = androidx.compose.foundation.BorderStroke(2.dp, LightBlueTeal),
             colors = ButtonDefaults.outlinedButtonColors(contentColor = LightBlueTeal)
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_download), contentDescription = null, modifier = Modifier.size(14.sdp))
             Spacer(modifier = Modifier.width(8.sdp))
            Text(text = "Save", fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun ZakatInfoSection() {
    Card(
        shape = RoundedCornerShape(12.sdp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.sdp)) {
             Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(32.sdp).background(LightBlueTeal.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(painter = painterResource(id = R.drawable.ic_book_quran), contentDescription = null, tint = LightBlueTeal, modifier = Modifier.size(16.sdp))
                }
                Spacer(modifier = Modifier.width(12.sdp))
                Text(text = "Understanding Zakat", fontSize = 13.ssp, fontWeight = FontWeight.Bold, color = DarkBlueNavy)
            }
            Spacer(modifier = Modifier.height(16.sdp))
            
            InfoRow(number = "1", title = "What is Zakat?", desc = "Zakat is one of the Five Pillars of Islam. It is an obligatory charity paid annually on wealth held for one lunar year.")
            Spacer(modifier = Modifier.height(12.sdp))
            InfoRow(number = "2", title = "When is it Due?", desc = "Zakat becomes obligatory when your wealth reaches or exceeds the Nisab threshold and you've held it for one full lunar year.")
            Spacer(modifier = Modifier.height(12.sdp))
             InfoRow(number = "3", title = "How Much to Pay?", desc = "The standard Zakat rate is 2.5% (1/40th) of your total qualifying wealth above the Nisab threshold.")
        }
    }
}

@Composable
fun InfoRow(number: String, title: String, desc: String) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier.size(24.sdp).background(LightBlueTeal.copy(alpha = 0.1f), RoundedCornerShape(6.sdp)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = number, color = LightBlueTeal, fontWeight = FontWeight.Bold, fontSize = 10.ssp)
        }
         Spacer(modifier = Modifier.width(12.sdp))
         Column {
             Text(text = title, fontSize = 11.ssp, fontWeight = FontWeight.SemiBold, color = DarkBlueNavy)
             Spacer(modifier = Modifier.height(4.sdp))
             Text(text = desc, fontSize = 10.ssp, color = Color.Gray, lineHeight = 14.sp)
         }
    }
}

@Composable
fun EligibleRecipientsSection() {
    Column {
        Text(text = "Who Can Receive Zakat?", fontSize = 14.ssp, fontWeight = FontWeight.Bold, color = DarkBlueNavy)
        Text(text = "Eight categories mentioned in the Quran", fontSize = 10.ssp, color = Color.Gray)
        Spacer(modifier = Modifier.height(12.sdp))
        
        Row(horizontalArrangement = Arrangement.spacedBy(10.sdp)) {
             RecipientCard(
                 title = "The Poor", 
                 desc = "Those with insufficient means", 
                 iconRes = R.drawable.ic_hands_holding_child, 
                 modifier = Modifier.weight(1f)
            )
             RecipientCard(
                 title = "The Needy", 
                 desc = "Those in difficult circumstances", 
                 iconRes = R.drawable.ic_person_shelter, 
                 modifier = Modifier.weight(1f)
            )
        }
         Spacer(modifier = Modifier.height(10.sdp))
         Row(horizontalArrangement = Arrangement.spacedBy(10.sdp)) {
             RecipientCard(
                 title = "Zakat Workers", 
                 desc = "Those who collect and distribute", 
                 iconRes = R.drawable.ic_graduation_cap, 
                 modifier = Modifier.weight(1f)
            )
             RecipientCard(
                 title = "New Muslims", 
                 desc = "Those whose hearts are inclined", 
                 iconRes = R.drawable.ic_hand_holding_heart,
                 modifier = Modifier.weight(1f)
            )
        }
        // ... Adding more rows as needed but keeping it concise for now
    }
}

@Composable
fun RecipientCard(title: String, desc: String, iconRes: Int, modifier: Modifier) {
    Card(
        shape = RoundedCornerShape(12.sdp),
        colors = CardDefaults.cardColors(containerColor = LightBlueTeal.copy(alpha = 0.05f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, LightBlueTeal.copy(alpha = 0.2f)),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.sdp)) {
            Box(
                modifier = Modifier.size(32.sdp).background(LightBlueTeal.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                 Icon(painter = painterResource(id = iconRes), contentDescription = null, tint = LightBlueTeal, modifier = Modifier.size(16.sdp))
            }
            Spacer(modifier = Modifier.height(12.sdp))
            Text(text = title, fontSize = 10.ssp, fontWeight = FontWeight.SemiBold, color = DarkBlueNavy)
            Spacer(modifier = Modifier.height(4.sdp))
            Text(text = desc, fontSize = 9.ssp, color = Color.Gray, lineHeight = 12.sp)
        }
    }
}

@Composable
fun FAQSection() {
    Column {
        Text(text = "Frequently Asked Questions", fontSize = 14.ssp, fontWeight = FontWeight.Bold, color = DarkBlueNavy)
         Text(text = "Common questions about Zakat", fontSize = 10.ssp, color = Color.Gray)
        Spacer(modifier = Modifier.height(12.sdp))
        
        FAQItem(question = "Do I pay Zakat on my home?", answer = "No, Zakat is not due on your primary residence or personal belongings used for daily living.")
        Spacer(modifier = Modifier.height(8.sdp))
        FAQItem(question = "Can I pay Zakat monthly?", answer = "Yes, you can pay Zakat in advance or in installments throughout the year.")
        Spacer(modifier = Modifier.height(8.sdp))
        FAQItem(question = "What if my wealth fluctuates?", answer = "Zakat is calculated based on your wealth on your Zakat due date.")
    }
}

@Composable
fun FAQItem(question: String, answer: String) {
    var expanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(targetValue = if (expanded) 180f else 0f)
    
    Card(
        shape = RoundedCornerShape(12.sdp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
         border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0)),
        modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded }
    ) {
        Column(modifier = Modifier.padding(12.sdp)) {
            Row(
                 modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = question, fontSize = 11.ssp, fontWeight = FontWeight.SemiBold, color = DarkBlueNavy)
                Icon(
                    painter = painterResource(id = R.drawable.ic_chevron_right), // Reuse existing chevron right but rotate
                    contentDescription = null,
                    tint = LightBlueTeal,
                     modifier = Modifier.size(12.sdp).rotate(rotation + 90f) // Chevron right needs +90 to point down
                )
            }
            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(modifier = Modifier.height(8.sdp))
                     Text(text = answer, fontSize = 10.ssp, color = Color.Gray, lineHeight = 14.sp)
                }
            }
        }
    }
}

@Composable
fun PaymentOptionsSection() {
    Card(
         shape = RoundedCornerShape(12.sdp),
         modifier = Modifier.fillMaxWidth()
    ) {
         Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        colors = listOf(LightBlueTeal, DarkBlueNavy)
                    )
                )
                .padding(16.sdp)
         ) {
             Column {
                 Text(text = "Pay Your Zakat", fontSize = 14.ssp, fontWeight = FontWeight.Bold, color = Color.White)
                 Text(text = "Choose a trusted organization to distribute your Zakat", fontSize = 10.ssp, color = Color.White.copy(alpha = 0.9f))
                 Spacer(modifier = Modifier.height(16.sdp))
                 
                 PaymentOptionButton(text = "Islamic Relief", iconRes = R.drawable.ic_hand_holding_heart)
                 Spacer(modifier = Modifier.height(8.sdp))
                 PaymentOptionButton(text = "Penny Appeal", iconRes = R.drawable.ic_handshake_angle)
                  Spacer(modifier = Modifier.height(8.sdp))
                 PaymentOptionButton(text = "Local Mosque", iconRes = R.drawable.ic_mosque)
                  Spacer(modifier = Modifier.height(8.sdp))
                 
                  Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier.fillMaxWidth().height(48.sdp),
                    shape = RoundedCornerShape(12.sdp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f)),
                     border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(painter = painterResource(id = R.drawable.ic_plus), contentDescription = null, tint = Color.White, modifier = Modifier.size(12.sdp))
                        Spacer(modifier = Modifier.width(8.sdp))
                        Text(text = "Add Custom Organization", fontSize = 11.ssp, fontWeight = FontWeight.SemiBold, color = Color.White)
                    }
                }
             }
         }
    }
}

@Composable
fun PaymentOptionButton(text: String, iconRes: Int) {
    Button(
        onClick = { /* TODO */ },
        modifier = Modifier.fillMaxWidth().height(48.sdp),
        shape = RoundedCornerShape(12.sdp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(painter = painterResource(id = iconRes), contentDescription = null, tint = LightBlueTeal, modifier = Modifier.size(14.sdp))
            Spacer(modifier = Modifier.width(8.sdp))
            Text(text = text, fontSize = 11.ssp, fontWeight = FontWeight.SemiBold, color = LightBlueTeal)
        }
    }
}

@Composable
fun ReminderSection() {
     Card(
        shape = RoundedCornerShape(12.sdp),
        colors = CardDefaults.cardColors(containerColor = LightBlueTeal.copy(alpha = 0.05f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, LightBlueTeal.copy(alpha = 0.2f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
         Row(modifier = Modifier.padding(16.sdp)) {
             Box(
                modifier = Modifier.size(32.sdp).background(LightBlueTeal.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                 Icon(painter = painterResource(id = R.drawable.ic_notifications), contentDescription = null, tint = LightBlueTeal, modifier = Modifier.size(16.sdp))
            }
             Spacer(modifier = Modifier.width(12.sdp))
             Column {
                 Text(text = "Set Zakat Reminder", fontSize = 12.ssp, fontWeight = FontWeight.Bold, color = DarkBlueNavy)
                 Spacer(modifier = Modifier.height(4.sdp))
                 Text(text = "Never miss your Zakat due date. Get reminded when it's time to calculate and pay.", fontSize = 10.ssp, color = Color.Gray, lineHeight = 14.sp)
                 Spacer(modifier = Modifier.height(12.sdp))
                 
                  Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier.fillMaxWidth().height(40.sdp),
                    shape = RoundedCornerShape(12.sdp),
                    colors = ButtonDefaults.buttonColors(containerColor = LightBlueTeal)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(painter = painterResource(id = R.drawable.ic_calendar_plus), contentDescription = null, tint = Color.White, modifier = Modifier.size(12.sdp))
                        Spacer(modifier = Modifier.width(8.sdp))
                        Text(text = "Set Annual Reminder", fontSize = 11.ssp, fontWeight = FontWeight.SemiBold, color = Color.White)
                    }
                }
             }
         }
    }
}

@Composable
fun HadithSection() {
    Card(
        shape = RoundedCornerShape(12.sdp),
        colors = CardDefaults.cardColors(containerColor = LightBlueTeal.copy(alpha = 0.05f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, LightBlueTeal.copy(alpha = 0.2f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.sdp)) {
             Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(32.sdp).background(LightBlueTeal.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(painter = painterResource(id = R.drawable.ic_quote_left), contentDescription = null, tint = LightBlueTeal, modifier = Modifier.size(16.sdp))
                }
                Spacer(modifier = Modifier.width(12.sdp))
                Text(text = "Hadith on Zakat", fontSize = 13.ssp, fontWeight = FontWeight.Bold, color = DarkBlueNavy)
            }
            Spacer(modifier = Modifier.height(12.sdp))
            
            HadithBox(text = "\"The example of those who spend their wealth in the way of Allah is like a seed of grain that sprouts seven ears...\"", source = "Quran 2:261")
            Spacer(modifier = Modifier.height(8.sdp))
            HadithBox(text = "\"Charity does not decrease wealth, no one forgives another except that Allah increases his honor...\"", source = "Sahih Muslim 2588")
        }
    }
}

@Composable
fun HadithBox(text: String, source: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.6f), RoundedCornerShape(8.sdp))
            .padding(12.sdp)
    ) {
        Text(text = text, fontSize = 11.ssp, fontStyle = FontStyle.Italic, color = DarkBlueNavy, lineHeight = 16.sp)
        Spacer(modifier = Modifier.height(4.sdp))
        Text(text = "— $source", fontSize = 10.ssp, color = Color.Gray)
    }
}

@Composable
fun FooterSection() {
     Card(
        shape = RoundedCornerShape(12.sdp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)), // bgLight
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.sdp)) {
             Row(verticalAlignment = Alignment.Top) {
                  Icon(painter = painterResource(id = R.drawable.ic_info), contentDescription = null, tint = LightBlueTeal, modifier = Modifier.size(14.sdp).padding(top = 2.dp))
                  Spacer(modifier = Modifier.width(10.sdp))
                  Column {
                      Text(text = "Important Note", fontSize = 11.ssp, fontWeight = FontWeight.Bold, color = DarkBlueNavy)
                      Spacer(modifier = Modifier.height(4.sdp))
                      Text(text = "Zakat is obligatory on wealth above Nisab that has been held for one complete lunar year (Hawl).", fontSize = 10.ssp, color = Color.Gray, lineHeight = 14.sp)
                      Spacer(modifier = Modifier.height(4.sdp))
                      Text(text = "This calculator provides an estimate. For complex situations, please consult a qualified Islamic scholar.", fontSize = 10.ssp, color = Color.Gray, lineHeight = 14.sp)
                  }
             }
             
             HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp, modifier = Modifier.padding(vertical = 12.sdp))
             
             Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Need help?", fontSize = 10.ssp, color = Color.Gray)
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { /* TODO */ }) {
                    Text(text = "Contact Scholar", fontSize = 10.ssp, fontWeight = FontWeight.SemiBold, color = LightBlueTeal)
                     Spacer(modifier = Modifier.width(4.sdp))
                     Icon(painter = painterResource(id = R.drawable.ic_arrow_right), contentDescription = null, tint = LightBlueTeal, modifier = Modifier.size(10.sdp))
                }
            }
             Spacer(modifier = Modifier.height(12.sdp))
             Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                 Text(text = "Privacy", fontSize = 10.ssp, color = Color.Gray)
                 Text(text = " • ", fontSize = 10.ssp, color = Color.Gray)
                 Text(text = "Terms", fontSize = 10.ssp, color = Color.Gray)
                 Text(text = " • ", fontSize = 10.ssp, color = Color.Gray)
                 Text(text = "About", fontSize = 10.ssp, color = Color.Gray)
             }
             Spacer(modifier = Modifier.height(8.sdp))
             Text(text = "© 2024 Salam. All rights reserved.", fontSize = 10.ssp, color = Color.Gray, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        }
    }
}

fun formatCurrency(amount: Double): String {
    return NumberFormat.getCurrencyInstance(Locale.US).format(amount)
}

fun formatPKR(amount: Double): String {
    val formatter = NumberFormat.getNumberInstance(Locale("en", "PK"))
    formatter.minimumFractionDigits = 0
    formatter.maximumFractionDigits = 0
    return "PKR ${formatter.format(amount)}"
}
