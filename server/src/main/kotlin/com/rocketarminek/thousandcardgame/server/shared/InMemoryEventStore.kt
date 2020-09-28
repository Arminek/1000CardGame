package com.rocketarminek.thousandcardgame.server.shared

import java.util.concurrent.ConcurrentHashMap

class InMemoryEventStore : EventStore {
    private var memory: Map<AggregateId, List<Event>> = ConcurrentHashMap()

    override fun save(id: AggregateId, events: List<Event>) {
        val committedEvents = this.memory[id]?.toMutableList() ?: mutableListOf()
        committedEvents.addAll(events.map { it.deepCopy() })

        this.memory = this.memory.plus(Pair(id, committedEvents))
    }

    override fun load(id: AggregateId): List<Event> = (this.memory[id] ?: arrayListOf()).map { it.deepCopy() }
}
