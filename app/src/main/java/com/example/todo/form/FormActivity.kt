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
        if(task != null) {
            binding.editTitle.setText(task.title)
            binding.editDescription.setText(task.description)
        }

        binding.FABValidateForm.setOnClickListener{
            val newTask = Task(id = UUID.randomUUID().toString(), binding.editTitle.text.toString(), binding.editDescription.text.toString())
            intent.putExtra("task", newTask)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}

