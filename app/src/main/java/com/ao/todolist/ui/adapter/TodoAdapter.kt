package com.ao.todolist.ui.adapter

import android.sax.TextElementListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.ao.todolist.R
import com.ao.todolist.db.TodoRecord
import kotlinx.android.synthetic.main.todo_item.view.*

class TodoAdapter (todoEvents : TodoEvents) : RecyclerView.Adapter<TodoAdapter.ViewHolder>(),Filterable {

    private var todoList: List<TodoRecord> = arrayListOf()
    private var filteredTodoList: List<TodoRecord> = arrayListOf()
    private val listener: TodoEvents = todoEvents

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false))

    override fun getItemCount(): Int = filteredTodoList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filteredTodoList[position],listener)

     }

    fun setAllTodoItems(todoItems: List<TodoRecord>) {
        this.todoList = todoItems
        this.filteredTodoList = todoItems
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
         return object : Filter(){
              override fun performFiltering(constraint: CharSequence?): FilterResults {
                  val charString = constraint.toString()
                  filteredTodoList = if (charString.isEmpty()) {
                      todoList
                  } else {
                      val filteredList = arrayListOf<TodoRecord>()
                      for (row in todoList) {
                          if (row.title.toLowerCase().contains(charString.toLowerCase())
                              || row.content.contains(charString.toLowerCase())) {
                              filteredList.add(row)
                          }
                      }
                      filteredList
                  }

                  val filterResults = FilterResults()
                  filterResults.values = filteredTodoList
                  return filterResults


              }

              override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                  filteredTodoList = results?.values as List<TodoRecord>
                  notifyDataSetChanged()


              }

         }


     }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(todo : TodoRecord,listener : TodoEvents )  {
            itemView.tv_item_title.text = todo.title
            itemView.tv_item_content.text = todo.content
            itemView.iv_item_delete.setOnClickListener{
                listener.onDeleteClicked(todo)
            }
            itemView.setOnClickListener{
                listener.onViewClicked(todo)
            }


        }
    }
}