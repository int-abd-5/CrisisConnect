package com.example.crisisconnect.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.crisisconnect.ui.theme.PurpleEnd
import com.example.crisisconnect.ui.theme.PurpleStart

@Composable
fun AIChatScreen(navController: NavController) {

    var userInput by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(listOf<Pair<Boolean, String>>()) } // (isUser, text)

    val typingAnim = rememberInfiniteTransition()
    val dotAlpha by typingAnim.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = androidx.compose.animation.core.tween(700, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7FB))
            .padding(16.dp),
    ) {

        Text(
            "AI Assistant",
            fontSize = 28.sp,
            color = PurpleStart,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {

            messages.forEach { (isUser, msg) ->

                val bubbleColor = if (isUser) PurpleStart else Color.White
                val textColor = if (isUser) Color.White else Color.Black

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    contentAlignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart
                ) {

                    Box(
                        modifier = Modifier
                            .shadow(8.dp, RoundedCornerShape(18.dp))
                            .background(
                                bubbleColor.copy(alpha = 0.9f),
                                RoundedCornerShape(18.dp)
                            )
                            .padding(14.dp)
                    ) {
                        Text(msg, color = textColor, fontSize = 16.sp)
                    }
                }
            }

            // Typing Indicator
            if (messages.lastOrNull()?.first == false && messages.isNotEmpty()) {
                Text(
                    "...",
                    color = Color.Gray.copy(alpha = dotAlpha),
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 8.dp)
                .shadow(8.dp, RoundedCornerShape(20.dp))
                .background(Color.White, RoundedCornerShape(20.dp))
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {

            BasicTextField(
                value = userInput,
                onValueChange = { userInput = it },
                modifier = Modifier.weight(1f),
                decorationBox = { inner ->
                    if (userInput.isEmpty()) {
                        Text("Ask something…", color = Color.Gray)
                    }
                    inner()
                }
            )

            IconButton(
                onClick = {
                    if (userInput.isNotEmpty()) {
                        messages = messages + (true to userInput)
                        messages = messages + (false to "Typing...")

                        userInput = ""
                    }
                }
            ) {
                Icon(Icons.Default.Send, "send", tint = PurpleStart)
            }
        }
    }
}
