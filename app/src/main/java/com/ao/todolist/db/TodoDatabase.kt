package com.ao.todolist.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ao.todolist.utils.Constants.NAME_DATA_todo_db


@Database(entities = [TodoRecord::class], version = 3, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {

    abstract fun todoDao(): TodoDao

    companion object {
        private var INSTANCE: TodoDatabase? = null

        fun getInstance(context: Context): TodoDatabase? {
            if (INSTANCE == null) {
                synchronized(TodoDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context,
                            TodoDatabase::class.java,
                            NAME_DATA_todo_db)
                            .build()
                }
            }
            return INSTANCE
        }
    }
}