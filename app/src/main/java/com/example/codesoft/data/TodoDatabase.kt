package com.example.codesoft.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Items::class],
    version = 1)
abstract class TodoDatabase:RoomDatabase() {

    abstract val dao:TodoDao
}