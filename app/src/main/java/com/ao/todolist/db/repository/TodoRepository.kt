package com.ao.todolist.db.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.ao.todolist.db.TodoDao
import com.ao.todolist.db.TodoDatabase
import com.ao.todolist.db.TodoRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class TodoRepository (application: Application) {

    private val todoDao : TodoDao
    private val allTodo : LiveData<List<TodoRecord>>

    init {
        val database = TodoDatabase.getInstance(application.applicationContext)
        todoDao = database!!.todoDao()
        allTodo = todoDao.getAllTodoList()
    }

    fun saveTodo(todo: TodoRecord) = runBlocking {
        this.launch(Dispatchers.IO) {
            todoDao.saveTodo(todo)
        }
    }
    fun updateTodo(todo:TodoRecord) = runBlocking {
        this.launch(Dispatchers.IO){
            todoDao.updateTodo(todo)
        }
    }
       fun deleteTodo(todo:TodoRecord) = runBlocking {
        this.launch(Dispatchers.IO){
            todoDao.deleteTodo(todo)
        }
    }

    fun getAllTodoList():LiveData<List<TodoRecord>>{
        return allTodo
    }




}