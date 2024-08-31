package com.example.codesoft.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Items(
    val title: String,
    val description: String,
    val date: Long,


    @PrimaryKey(autoGenerate = true)
    val id:Int=0,


)
