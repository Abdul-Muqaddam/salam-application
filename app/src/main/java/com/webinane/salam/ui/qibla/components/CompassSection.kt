package com.webinane.salam.ui.qibla.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.webinane.salam.R
import com.webinane.salam.ui.theme.DarkBlueNavy
import com.webinane.salam.ui.theme.LightBlueTeal
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CompassSection(
    qiblaDirection: Double = 0.0,
    currentHeading: Double = 0.0
) {
    // We want the Dail to rotate with the phone, so North matches real North.
    // If azimuth (heading) is 90 (East), we rotate dial by -90 so "N" is on the left.
    // The Qibla Pointer should ideally point to the Geographic Qibla direction.
    // If we rotate the whole dial by -heading:
    // The Qibla pointer needs to form the angle relative to North.
    // So Qibla pointer rotation relative to dial = qiblaDirection.
    // Total visual separation = qiblaDirection.
    // So pointer rotation inside the rotated box = qiblaDirection.
    
    // HOWEVER: Android Canvas rotation is additive if nested? No, if we rotate the parent box.
    // Let's Rotate the DIAL container by -heading.
    // The Qibla Pointer inside will rotate with it.
    // Wait.
    // Dial: "N" is at Top (0 deg).
    // If I face East (90), "N" should point Left (-90 screen deg). 
    // So Dial Rotation = -heading.
    // Qibla: 122 deg East of North.
    // It should be fixed at 122 deg ON THE DIAL.
    // So if Dial rotates, Qibla pointer rotates with it.
    // Example: Face North (0). Dial rot 0. Qibla points 122. Correct.
    // Face East (90). Dial rot -90. "N" is at Left. Qibla pointer (fixed at 122 on dial) moves to 122-90 = 32 deg. Correct.
    
    val dialRotation = -currentHeading.toFloat()

    Card(
        shape = RoundedCornerShape(16.sdp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.sdp)
    ) {
        Column(
            modifier = Modifier
                .background(LightBlueTeal.copy(alpha = 0.05f))
                .padding(20.sdp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Qibla Direction",
                fontSize = 14.ssp,
                fontWeight = FontWeight.Bold,
                color = DarkBlueNavy
            )
            Spacer(modifier = Modifier.height(4.sdp))
            Text(
                text = "Align your device with the arrow",
                fontSize = 10.ssp,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(20.sdp))
            
            // Detailed Compass Visual
            Box(
                modifier = Modifier.size(260.sdp),
                contentAlignment = Alignment.Center
            ) {
                // Rotatable Dial Assembly
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .rotate(dialRotation),
                    contentAlignment = Alignment.Center
                ) {
                    // Background Dial with Ticks
                    CompassDial(modifier = Modifier.fillMaxSize())
                    
                    // Cardinal Points Labels
                    CardinalPoints(modifier = Modifier.fillMaxSize())

                    // Qibla Pointer
                    QiblaPointer(
                        modifier = Modifier.size(200.sdp),
                        rotation = qiblaDirection.toFloat()
                    )
                }
                
                // Center Mosque Icon (Fixed orientation relative to screen)
                Box(
                    modifier = Modifier
                        .size(60.sdp)
                        .background(LightBlueTeal, CircleShape)
                        .padding(12.sdp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_mosque),
                        contentDescription = "Kaaba",
                        tint = Color.White,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            
            // Sensor Error Message
            if (currentHeading == 0.0) {
                 Spacer(modifier = Modifier.height(8.sdp))
                 Text(
                     text = "Device sensors not detected. Live compass unavailable.",
                     fontSize = 10.ssp,
                     color = Color.Red,
                     fontWeight = FontWeight.Medium
                 )
            }
            
            Spacer(modifier = Modifier.height(12.sdp))
            
            // Direction Info Card
            // ... (keep as is)
            Card(
                shape = RoundedCornerShape(12.sdp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(12.sdp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.sdp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_place),
                            contentDescription = null,
                            tint = LightBlueTeal,
                            modifier = Modifier.size(16.sdp)
                        )
                        Text(
                            text = "Direction:",
                            fontSize = 12.ssp,
                            fontWeight = FontWeight.SemiBold,
                            color = DarkBlueNavy
                        )
                        Text(
                            text = "%.1f°".format(qiblaDirection),
                            fontSize = 20.ssp,
                            fontWeight = FontWeight.Bold,
                            color = LightBlueTeal
                        )
                    }
                    Spacer(modifier = Modifier.height(4.sdp))
                    Text(
                        text = "Bearing from North",
                        fontSize = 10.ssp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun CompassDial(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val radius = size.minDimension / 2
        val center = center
        
        // Draw Outer Rim Shadow/Gradient Effect
        drawCircle(
            color = LightBlueTeal.copy(alpha = 0.1f),
            radius = radius,
            center = center
        )
        
        // Draw Inner Dial Background
        drawCircle(
            color = Color.White,
            radius = radius * 0.85f,
            center = center
        )
        
        // Rim Border
        drawCircle(
            color = Color.Gray.copy(alpha = 0.1f),
            radius = radius * 0.85f,
            center = center,
            style = Stroke(width = 1.dp.toPx())
        )

        // Draw Ticks
        val tickRadius = radius * 0.82f // Ticks end here
        val tickLengthMajor = 12.dp.toPx()
        val tickLengthMinor = 6.dp.toPx()
        
        for (i in 0 until 360 step 5) { // 72 ticks
            val angleRad = Math.toRadians(i.toDouble() - 90)
            val isMajor = i % 30 == 0
            val isCardinal = i % 90 == 0
            
            val tickLen = if (isMajor) tickLengthMajor else tickLengthMinor
            
            // Start point (closer to center)
            val start = Offset(
                x = (center.x + (tickRadius - tickLen) * cos(angleRad)).toFloat(),
                y = (center.y + (tickRadius - tickLen) * sin(angleRad)).toFloat()
            )
            
            // End point (at tick circle)
            val end = Offset(
                x = (center.x + tickRadius * cos(angleRad)).toFloat(),
                y = (center.y + tickRadius * sin(angleRad)).toFloat()
            )
            
            val color = if (isCardinal) DarkBlueNavy else if (isMajor) Color.Gray else Color.LightGray
            val strokeWidth = if (isCardinal) 2.dp.toPx() else if (isMajor) 1.5.dp.toPx() else 1.dp.toPx()
            
            drawLine(
                color = color,
                start = start,
                end = end,
                strokeWidth = strokeWidth
            )
        }
    }
}

@Composable
fun CardinalPoints(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        // We position N, E, S, W manually or using alignment
        // Ideally we calculate offset. But Alignment is easier.
        // Padding allows us to push them in from the edge
        
        // North
        Text(
            text = "N",
            color = Color(0xFFD32F2F), // Red North
            fontSize = 14.ssp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 24.sdp)
        )
        
        // South
        Text(
            text = "S",
            color = DarkBlueNavy,
            fontSize = 14.ssp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 24.sdp)
        )
        
        // West
        Text(
            text = "W",
            color = DarkBlueNavy,
            fontSize = 14.ssp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterStart).padding(start = 24.sdp)
        )
        
        // East
        Text(
            text = "E",
            color = DarkBlueNavy,
            fontSize = 14.ssp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterEnd).padding(end = 24.sdp)
        )
    }
}

@Composable
fun QiblaPointer(modifier: Modifier = Modifier, rotation: Float) {
    // This is the needle that points to Qibla
    Canvas(modifier = modifier.rotate(rotation)) {
        val center = center
        val needleLength = size.minDimension / 2 * 0.9f 
        val baseWidth = 14.dp.toPx()
        
        val path = Path().apply {
            moveTo(center.x, center.y - needleLength) // Tip
            lineTo(center.x + baseWidth/2, center.y) // Right Base
            lineTo(center.x, center.y + baseWidth/4) // Inner notch
            lineTo(center.x - baseWidth/2, center.y) // Left Base
            close()
        }
        
        drawPath(
            path = path,
            color = LightBlueTeal, // Needle Color
        )
        
        // Draw a small circle at the base
        drawCircle(
            color = LightBlueTeal,
            radius = baseWidth/3,
            center = center
        )
    }
}
