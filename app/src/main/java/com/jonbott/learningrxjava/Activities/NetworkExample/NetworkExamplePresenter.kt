package com.jonbott.learningrxjava.Activities.NetworkExample

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jonbott.learningrxjava.ModelLayer.Entities.Message
import com.jonbott.learningrxjava.ModelLayer.ModelLayer
import io.reactivex.disposables.CompositeDisposable

class NetworkExamplePresenter {
    private val modelLayer = ModelLayer.shared //normally injected

    val messages: BehaviorRelay<List<Message>>
    get() = modelLayer.messages

    private val bag = CompositeDisposable()

    init {
        modelLayer.getMessages()
    }
}