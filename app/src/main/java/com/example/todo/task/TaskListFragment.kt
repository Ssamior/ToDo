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
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import java.util.*
import androidx.fragment.app.viewModels


class TaskListFragment() : Fragment() {
    private val taskListAdapter = TaskListAdapter()
    private var _binding: FragmentTaskListBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val tasksRepository = TasksRepository()
    private val taskViewModel: TaskListViewModel by viewModels()

    @SuppressLint("NotifyDataSetChanged")
    private val formLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra("task") as? Task
        if (task != null) {
            taskViewModel.addOrEdit(task)
        }
        taskListAdapter.notifyDataSetChanged()
    }




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    @SuppressLint("NotifyDataSetChanged")
    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = taskListAdapter

        binding.floatingActionButton.setOnClickListener{
            val intent = Intent(activity, FormActivity::class.java)
            formLauncher.launch(intent)
        }

        taskListAdapter.onClickEdit = { task ->
            val intent = Intent(activity, FormActivity::class.java)
            intent.putExtra("task", task)
            formLauncher.launch(intent)
        }
        taskListAdapter.onClickDelete = { task ->
            taskViewModel.delete(task)
            taskListAdapter.notifyDataSetChanged();
        }

        lifecycleScope.launch {
            taskViewModel.taskList.collect { newList ->
                taskListAdapter.submitList(newList);
                taskListAdapter.notifyDataSetChanged();
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

            taskViewModel.refresh();
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

