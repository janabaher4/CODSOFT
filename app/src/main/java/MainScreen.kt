import android.net.sip.SipSession.State
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.codesoft.DefaultBoxes
import com.example.codesoft.data.Items
import com.example.codesoft.data.formatDate
import kotlinx.coroutines.time.delay
import presentation.TodoEvents
import presentation.TodoState
import kotlin.time.Duration.Companion.seconds
import androidx.compose.material3.Text as Text

import androidx.compose.foundation.layout.PaddingValues
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    state: TodoState,
    navController: NavController,
    onEvent: (TodoEvents) -> Unit
) {

    val checkedItems = remember { mutableStateOf(setOf<Int>()) }
    val removingItems = remember { mutableStateOf(setOf<Int>()) }
    var startAnimation by remember { mutableStateOf(false) }
    val stateScroll = rememberScrollState()




    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Daily Tracker",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = FontFamily.Monospace
                    )
                },
                actions = {
                    IconButton(onClick = { onEvent(TodoEvents.SortTodo) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Sort,
                            contentDescription = "menu_icon",
                            modifier = Modifier
                                .size(24.dp)
                                .background(
                                    shape = RoundedCornerShape(20.dp),
                                    color = Color(0xFFE1BEE7)
                                )
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("AddScreen")
                },
                containerColor = Color(0xFFE1BEE7),
                shape = RoundedCornerShape(50)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Item",
                    tint = Color.Black
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = PaddingValues(start = 8.dp))


        ) {
            DefaultBoxes(state = state)
            Text(
                text = "Today's Tasks", style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                ), modifier = Modifier.padding(start = 15.dp)
            )
            if (state.items.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No tasks for today",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                    )
                }
            } else {


                LazyColumn(
                    contentPadding = PaddingValues(
                        start = 8.dp, end = 8.dp, bottom = 50.dp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 60.dp),
                    verticalArrangement = Arrangement.spacedBy(13.dp)
                ) {
                    items(state.items) { item ->
                        val isChecked = checkedItems.value.contains(item.id)
                        val isRemoving = removingItems.value.contains(item.id)

                        if (isChecked && !isRemoving) {
                            LaunchedEffect(item.id) {
                                startAnimation = true
                                delay(2000)
                                onEvent(TodoEvents.DeleteTodo(item))
                                removingItems.value = removingItems.value + item.id


                            }
                        }
                        val alpha by animateFloatAsState(
                            targetValue = if (startAnimation && isChecked && !isRemoving) 0f else 1f,
                            animationSpec = tween(durationMillis = 2000)
                        )

                        AnimatedVisibility(
                            visible = !isRemoving,
                            enter = fadeIn(),
                            exit = fadeOut(tween(durationMillis = 2000))
                        ) {
                            if (!isRemoving) {
                                NoteItem(
                                    modifier = Modifier.alpha(alpha),
                                    state = state,
                                    index = state.items.indexOf(item),
                                    onEvent = onEvent,
                                    navController = navController,
                                    checked = isChecked,
                                    onCheckedChange = { checked ->
                                        if (checked) {
                                            checkedItems.value = checkedItems.value + item.id
                                        } else {
                                            checkedItems.value = checkedItems.value - item.id
                                            removingItems.value = removingItems.value - item.id
                                        }
                                    }
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
fun NoteItem(
    modifier: Modifier,
    state: TodoState, index: Int,
    onEvent: (TodoEvents) -> Unit,
    navController: NavController,
    checked: Boolean, onCheckedChange:  (Boolean) -> Unit
) {
    val item = state.items[index]
    val date = Date(item.date)
    val formattedDate = formatDate(date)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .border(
                width = 2.dp,
                color = if (checked) Color(0xFFE1BEE7) else Color.LightGray,
                shape = RoundedCornerShape(15.dp)
            )
            .clip(RoundedCornerShape(15.dp))
            .background(if (checked) Color(0xFFE1BEE7) else Color.White)
            .padding(horizontal = 16.dp, vertical = 9.dp)
            .clickable {
                navController.navigate("UpdateScreen/${item.id}")
            }
    )  {

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "${item.description}\n$formattedDate",
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Normal,
                    color = Color.DarkGray
                )
            )
        }

        CustomCheckbox(checked = checked, onCheckedChange = onCheckedChange)


    }
}

@Composable
fun CustomCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (checked) Color(0xFFE1BEE7) else Color.White)
            .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
            .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
