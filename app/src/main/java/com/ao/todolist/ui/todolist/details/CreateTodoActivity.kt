package com.ao.todolist.ui.todolist.details

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.ao.todolist.R
import com.ao.todolist.db.TodoRecord
import com.ao.todolist.utils.Constants
import kotlinx.android.synthetic.main.activity_create_todo.*

class CreateTodoActivity : AppCompatActivity() {
    var todoRecord: TodoRecord? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_todo)
        val intent = intent
        if (intent != null && intent.hasExtra(Constants.INTENT_OBJECT)) {
            val todoRecord: TodoRecord = intent.getParcelableExtra(Constants.INTENT_OBJECT)
            this.todoRecord = todoRecord
            prePopulateData(todoRecord)
        }

        title = if (todoRecord != null) getString(R.string.viewOrEditTodo) else getString(R.string.createTodo)
    }
    private fun prePopulateData(todoRecord: TodoRecord) {
        et_todo_title.setText(todoRecord.title)
        et_todo_content.setText(todoRecord.content)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflate = menuInflater
        menuInflate.inflate(R.menu.menus_save, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.save_todo -> {
                saveTodo()
            }
        }
        return true
    }
    private fun saveTodo() {
        if (validateFields()) {
            val id = if (todoRecord != null) todoRecord?.id else null
            val todo = TodoRecord(id = id, title = et_todo_title.text.toString(), content = et_todo_content.text.toString())
            val intent = Intent()
            intent.putExtra(Constants.INTENT_OBJECT, todo)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun validateFields(): Boolean {
        if (et_todo_title.text.isEmpty()) {
            til_todo_title.error = getString(R.string.pleaseEnterTitle)
            et_todo_title.requestFocus()
            return false
        }
        if (et_todo_content.text.isEmpty()) {
            til_todo_content.error = getString(R.string.pleaseEnterContent)
            et_todo_content.requestFocus()
            return false
        }
        return true
    }
}