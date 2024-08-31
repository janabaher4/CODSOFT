package presentation

import com.example.codesoft.data.Items

sealed interface TodoEvents {
    object SortTodo:TodoEvents

    data class DeleteTodo(val item:Items):TodoEvents

    data class SaveTodo(
        val id: Int? ,
        val title:String,
        val description:String
    ):TodoEvents


    data class LoadTodoById(val id: Int) : TodoEvents

}