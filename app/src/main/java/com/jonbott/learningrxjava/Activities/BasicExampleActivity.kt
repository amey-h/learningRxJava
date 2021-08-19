package com.jonbott.learningrxjava.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jonbott.learningrxjava.R
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class BasicExampleActivity : AppCompatActivity() {

    private var bag = CompositeDisposable()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://jsonplaceholder.typicode.com/")
        .build()

    private val service = retrofit.create(JsonPlaceholderService::class.java)
    //region Life Cycle Events

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_example)
    }

    //endregion
}