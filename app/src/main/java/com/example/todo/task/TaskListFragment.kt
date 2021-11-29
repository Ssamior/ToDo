package com.example.todo.task

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.databinding.FragmentTaskListBinding
import com.example.todo.form.FormActivity
import java.util.*

private val taskList = mutableListOf(
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
            taskList.add(task)
            taskListAdapter.submitList(taskList.toList())
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }


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
            taskList.remove(task)
            taskListAdapter.submitList(taskList.toList())
        }
        taskListAdapter.onClickDelete = { task ->
            taskList.remove(task)
            taskListAdapter.submitList(taskList.toList())
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}