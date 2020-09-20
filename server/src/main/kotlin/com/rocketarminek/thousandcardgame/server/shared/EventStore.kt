package com.rocketarminek.thousandcardgame.server.shared

interface EventStore {
    fun save(id: AggregateId, events: ArrayList<Event>)
    fun load(id: AggregateId): ArrayList<Event>?
}
