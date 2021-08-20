package com.jonbott.learningrxjava.Activities.ReactiveUi.Simple

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ArrayAdapter
import com.jonbott.learningrxjava.Common.disposedBy
import com.jonbott.learningrxjava.ModelLayer.Entities.Friend
import com.jonbott.learningrxjava.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_simple_ui.*

class SimpleUIActivity : AppCompatActivity() {

    private val presenter = SimpleUIPresenter()
    private var bag = CompositeDisposable()

    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_ui)

        rxExamples()
    }

    private fun rxExamples() {
        rxSimpleListBind()
        //simpleRx()
    }

    private fun simpleRx() {
        presenter.title.observeOn(AndroidSchedulers.mainThread())
            .onErrorReturn {
                "Default Value"
            }
            .share()
            .subscribe {
                simpleUITitleTextView.text = it
            }
            .disposedBy(bag)
    }

    @SuppressLint("CheckResult")
    private fun rxSimpleListBind() {
        val listItems = presenter.friends.value.map { it.toString() }

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems)

        simpleUIListView.adapter = adapter

        presenter.friends.subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(::updateList)
    }

    fun updateList(ites: List<Friend>) {
        val itemsArray = ites.map {
            it.description
        }.toTypedArray()
        adapter.clear()
        adapter.addAll(*itemsArray)
        adapter.notifyDataSetChanged()
    }
}
