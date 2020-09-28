package com.rocketarminek.thousandcardgame.server.shared

interface EventStore {
    fun save(id: AggregateId, events: List<Event>)
    fun load(id: AggregateId): List<Event>
}
