package com.example.todo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.databinding.LayoutCardViewHoresanlBinding
import com.example.todo.model.Task
import com.example.todo.util.Utils

class TaskAdapter2(private val listener: OnClickItem, val context: Context) :
    RecyclerView.Adapter<TaskAdapter2.TaskHolder>() {

    private val differCallBack = object : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return (oldItem.title == newItem.title)
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_card_view_horesanl, parent, false)
        return TaskHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        val task = differ.currentList[position]
        val formatTime = Utils.formatTime(task.dueDate)

        holder.binding.apply {
            tvTitleTask.text = task.title
            tvTime.text = formatTime
            cardView.setOnLongClickListener {
                listener.longClickItem(task)
                true
            }
            btnDone.setOnClickListener { listener.clickBackTask(task.taskId) }
        }

    }

    override fun getItemCount() = differ.currentList.size

    inner class TaskHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = LayoutCardViewHoresanlBinding.bind(view)
    }
}