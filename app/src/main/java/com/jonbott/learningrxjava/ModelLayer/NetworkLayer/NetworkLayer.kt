package com.jonbott.learningrxjava.ModelLayer.NetworkLayer

import com.github.kittinunf.result.Result
import com.jonbott.datalayerexample.DataLayer.NetworkLayer.EndpointInterfaces.JsonPlaceHolder
import com.jonbott.datalayerexample.DataLayer.NetworkLayer.Helpers.ServiceGenerator
import com.jonbott.learningrxjava.Common.NullBox
import com.jonbott.learningrxjava.Common.StringLambda
import com.jonbott.learningrxjava.Common.VoidLambda
import com.jonbott.learningrxjava.ModelLayer.Entities.Message
import com.jonbott.learningrxjava.ModelLayer.Entities.Person
import io.reactivex.Observable
import io.reactivex.rxkotlin.zip
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.Exception
import java.util.*


typealias MessageLambda = (Message?) -> Unit
typealias MessagesLambda = (List<Message>?) -> Unit

class NetworkLayer {

    companion object {
        val instance = NetworkLayer()
    }

    private val placeHolderApi: JsonPlaceHolder

    init {
        placeHolderApi = ServiceGenerator.createService(JsonPlaceHolder::class.java)
    }


    //region End Point - SemiRx Way

    fun getMessages(success: MessagesLambda, failure: StringLambda) {
        val call = placeHolderApi.getMessages()

        call.enqueue(object : Callback<List<Message>> {
            override fun onResponse(
                call: Call<List<Message>>?,
                response: Response<List<Message>>?
            ) {
                val article = parseRespone(response)
                success(article)
            }

            override fun onFailure(call: Call<List<Message>>?, t: Throwable?) {
                println("Failed to GET Message: ${t?.message}")
                failure(t?.localizedMessage ?: "Unknown error occured")
            }
        })
    }

    fun getMessage(articleId: String, success: MessageLambda, failure: VoidLambda) {
        val call = placeHolderApi.getMessage(articleId)

        call.enqueue(object : Callback<Message> {
            override fun onResponse(call: Call<Message>?, response: Response<Message>?) {
                val article = parseRespone(response)
                success(article)
            }

            override fun onFailure(call: Call<Message>?, t: Throwable?) {
                println("Failed to GET Message: ${t?.message}")
                failure()
            }
        })
    }

    fun postMessage(message: Message, success: MessageLambda, failure: VoidLambda) {
        val call = placeHolderApi.postMessage(message)

        call.enqueue(object : Callback<Message> {
            override fun onResponse(call: Call<Message>?, response: Response<Message>?) {
                val article = parseRespone(response)
                success(article)
            }

            override fun onFailure(call: Call<Message>?, t: Throwable?) {
                println("Failed to POST Message: ${t?.message}")
                failure()
            }
        })
    }

    //Make observable for each person
    fun loadInfoFor(people: List<Person>): Observable<List<String>> {
        // for each person make network call
        val networkObservables = people.map(::buildGetInfoNetworkCallFor)
        return networkObservables.zip { list ->
            list.filter { box -> box.value != null }
                .map {
                    it.value!!
                }

        }
    }

    //Wrap task in reactive observable
    fun buildGetInfoNetworkCallFor(person: Person): Observable<NullBox<String>> {
        return Observable.create { observer ->
            // execute request
            getInfoFor(person) { result ->
                result.fold({ info ->
                    observer.onNext(info)
                    observer.onComplete()
                }, { error ->
                    observer.onError(error)
                })

            }

        }
    }

    // create network task
    fun getInfoFor(person: Person, finished: (Result<NullBox<String>, Exception>) -> Unit) {
        GlobalScope.launch {
            println("start network call : $person")
            val randomTime: Long = ((person.age * 1000).toLong())
            delay(randomTime)
            println("finished network call : $person")

            var result = Result.of(NullBox(person.toString()))
            finished(result)

        }
    }

    private fun <T> parseRespone(response: Response<T>?): T? {
        val article = response?.body() ?: null

        if (article == null) {
            parseResponeError(response)
        }

        return article
    }

    private fun <T> parseResponeError(response: Response<T>?) {
        if (response == null) return

        val responseBody = response.errorBody()

        if (responseBody != null) {
            try {
                val text = "responseBody = ${responseBody.string()}"
                println("$text")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            val text = "responseBody == null"
            println("$text")
        }
    }

    //endregion

}