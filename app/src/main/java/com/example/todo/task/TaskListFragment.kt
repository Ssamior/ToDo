package com.example.todo.task

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.databinding.FragmentTaskListBinding
import com.example.todo.form.FormActivity
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

private var taskList = mutableListOf(
    Task(id = "id_1", title = "Task 1", description = "description 1"),
    Task(id = "id_2", title = "Task 2"),
    Task(id = "id_3", title = "Task 3")
)


class TaskListFragment() : Fragment() {
    private val taskListAdapter = TaskListAdapter()
    private var _binding: FragmentTaskListBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val formLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra("task") as? Task
        if (task != null) {
            lifecycleScope.launch {
                tasksRepository.createTask(task)
                taskListAdapter.submitList(taskList.toList())
            }
        }
    }

    private val tasksRepository = TasksRepository()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = taskListAdapter
        taskListAdapter.submitList(taskList.toList())

        binding.floatingActionButton.setOnClickListener{
            val intent = Intent(activity, FormActivity::class.java)
            formLauncher.launch(intent)
        }

        taskListAdapter.onClickEdit = { task ->
            val intent = Intent(activity, FormActivity::class.java)
            intent.putExtra("task", task)
            formLauncher.launch(intent)
            lifecycleScope.launch {
                tasksRepository.deleteTask(task.id)
                tasksRepository.refresh()
                taskListAdapter.submitList(taskList.toList())
            }
        }
        taskListAdapter.onClickDelete = { task ->
            lifecycleScope.launch {
                tasksRepository.deleteTask(task.id)
                tasksRepository.refresh()
                taskListAdapter.submitList(taskList.toList())
            }
        }

        lifecycleScope.launch { // on lance une coroutine car `collect` est `suspend`
            tasksRepository.taskList.collect { list ->
                if (taskList != list) {
                    taskList = list.toMutableList()
                    taskListAdapter.submitList(taskList.toList())
                }
            }

        }

    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            // Ici on ne va pas gérer les cas d'erreur donc on force le crash avec "!!"
            val userInfo = Api.userWebService.getInfo().body()!!
            binding.textViewUser.text = "User : ${userInfo.firstName} ${userInfo.lastName}"

            tasksRepository.refresh() // on demande de rafraîchir les données sans attendre le retour directement
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

