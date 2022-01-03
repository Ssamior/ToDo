package com.example.todo.form

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.todo.task.Task
import com.example.todo.databinding.ActivityFormBinding
import java.util.*

private lateinit var binding: ActivityFormBinding

class FormActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val task = intent.getSerializableExtra("task") as? Task
        val taskTitle = task?.title ?: "Title";
        val taskDescription = task?.description ?: "Description";

        binding.editTitle.setText(taskTitle)
        binding.editDescription.setText(taskDescription)

        binding.FABValidateForm.setOnClickListener {

            val taskTitle_ = binding.editTitle.text.toString()
            val taskDescription_ = binding.editDescription.text.toString()

            val newTask = Task(id = task?.id ?: UUID.randomUUID().toString(), title = taskTitle_, description = taskDescription_)

            intent.putExtra("task", newTask)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}

