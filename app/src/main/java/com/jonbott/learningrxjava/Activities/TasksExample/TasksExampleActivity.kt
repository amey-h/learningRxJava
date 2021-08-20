package com.jonbott.learningrxjava.Activities.TasksExample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jonbott.learningrxjava.Common.disposedBy
import com.jonbott.learningrxjava.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class TasksExampleActivity : AppCompatActivity() {

    private val presenter = TasksExamplePresenter()
    private val bag = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks_example)

        presenter.loadPeopleInfo().observeOn(AndroidSchedulers.mainThread())
            .subscribe({infoList->
                println("All processes completed successfully")
                infoList.forEach {
                    println("info: $it")
                }
            }, {error ->
                println("Few processes failed to complete")
            }).disposedBy(bag)
    }
}

