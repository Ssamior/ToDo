package com.example.todo.form

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.todo.R
import com.example.todo.Task
import com.example.todo.databinding.ActivityFormBinding
import com.example.todo.databinding.ActivityMainBinding
import java.util.*

private lateinit var binding: ActivityFormBinding

class FormActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.FABValidateForm.setOnClickListener{

            val newTask = Task(id = UUID.randomUUID().toString(), binding.editTitle.toString(), binding.editDescription.toString())
            intent.putExtra("task", newTask)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}