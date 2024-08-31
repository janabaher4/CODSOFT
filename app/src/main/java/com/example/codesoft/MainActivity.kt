package com.example.codesoft

import MainScreen
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.codesoft.data.TodoDatabase
import com.example.codesoft.ui.theme.CodeSoftTheme
import presentation.TodoViewModel

class MainActivity : ComponentActivity() {

    private val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            TodoDatabase::class.java,
            "todo.db"
        ).build()
    }

    private val viewModel by viewModels<TodoViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return TodoViewModel(database.dao) as T
                }
            }
        }
    )


    @SuppressLint("RememberReturnType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CodeSoftTheme {
                Surface {
                    val state by viewModel.state.collectAsState()
                    val navController = rememberNavController()


                    NavHost(navController = navController, startDestination = "MainScreen") {
                        composable("MainScreen") {
                            MainScreen(
                                state = state,
                                navController = navController,
                                onEvent = viewModel::onEvent
                            )

                        }
                        composable("AddScreen") {
                            val itemId : Int
                            AddScreen(
                                state = state,
                                navController = navController,
                                onEvent = viewModel::onEvent,

                            )

                        }
                        composable("UpdateScreen/{itemId}") { backStackEntry ->
                            val itemId = backStackEntry.arguments?.getString("itemId")?.toIntOrNull()
                            UpdateScreen(
                                viewModel = viewModel,
                                navController = navController,
                                itemId = itemId
                            )

                        }


                    }
                }
            }
        }
    }
}

