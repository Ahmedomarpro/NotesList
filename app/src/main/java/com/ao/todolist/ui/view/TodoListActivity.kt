package com.ao.todolist.ui.view

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.view.menu.MenuView
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.ao.todolist.R
import com.ao.todolist.db.TodoRecord
import com.ao.todolist.ui.adapter.TodoAdapter
import com.ao.todolist.ui.adapter.TodoEvents
import com.ao.todolist.ui.todolist.details.CreateTodoActivity
import com.ao.todolist.ui.viewmodel.TodoViewModel
import com.ao.todolist.utils.Constants
import com.ao.todolist.utils.Constants.FIRST_START
import com.ao.todolist.utils.Constants.NIGHT_MODE
import com.ao.todolist.utils.Constants.PREF
import kotlinx.android.synthetic.main.activity_todo_list.*
import kotlinx.android.synthetic.main.content_main.*


class TodoListActivity : AppCompatActivity(), TodoEvents {
    private lateinit var todoViewModel: TodoViewModel
    private lateinit var searchView: SearchView
    private lateinit var todoAdapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_list)
        getSharedNihtmode()
        getCreateTodo()
        rv_todo_list.layoutManager = LinearLayoutManager(this)
        todoAdapter = TodoAdapter(this)
        rv_todo_list.adapter = todoAdapter

        //Setting up ViewModel and LiveData
        todoViewModel = ViewModelProviders.of(this).get(TodoViewModel::class.java)
        todoViewModel.getAllTodoList().observe(this, Observer {
            todoAdapter.setAllTodoItems(it)
        })


    }


    fun getSharedNihtmode() {
        val appSettingsPrefs: SharedPreferences = getSharedPreferences(PREF, 0)
        val isNightModeOn: Boolean = appSettingsPrefs.getBoolean(NIGHT_MODE, true)
        val isFirstStart: Boolean = appSettingsPrefs.getBoolean(FIRST_START, false)
        val editor: SharedPreferences.Editor = appSettingsPrefs.edit()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && isFirstStart) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        } else {
            when {
                isNightModeOn -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                else -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }
        btnSwitch.setOnClickListener {
            if (isNightModeOn) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor.putBoolean(FIRST_START, false)
                editor.putBoolean(NIGHT_MODE, false)
                editor.apply()
                //recreate activity to make changes visible
                recreate()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor.putBoolean(FIRST_START, false)
                editor.putBoolean(NIGHT_MODE, true)
                editor.apply()
                recreate()

            }
        }

    }



    fun getCreateTodo() {
        //FAB click listener
        fab_new_todo.setOnClickListener {
            resetSearchView()
            val intent = Intent(this@TodoListActivity, CreateTodoActivity::class.java)
            startActivityForResult(intent, Constants.INTENT_CREATE_TODO)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val todoRecord = data?.getParcelableExtra<TodoRecord>(Constants.INTENT_OBJECT)!!
            when (requestCode) {
                Constants.INTENT_CREATE_TODO -> {
                    todoViewModel.saveTodo(todoRecord)
                }
                Constants.INTENT_UPDATE_TODO -> {
                    todoViewModel.updateTodo(todoRecord)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu?.findItem(R.id.search_todo)
            ?.actionView as SearchView
        searchView.setSearchableInfo(
            searchManager
                .getSearchableInfo(componentName)
        )
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                todoAdapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                todoAdapter.filter.filter(newText)
                return false
            }

        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.search_todo -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        resetSearchView()
        super.onBackPressed()
    }

    private fun resetSearchView() {
        if (!searchView.isIconified) {
            searchView.isIconified = true
            return
        }
    }


  
    override fun onDeleteClicked(todoRecord: TodoRecord) {
        todoViewModel.deleteTodo(todoRecord)
    }
    override fun onViewClicked(todoRecord: TodoRecord) {
        resetSearchView()

        val intent = Intent(this@TodoListActivity, CreateTodoActivity::class.java)
        intent.putExtra(Constants.INTENT_OBJECT, todoRecord)
        startActivityForResult(intent, Constants.INTENT_UPDATE_TODO)


    }


}
