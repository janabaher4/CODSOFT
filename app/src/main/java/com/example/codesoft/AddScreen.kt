package com.example.codesoft

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.AlertDialog

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composer.Companion.Empty
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.NavController
import presentation.TodoEvents
import presentation.TodoState
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    state: TodoState,
    navController: NavController,
    onEvent: (TodoEvents) -> Unit,

) {
    var showErrorDialog by remember { mutableStateOf(false) }
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            confirmButton = {
                TextButton(onClick = { showErrorDialog = false }) {
                    Text("OK")
                }
            },
            title = { Text(text = "Error") },
            text = { Text("Title cannot be empty. Please enter a title.") }
        )

    }
    val title = state.title.value
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Create New Task",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Serif
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("MainScreen") }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                })
        },


        floatingActionButton = {
            FloatingActionButton(onClick = {
                if(title.isNotBlank()){ onEvent(
                    TodoEvents.SaveTodo(
                        id = null,
                        title = state.title.value,
                        description = state.description.value
                    )

                )
                    navController.popBackStack()} else {
                    showErrorDialog = true

                }



            }) {

                Icon(imageVector = Icons.Rounded.Check, contentDescription = "SAVE ICON")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(10.dp)
        ) {
            Text(
                text = "Task Title",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 4.dp, start = 10.dp)
            )

            OutlinedTextField(
                value = state.title.value,
                onValueChange = {
                    state.title.value = it
                },
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFE1BEE7),
                    unfocusedContainerColor = Color.Transparent,
                    focusedBorderColor = Color(0xFFE1BEE7)

                ),
                textStyle = TextStyle(
                    fontSize = 16.sp, fontWeight = FontWeight.Normal
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Description",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 4.dp, start = 10.dp),
            )

            OutlinedTextField(
                value = state.description.value,
                onValueChange = {
                    state.description.value = it
                },
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .size(150.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFE1BEE7),
                    unfocusedContainerColor = Color.Transparent,
                    focusedBorderColor = Color(0xFFE1BEE7)

                ),
                textStyle = TextStyle(
                    fontSize = 16.sp, fontWeight = FontWeight.Normal
                )

                )


        }


    }

}