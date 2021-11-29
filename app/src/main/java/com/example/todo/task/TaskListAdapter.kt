package com.example.todo.task

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.form.FormActivity
import java.util.*

object TasksDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        // are they the same "entity" ? (usually same id)
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        // do they have the same data ? (content)
        return oldItem == newItem
    }

}

class TaskListAdapter() : ListAdapter<Task, TaskListAdapter.TaskViewHolder>(TasksDiffCallback) {

    var onClickDelete: (Task) -> Unit = {}
    var onClickEdit: (Task) -> Unit = {}

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val text: TextView = itemView.findViewById(R.id.task_title)
            private val btnEdit: ImageView = itemView.findViewById(R.id.editButton)
            private val btnDelete: ImageView = itemView.findViewById(R.id.deleteButton)

        fun bind(task: Task) {
            val str: String = task.title + "\n" + task.description + "\n"
            text.text = str

            btnEdit.setOnClickListener{
                onClickEdit(task)
            }
            btnDelete.setOnClickListener{
                onClickDelete(task)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(currentList[position])
    }


}
