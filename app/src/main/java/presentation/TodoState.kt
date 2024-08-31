package presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.codesoft.data.Items

data class TodoState (
    val items: List<Items> = emptyList(),
    val title:MutableState<String> = mutableStateOf(""),

    val description:MutableState<String> = mutableStateOf("")
)