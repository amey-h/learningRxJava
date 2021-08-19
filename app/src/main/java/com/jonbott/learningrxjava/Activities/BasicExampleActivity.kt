package com.jonbott.learningrxjava.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jonbott.learningrxjava.Common.disposedBy
import com.jonbott.learningrxjava.ModelLayer.Entities.Posting
import com.jonbott.learningrxjava.R
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.item_message.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


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

        singleExample()
    }


    private fun singleExample() {
        loadPostAsSingle().observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ posting ->
                userIdTextView.text = posting.title
                bodyTextView.text = posting.body
            }, { error ->
                userIdTextView.text = "Error"
                bodyTextView.text = error.message
            }).disposedBy(bag)

    }

    private fun loadPostAsSingle(): Single<Posting> {
        return Single.create { observer ->
            Thread.sleep(2000)

            val id = "5"
            service.getPosts(id).enqueue(object : Callback<Posting> {
                override fun onResponse(call: Call<Posting>, response: Response<Posting>) {

                    val posting = response.body()
                    if (posting != null) {
                        observer.onSuccess(posting)
                    } else {
                        val error = IOException("Server Error")
                        observer.onError(error)
                    }
                }

                override fun onFailure(call: Call<Posting>, t: Throwable) {
                    observer.onError(t)
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bag.clear()
    }
}



