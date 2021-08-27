package com.example.todo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.databinding.LayoutCardViewBinding
import com.example.todo.model.Priority
import com.example.todo.model.Task
import com.example.todo.util.Utils

class TaskAdapter(private val listener: OnClickItem, val context: Context) :
    RecyclerView.Adapter<TaskAdapter.TaskHolder>() {

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
            LayoutInflater.from(parent.context).inflate(R.layout.layout_card_view, parent, false)
        return TaskHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        val task = differ.currentList[position]
        val formatDateDay = Utils.formatDay(task.dueDate)
        val formatDateDayNum = Utils.formatDayNum(task.dueDate)
        val formatDateMonth = Utils.formatDayMonth(task.dueDate)
        val formatTime = Utils.formatTime(task.dueDate)

        holder.binding.apply {
            tvTitleTask.text = task.title
            tvTime.text = formatTime
            tvDateDay.text = formatDateDay
            tvDateDayNumber.text = formatDateDayNum
            tvDateMonth.text = formatDateMonth

            getPriority(task.priority, this)
            cardView.setOnLongClickListener {
                listener.longClickItem(task)
                true
            }
            btnDone.setOnClickListener { listener.clickCheckTask(task.taskId) }
        }

    }

    private fun getPriority(priority: Priority, view: LayoutCardViewBinding) {
        when (priority) {
            Priority.LOW -> view.tvPraiorty.text = "LOW"
            Priority.MEDIUM ->  view.tvPraiorty.text = "MEDIUM"
            Priority.HIGH ->  view.tvPraiorty.text = "HIGH"
        }
    }


    override fun getItemCount() = differ.currentList.size

    inner class TaskHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = LayoutCardViewBinding.bind(view)
    }
}