package com.example.codesoft

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Delete
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import presentation.TodoEvents
import presentation.TodoState
import presentation.TodoViewModel
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.StateFlow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateScreen(
    viewModel: TodoViewModel,
    navController: NavController,
    itemId: Int?,

    ) {


    val state by viewModel.state.collectAsState()
    val titleState = remember { mutableStateOf(state.title.value) }
    val descriptionState = remember { mutableStateOf(state.description.value) }


    LaunchedEffect(itemId) {
        itemId?.let { viewModel.onEvent(TodoEvents.LoadTodoById(it)) }
    }



    LaunchedEffect(state) {

        if (itemId != null) {
            titleState.value = state.items.find { it.id == itemId }?.title ?: ""
            descriptionState.value = state.items.find { it.id == itemId }?.description ?: ""
        }
    }


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

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Update Task",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Serif
                    )
                }, actions = {
                    itemId?.let { id ->
                        IconButton(onClick = {
                            viewModel.onEvent(TodoEvents.DeleteTodo(state.items.first { it.id == id }))
                            navController.popBackStack()
                        }) {

                            Icon(
                                imageVector = Icons.Default.DeleteForever,
                                contentDescription = "delete task",
                                modifier = Modifier.size(25.dp),
                                tint = Color.DarkGray
                            )
                        }
                    }},
                    navigationIcon = {
                        IconButton(onClick = { navController.navigate("MainScreen") }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    )
                },

                floatingActionButton = {
                    FloatingActionButton(onClick = {

                        if (titleState.value.isNotBlank()) {
                            viewModel.onEvent(
                                TodoEvents.SaveTodo(
                                    id = itemId,
                                    title = titleState.value,
                                    description = descriptionState.value
                                )
                            )
                            navController.popBackStack()
                        } else {
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
                        value = titleState.value,
                        onValueChange = {
                            titleState.value = it
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
                            fontSize = 16.sp, fontWeight = FontWeight.SemiBold
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
                        value = descriptionState.value,
                        onValueChange = {
                            descriptionState.value = it
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

                        )


                }


            }

        }

