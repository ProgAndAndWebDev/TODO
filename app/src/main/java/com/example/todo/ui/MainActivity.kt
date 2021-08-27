package com.example.todo.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.todo.R
import com.example.todo.adapter.OnClickItem
import com.example.todo.adapter.TaskAdapter
import com.example.todo.adapter.TaskAdapter2
import com.example.todo.data.Repository
import com.example.todo.data.TaskDatabase
import com.example.todo.databinding.ActivityMainBinding
import com.example.todo.model.Task
import com.example.todo.util.Constant
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.*
import java.util.*

class MainActivity : AppCompatActivity(), OnClickItem {

    lateinit var binding: ActivityMainBinding
    lateinit var taskViewModel: TaskViewModel
    lateinit var taskAdapter: TaskAdapter
    lateinit var taskAdapter2: TaskAdapter2
    lateinit var repository: Repository
    var visibil = false
    private val NIGHTMARE = "NightMode"

    @SuppressLint("CommitPrefEdits", "WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TODO)
        Thread.sleep(1000L)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        

        val appSettingPref: SharedPreferences = getSharedPreferences("AppSettingPref", 0)
        val sharedPrefEdit: SharedPreferences.Editor = appSettingPref.edit()
        val isNightMode: Boolean = appSettingPref.getBoolean(NIGHTMARE, false)

        if (isNightMode)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


        repository = Repository(TaskDatabase.invoke(this))
        val taskViewModelProvider = TaskViewModelProvider(repository)
        taskViewModel =
            ViewModelProvider(this, taskViewModelProvider).get(TaskViewModel::class.java)

        binding.btnAdd.setOnClickListener {
            addNewTask()
        }
        setTaskViewModel(taskViewModel)
        setRecyclerView()

        binding.btnSerach.setOnClickListener {
            getQuietTask()
            visibil = true
        }
        binding.btnCloseSerch.setOnClickListener {
            visibil = false
            TransitionManager.beginDelayedTransition(binding.root)
            binding.searchView.visibility = View.GONE
            binding.btnCloseSerch.visibility = View.GONE
            binding.container1.visibility = View.VISIBLE
        }
        binding.btnDarkTheme.setOnClickListener {
            if (isNightMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                AppCompatDelegate.setDefaultNightMode(Animation.START_ON_FIRST_FRAME)
                sharedPrefEdit.putBoolean(NIGHTMARE, false)
                sharedPrefEdit.apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPrefEdit.putBoolean(NIGHTMARE, true)
                sharedPrefEdit.apply()
            }

        }

    }

    private fun getQuietTask() {
        TransitionManager.beginDelayedTransition(binding.root)
        binding.searchView.visibility = View.VISIBLE
        binding.container1.visibility = View.GONE
        binding.btnCloseSerch.visibility = View.VISIBLE

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    taskViewModel.getItemTask(query.trim())
                    taskViewModel.getItemTasks.observe(this@MainActivity, Observer { item ->
                        taskAdapter.differ.submitList(item)
                    })
                } else {
                    taskViewModel.getTasks.observe(this@MainActivity, Observer { task ->
                        task.filter { !it.isDone }.apply { taskAdapter.differ.submitList(this) }
                    })
                }
                return true
            }
        })
    }

    private fun addNewTask() {
        startActivity(Intent(this, AddActivity::class.java))
        finish()
    }

    @JvmName("setTaskViewModel1")
    private fun setTaskViewModel(taskViewModel: TaskViewModel) {
        taskViewModel.getTasks.observe(this, Observer { task ->
            task.filter { !it.isDone }.apply { taskAdapter.differ.submitList(this) }
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    binding.tvCountTask.text = repository.getCountTask().toString()
                }
            }
        })

        // this for show compelete task
        taskViewModel.getTasks.observe(this, Observer { task ->
            task.filter { it.isDone }.apply {
                taskAdapter2.differ.submitList(this)
            }
        })
    }

    private fun setRecyclerView() {
        taskAdapter = TaskAdapter(this, this)
        binding.recyclerview.adapter = taskAdapter
        binding.recyclerview.setHasFixedSize(true)

        taskAdapter2 = TaskAdapter2(this, this)
        binding.recyclerviewHorizontal.adapter = taskAdapter2
        binding.recyclerviewHorizontal.setHasFixedSize(true)
    }

    override fun longClickItem(task: Task) {
        TransitionManager.beginDelayedTransition(binding.root)
        binding.container2.visibility = View.VISIBLE

        binding.btnClose.setOnClickListener {
            TransitionManager.beginDelayedTransition(binding.root)
            if (visibil) {
                binding.container1.visibility = View.GONE
            }
            binding.container2.visibility = View.GONE
        }

        binding.btnDelete.setOnClickListener {
            MaterialAlertDialogBuilder(
                this,
                R.style.ThemeOverlay_AppCompat
            )
                .setMessage(resources.getString(R.string.long_message))
                .setNegativeButton(resources.getString(R.string.decline)) { dialog, which ->
                    TransitionManager.beginDelayedTransition(binding.root)
                    binding.container1.visibility = View.VISIBLE
                    binding.container2.visibility = View.GONE
                    dialog.cancel()
                }
                .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                    taskViewModel.deleteTask(task)
                    TransitionManager.beginDelayedTransition(binding.root)
                    if (!visibil) {
                        binding.container1.visibility = View.VISIBLE
                    }
                    binding.container2.visibility = View.GONE
                }
                .show()
        }

        binding.btnEdit.setOnClickListener {
            Intent(this, UpdateActivity::class.java).apply {
                putExtra(Constant.KEY_TASK_UPDATE, task)
                startActivity(this)
            }
        }
    }

    override fun clickCheckTask(id: Long) {
        taskViewModel.updateOneTask(true, id)
    }

    override fun clickBackTask(id: Long) {
        taskViewModel.updateOneTask(false, id)
    }


}
