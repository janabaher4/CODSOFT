package presentation

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.codesoft.data.Items
import com.example.codesoft.data.TodoDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TodoViewModel(
    private val dao: TodoDao,
) : ViewModel() {
    private val isSortedByDate = MutableStateFlow(true)

    private var items = isSortedByDate.flatMapLatest { sort ->
        if (sort) {
            dao.getTodoOrdered()
        } else {
            dao.getTodoTitle()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val _state = MutableStateFlow(TodoState())
    val state = combine(_state, isSortedByDate, items) { state, isSortedByDate, items ->
        state.copy(
            items = items
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TodoState())


    fun onEvent(event: TodoEvents) {
        when (event) {
            is TodoEvents.DeleteTodo -> {
                viewModelScope.launch {
                    dao.deleteTodo(event.item)
                    _state.update { currentState ->
                        currentState.copy(
                            items = currentState.items.filter { it.id != event.item.id }
                        )
                    }
                }

            }


            is TodoEvents.SaveTodo -> {
                val item = Items(
                    id = event.id ?: 0,
                    title = state.value.title.value,
                    description = state.value.description.value,
                    date = System.currentTimeMillis(),


                    )
                viewModelScope.launch {
                    if (event.id != null) {
                        // Update existing todo
                        dao.updateTodo(event.id, event.title, event.description)
                    } else {
                        // Insert new todo
                        val item = Items(
                            id = 0, // ID will be generated by database
                            title = event.title,
                            description = event.description,
                            date = System.currentTimeMillis()
                        )
                        dao.upsertTodo(item)
                    }
                }



                    _state.update {
                it.copy(
                    title = mutableStateOf(""),
                    description = mutableStateOf("")
                )
            }


        }

        TodoEvents.SortTodo -> {
            isSortedByDate.value = !isSortedByDate.value
            Log.d("TodoViewModel", "Sorted by Date: ${isSortedByDate.value}")
        }
        is TodoEvents.LoadTodoById -> {
            viewModelScope.launch {
                val item = dao.getTodoById(event.id)
                Log.d("TodoViewModel", "Loaded Todo by ID: $item")
                _state.update {
                    it.copy(

                        title = mutableStateOf(item?.title ?: ""),
                        description = mutableStateOf(item?.description ?: "")
                    )
                }
            }
        }
    }
}



    private fun refreshItems() {
        viewModelScope.launch {
            val updatedItems = if (isSortedByDate.value) {
                dao.getTodoOrdered().first()
            } else {
                dao.getTodoTitle().first()
            }
            _state.update {
                it.copy(items = updatedItems)
            }
        }
    }




}