package com.jonbott.learningrxjava.Activities.ReactiveUi.Simple

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jonbott.learningrxjava.ModelLayer.Entities.Friend
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SimpleUIPresenter {

    val friends = BehaviorRelay.createDefault(listOf<Friend>())
    val title = BehaviorSubject.createDefault("Default Title")

    init {
loadFriends()
    }

    fun loadFriends() {
        title.onNext("Loading Friends")
        GlobalScope.launch {
            delay(3000)
            title.onNext("Friends loaded")
            var newFriends = listOf(Friend("Debi", "Darlington"),
                Friend("Arlie", "Abalos"),
                Friend("Jessica", "Jetton"),
                Friend("Tonia", "Threlkeld"),
                Friend("Donte", "Derosa"),
                Friend("Nohemi", "Notter"),
                Friend("Rod", "Rye"),
                Friend("Simonne", "Sala"),
                Friend("Kathaleen", "Kyles"),
                Friend("Loan", "Lawrie"),
                Friend("Elden", "Ellen"),
                Friend("Felecia", "Fortin"),
                Friend("Fiona", "Fiorini"),
                Friend("Joette", "July"),
                Friend("Beverley", "Bob"),
                Friend("Artie", "Aquino"),
                Friend("Yan", "Ybarbo"),
                Friend("Armando", "Araiza"),
                Friend("Dolly", "Delapaz"),
                Friend("Juliane", "Jobin"))

            GlobalScope.launch(Dispatchers.Main) {
                friends.accept(newFriends)
            }

        }

        GlobalScope.launch {
            delay(5000)
            title.onError(Exception("Fake Error"))
        }

        GlobalScope.launch {
            delay(6000)
            title.onNext("New Value")
        }
    }


}