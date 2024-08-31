package com.example.codesoft

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.codesoft.data.formatDate
import com.example.codesoft.data.formatDayOfWeek
import com.example.codesoft.data.formatTime
import com.example.codesoft.data.formatday
import presentation.TodoState
import java.util.Date
@Composable
fun DefaultBoxes(state: TodoState) {
    val currentTime = System.currentTimeMillis()
    val date = Date(currentTime)
    val formattedDate = formatDayOfWeek(date)
    val formattedDay = formatday(date)
    val formattedTime = formatTime(date)

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .padding(top = 80.dp)
    ) {
        // First box
        Box(
            modifier = Modifier
                .size(width = 130.dp, height = 100.dp)
                .weight(1.5f) // Equally distribute space between boxes
                .aspectRatio(1.5f) // Maintain the aspect ratio
                .background(
                    color = Color(0xFFE1BEE7),
                    shape = RoundedCornerShape(15.dp)
                )
                .padding(10.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                formattedDate.let {
                    Text(
                        text = it,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                formattedDay.let {
                    Text(
                        text = it,
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Normal,
                            color = Color.DarkGray
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(16.dp)) // Space between boxes

        // Second box
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1.5f)
                .size(width = 130.dp, height = 100.dp)
                .aspectRatio(1.5f) // Maintain the aspect ratio
                .background(
                    color = Color(0xFFE1BEE7),
                    shape = RoundedCornerShape(15.dp)
                )
                .padding(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Filled.WbSunny, contentDescription = "sun icon")

                Spacer(modifier = Modifier.width(8.dp))

                formattedTime.let {
                    Text(
                        text = it,
                        fontWeight = FontWeight.Normal,
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    )
                }
            }
        }
    }
}
