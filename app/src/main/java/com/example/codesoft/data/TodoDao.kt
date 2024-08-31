package com.example.codesoft.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTodo(items: Items)

    @Delete
    suspend fun deleteTodo(items: Items)

    @Query("UPDATE items SET title = :title, description = :description WHERE id = :id ")
    suspend fun updateTodo(id: Int, title: String, description: String)


    @Query("SELECT * FROM items")
    fun getAllItems(): Flow<List<Items>>

    @Query("SELECT * FROM items ORDER BY date")
    fun getTodoOrdered(): Flow<List<Items>>

    @Query("SELECT * FROM items ORDER BY title")
    fun getTodoTitle(): Flow<List<Items>>

    @Query("SELECT * FROM items WHERE id = :id")
    suspend fun getTodoById(id: Int): Items?







}