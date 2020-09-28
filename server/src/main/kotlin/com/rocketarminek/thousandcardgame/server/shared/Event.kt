package com.rocketarminek.thousandcardgame.server.shared

import com.google.gson.Gson

interface Event {
    val id: String
    fun deepCopy(): Event;
}

abstract class BaseEvent<T: Event>(): Event {
    fun deepCopy(type: Class<T>): Event {
        val json = Gson().toJson(this)

        return Gson().fromJson(json, type)
    }
}
