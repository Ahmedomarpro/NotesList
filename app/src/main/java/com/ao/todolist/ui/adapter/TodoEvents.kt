package com.ao.todolist.ui.adapter

import com.ao.todolist.db.TodoRecord

interface TodoEvents {

    fun onDeleteClicked(todoRecord: TodoRecord)
    fun onViewClicked(todoRecord: TodoRecord)

}