package com.jonbott.learningrxjava.ModelLayer

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jonbott.learningrxjava.ModelLayer.Entities.Message
import com.jonbott.learningrxjava.ModelLayer.NetworkLayer.NetworkLayer
import com.jonbott.learningrxjava.ModelLayer.PersistenceLayer.PersistenceLayer
import com.jonbott.learningrxjava.ModelLayer.PersistenceLayer.PhotoDescription

class ModelLayer {

    companion object {
        val shared = ModelLayer()
    }

    private val networkLayer = NetworkLayer.instance
    private val persistenceLayer = PersistenceLayer.shared

    val photoDescriptions = BehaviorRelay.createDefault(listOf<PhotoDescription>())

    val messages = BehaviorRelay.createDefault(listOf<Message>())

    fun loadAllPhotoDescriptions() {
        //do in background thread
        persistenceLayer.loadAllPhotoDescriptions { photoDescriptions ->
            this.photoDescriptions.accept(photoDescriptions)

        }
    }

    fun getMessages() {
        return networkLayer.getMessages({ messages ->
            this.messages.accept(messages)

        }, { error ->
            notifyError(error)
        })
    }

    fun notifyError(error: String) {
        println("Network Error: $error")
    }
}