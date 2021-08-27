package com.example.todo.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.todo.R
import com.example.todo.data.Repository
import com.example.todo.data.TaskDatabase
import com.example.todo.databinding.ActivityAddBinding
import com.example.todo.model.Priority
import com.example.todo.model.Task
import com.example.todo.util.Constant
import java.util.*

class UpdateActivity : AppCompatActivity() {

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var task: Task
    private lateinit var dueDate: Date
    private var calendar: Calendar = Calendar.getInstance()
    private lateinit var binding: ActivityAddBinding
    private lateinit var priority: Priority
    private var idTask :Long =  0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TODO)
        Thread.sleep(1000L)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dueDate = Date()
        priority = Priority.LOW

        val repository = Repository(TaskDatabase.invoke(this))
        val taskViewModelProvider = TaskViewModelProvider(repository)
        taskViewModel = ViewModelProvider(this, taskViewModelProvider).get(TaskViewModel::class.java)

        binding.btnSave.setOnClickListener {
            if (binding.edTitle.text.toString().isNotEmpty())
                updateTask(binding.edTitle.text.toString().trim())
        }
        binding.btnClose.setOnClickListener { finish() }

        getTask()
        setCalendar()
        setPriority()
    }


    private fun getTask() {
        val task = intent.getSerializableExtra(Constant.KEY_TASK_UPDATE) as Task
        binding.edTitle.setText(task.title)
        dueDate = task.dueDate
        priority = task.priority
        getPriority(priority)
        binding.calnderView.date = task.dueDate.time
        idTask = task.taskId
    }

    private fun setCalendar() {
        binding.calnderView.setOnDateChangeListener { calnderView, year, month, dayOfMonth ->
            calendar.clear()
            calendar.set(year, month, dayOfMonth)
            dueDate = calendar.time
        }
    }

    private fun getPriority(priority: Priority) {
        when (priority) {
            Priority.LOW -> binding.radioButtonLow.isChecked = true
            Priority.MEDIUM -> binding.radioButtonMedium.isChecked = true
            Priority.HIGH -> binding.radioButtonHigh.isChecked = true
        }
    }

    private fun setPriority() {
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            priority = when (checkedId) {
                R.id.radioButtonLow -> Priority.LOW
                R.id.radioButtonMedium -> Priority.MEDIUM
                R.id.radioButtonHigh -> Priority.HIGH
                else -> Priority.LOW
            }
        }
    }


    @JvmName("setTaskViewModel1")
    private fun updateTask(title: String) {
        task = Task(
            title = title,
            priority = priority,
            dueDate = dueDate,
            createDate = Calendar.getInstance().time
        )
        taskViewModel.updateTask(task.title,task.priority,task.dueDate,task.createDate,idTask)
        backHome()
    }

    private fun backHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    fun clicked(view: View) {

        when (view.id) {
            R.id.tv_today -> {
                calendar.add(Calendar.DAY_OF_YEAR, 0)
                dueDate = calendar.time
            }
            R.id.tv_tomorrow -> {
                calendar.add(Calendar.DAY_OF_YEAR, 1)
                dueDate = calendar.time
            }
            R.id.tv_next_week -> {
                calendar.add(Calendar.DAY_OF_YEAR, 7)
                dueDate = calendar.time
            }
        }
    }
}