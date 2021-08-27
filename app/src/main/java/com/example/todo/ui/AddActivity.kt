package com.example.todo.ui

import android.content.Intent
import android.os.Bundle
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.todo.R
import com.example.todo.data.Repository
import com.example.todo.data.TaskDatabase
import com.example.todo.databinding.ActivityAddBinding
import com.example.todo.model.Priority
import com.example.todo.model.Task
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


class AddActivity : AppCompatActivity() {

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var task: Task
    private lateinit var dueDate: Date
    private var calendar: Calendar = Calendar.getInstance()
    private lateinit var binding: ActivityAddBinding
    private lateinit var priority: Priority
    private var showAndHidPraiorty = true
    private var showAndHidDateDue = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TODO)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dueDate = Date()
        priority = Priority.LOW

        val repository = Repository(TaskDatabase.invoke(this))
        val taskViewModelProvider = TaskViewModelProvider(repository)
        taskViewModel =
            ViewModelProvider(this, taskViewModelProvider).get(TaskViewModel::class.java)

        binding.btnSave.setOnClickListener {
            if (binding.edTitle.text.toString().isNotEmpty()) {
                binding.lodding.visibility = View.VISIBLE
                binding.tvLodding.visibility = View.VISIBLE
                GlobalScope.launch {
                    delay(1000L)
                    addNewTask(binding.edTitle.text.toString().trim())
                }
            }
        }
        binding.btnClose.setOnClickListener {
            finish()
        }
        binding.btnShowPraiorty.setOnClickListener {
            if (showAndHidPraiorty) {
                showAndHidPraiorty = false
                TransitionManager.beginDelayedTransition(binding.root)
                binding.radioGroup.visibility = View.VISIBLE
            } else {
                showAndHidPraiorty = true
                TransitionManager.beginDelayedTransition(binding.root)
                binding.radioGroup.visibility = View.GONE
            }
        }
        binding.btnRemainder.setOnClickListener {
            if (showAndHidDateDue) {
                showAndHidDateDue = false
                TransitionManager.beginDelayedTransition(binding.root)
                binding.tvToday.visibility = View.VISIBLE
                binding.tvTomorrow.visibility = View.VISIBLE
                binding.tvNextWeek.visibility = View.VISIBLE
            } else {
                showAndHidDateDue = true
                TransitionManager.beginDelayedTransition(binding.root)
                binding.tvToday.visibility = View.GONE
                binding.tvTomorrow.visibility = View.GONE
                binding.tvNextWeek.visibility = View.GONE
            }
        }

        setCalendar()
        setPriority()
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

    private fun setCalendar() {
        binding.calnderView.setOnDateChangeListener { calnderView, year, month, dayOfMonth ->
            calendar.clear()
            calendar.set(year, month, dayOfMonth)
            dueDate = calendar.time
        }
    }

    @JvmName("setTaskViewModel1")
    private fun addNewTask(title: String) {
        task = Task(
            title = title,
            priority = priority,
            dueDate = dueDate,
            createDate = Calendar.getInstance().time
        )
        taskViewModel.addTask(task)
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