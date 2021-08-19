package com.jonbott.learningrxjava.ModelLayer

import com.jakewharton.rxrelay2.BehaviorRelay
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

    fun loadAllPhotoDescriptions() {
        //do in background thread
        persistenceLayer.loadAllPhotoDescriptions { photoDescriptions ->
            this.photoDescriptions.accept(photoDescriptions)

        }
    }
}