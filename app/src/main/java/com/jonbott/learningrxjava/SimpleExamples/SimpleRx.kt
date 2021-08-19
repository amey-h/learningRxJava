package com.jonbott.learningrxjava.SimpleExamples

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jonbott.learningrxjava.Common.disposedBy
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object SimpleRx {

    var bag = CompositeDisposable()

    fun simpleValues() {
        val someInfo = BehaviorRelay.createDefault("1");
        println("someInfo.val = ${someInfo.value}")

        someInfo.accept("2")
        println("someInfo.val after change = ${someInfo.value}")

        someInfo.subscribe { newValue ->
            println("New Value: $newValue")
        }
        someInfo.accept("3")
    }

    fun subjects() {
        val sub = BehaviorSubject.createDefault(24)
        sub.subscribe({ newValue -> //onNext
            println("onNext NewValue: $newValue")
        }, { error -> //onError
            println("onError")
        }, { // onCompleted
            println("onCompleted")
        }, { disposable -> //onSubscribed
            println("onSubscribed")
        })

        sub.onNext(32)
        sub.onNext(48)
        sub.onNext(32)

        /*val someException = IllegalArgumentException("exception")
        sub.onError(someException)
        sub.onNext(54)*/

        sub.onComplete()
        sub.onNext(54)
    }

    fun basicObservable() {
        val observable = Observable.create<String> {observer ->
            // the lamda is called for every subscriber by default
            println("---- Observable logic is called ----")

            //do work in background
            GlobalScope.launch {
                delay(1000)
                observer.onNext("SomeValue 24")
                observer.onComplete()
            }
        }

        observable.subscribe{value ->
            println("value: $value")
        }.disposedBy(bag)
    }
}