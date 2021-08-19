package com.jonbott.learningrxjava.Activities.DatabaseExample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jonbott.learningrxjava.Activities.DatabaseExample.Recycler.PhotoDescriptionViewAdapter
import com.jonbott.learningrxjava.Common.disposedBy
import com.jonbott.learningrxjava.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_database_example.*


class DatabaseExampleActivity : AppCompatActivity() {

    private val presenter = DatabaseExamplePresenter()

    lateinit var adapter: PhotoDescriptionViewAdapter

    private val bag = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database_example)

        presenter.photoDescriptions.observeOn(AndroidSchedulers.mainThread())
            .subscribe{ descriptions ->
                descriptions.forEach { println("result: $it") }
                adapter.photoDescriptions.accept(descriptions.toMutableList())

            }.disposedBy(bag)

        attachUI()
    }

    private fun attachUI() {
        val linearLayoutManager =
            LinearLayoutManager(this)
        val dividerItemDecoration =
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )

        photoDescriptionRecyclerView.setHasFixedSize(true)
        photoDescriptionRecyclerView.layoutManager = linearLayoutManager
        photoDescriptionRecyclerView.addItemDecoration(dividerItemDecoration)

        initializeListView()
    }

    private fun initializeListView() {
        adapter = PhotoDescriptionViewAdapter { view, position -> rowTapped(position) }
        photoDescriptionRecyclerView.adapter = adapter
    }

    private fun rowTapped(position: Int) {
        println("🍄")
        println(adapter.photoDescriptions.value[position])
    }

    override fun onDestroy() {
        super.onDestroy()
        bag.clear()
    }
}