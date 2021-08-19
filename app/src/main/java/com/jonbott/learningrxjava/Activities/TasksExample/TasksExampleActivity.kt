package com.jonbott.learningrxjava.Activities.TasksExample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jonbott.learningrxjava.R

class TasksExampleActivity : AppCompatActivity() {

    private val presenter = TasksExamplePresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks_example)
    }
}

