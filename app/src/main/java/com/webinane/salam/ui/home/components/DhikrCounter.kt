package com.webinane.salam.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.font.FontWeight
import com.webinane.salam.ui.theme.DarkBlueNavy
import com.webinane.salam.ui.theme.LightBlueTeal
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import org.koin.androidx.compose.koinViewModel
import com.webinane.salam.ui.viewmodel.DhikrViewModel

@Composable
fun DhikrCounter(
    viewModel: DhikrViewModel = koinViewModel()
) {
    val count by viewModel.count.collectAsState()
    val totalCount by viewModel.totalCount.collectAsState()
    
    var showDialog by remember { mutableStateOf(false) }
    var customInput by remember { mutableStateOf("") }
    
    val targetOptions = viewModel.targetOptions
    Column(modifier = Modifier.padding(16.sdp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.sdp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Daily Dhikr Counter",
                fontSize = 14.ssp,
                fontWeight = FontWeight.Bold,
                color = LightBlueTeal
            )
            
            // Target Selection Chips
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.sdp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                targetOptions.forEach { target ->
                    Surface(
                        onClick = { 
                            viewModel.setTotalCount(target)
                        },
                        shape = RoundedCornerShape(16.sdp),
                        color = if (totalCount == target) LightBlueTeal else Color.White.copy(alpha = 0.1f),
                        modifier = Modifier.height(24.sdp)
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = 8.sdp)) {
                            Text(
                                text = target.toString(),
                                fontSize = 10.ssp,
                                fontWeight = FontWeight.Bold,
                                color = if (totalCount == target) Color.White else LightBlueTeal
                            )
                        }
                    }
                }
                
                // Pencil Icon for Custom Count
                IconButton(
                    onClick = { 
                        customInput = totalCount.toString()
                        showDialog = true 
                    },
                    modifier = Modifier.size(24.sdp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Custom Count",
                        tint = LightBlueTeal,
                        modifier = Modifier.size(14.sdp)
                    )
                }
            }
        }

        // Custom Target Dialog
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Set Custom Target", fontSize = 16.ssp) },
                text = {
                    OutlinedTextField(
                        value = customInput,
                        onValueChange = { if (it.length <= 4) customInput = it.filter { char -> char.isDigit() } },
                        label = { Text("Enter target") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        val newTarget = customInput.toIntOrNull() ?: 33
                        if (newTarget > 0) {
                            viewModel.setTotalCount(newTarget)
                        }
                        showDialog = false
                    }) {
                        Text("Save", color = LightBlueTeal)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancel", color = Color.Gray)
                    }
                },
                containerColor = Color.White,
                shape = RoundedCornerShape(16.sdp)
            )
        }

        Card(
            shape = RoundedCornerShape(12.sdp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.sdp),
            colors = CardDefaults.cardColors(containerColor = DarkBlueNavy),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.sdp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Reset Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = { viewModel.resetCount() },
                        modifier = Modifier.size(28.sdp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reset Count",
                            tint = Color.White.copy(alpha = 0.6f),
                            modifier = Modifier.size(16.sdp)
                        )
                    }
                }

                Text(text = "SubhanAllah", color = Color.White.copy(alpha = 0.9f), fontSize = 12.ssp)
                Text(text = "$count", color = Color.White, fontSize = 48.ssp, fontWeight = FontWeight.Bold)
                Text(text = "of $totalCount", color = Color.White.copy(alpha = 0.7f), fontSize = 10.ssp)

                Spacer(modifier = Modifier.height(16.sdp))

                // Custom Flawless Progress Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.sdp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)) // Light background color
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(if (count > 0) count.toFloat() / totalCount else 0f)
                            .fillMaxHeight()
                            .background(Color.White) // Active progress color
                    )
                }

                Spacer(modifier = Modifier.height(20.sdp))

                Button(
                    onClick = { viewModel.incrementCount() },
                    modifier = Modifier.fillMaxWidth().height(45.sdp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.sdp)
                ) {
                    Text(text = "Tap to Count", color = DarkBlueNavy, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
