package com.example.todoapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.core.ChangeType
import com.example.todoapp.core.Importance
import com.example.todoapp.data.repository.TodoItem


class TodoListAdapter(
    private val onInfoClick : (todoItem: TodoItem)->Unit,
    private val checkToggle : (item : TodoItem)->Unit,
    private val checkCompleted : (isCompleted : Boolean, importance : Importance, title : TextView, checkBox : ToggleButton) -> Unit,
    private val onBlockItemClick : () -> Unit) :
    ListAdapter<TodoItem, RecyclerView.ViewHolder>(
        callback
    ) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == 0)
                TodoItemVH (inflater.inflate(R.layout.todo_item_vh, parent, false))
        else BlockVH(inflater.inflate(R.layout.extra_label_vh, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == 0) {
            (holder as TodoItemVH).onBind(getItem(position))
        } else {
            (holder as BlockVH).obBind()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position != (itemCount-1)) {
            0
        } else {
            1
        }
    }

    inner class TodoItemVH(view: View) : RecyclerView.ViewHolder(view) {
        private val title = view.findViewById<TextView>(R.id.todoItem)
        private val infoBtn = view.findViewById<ImageView>(R.id.info_btn)
        private val img = view.findViewById<ImageView>(R.id.importance_img)
        private val checkBox = view.findViewById<ToggleButton>(R.id.checkbox)

        private val date = view.findViewById<TextView>(R.id.date_text)

        fun onBind(item: TodoItem) {
            title.text = item.text

            infoBtn.setOnClickListener {
                onInfoClick.invoke(item)
            }

            title.setOnClickListener {
                checkToggle.invoke(item)
            }

            checkBox.setOnCheckedChangeListener { _, _ ->
                checkToggle.invoke(item)
            }

            if (item.deadLine!=null){
                date.visibility = View.VISIBLE
                date.text = ChangeType.changeDateFormat(item.deadLine!!)
            }  else {
                date.visibility = View.GONE
            }

            when(item.importance){
                Importance.NORMAL -> img.setImageDrawable(null)
                Importance.LOW -> img.setImageResource(R.drawable.arrow_down)
                Importance.URGENT -> img.setImageResource(R.drawable.importance)
            }

            checkCompleted.invoke(item.isCompleted, item.importance, title, checkBox)

        }
    }

    inner class BlockVH(view: View) : RecyclerView.ViewHolder(view) {
        private val root = view.rootView
        fun obBind(){
            root.setOnClickListener {
                onBlockItemClick.invoke()
            }
        }
    }

    override fun getItemCount(): Int = super.getItemCount() + 1

}

val callback = object : DiffUtil.ItemCallback<TodoItem>() {

    override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean =
        oldItem == newItem
}