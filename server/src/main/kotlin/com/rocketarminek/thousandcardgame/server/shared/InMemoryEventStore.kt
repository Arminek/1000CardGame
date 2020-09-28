package com.rocketarminek.thousandcardgame.server.shared

import java.util.concurrent.ConcurrentHashMap

class InMemoryEventStore : EventStore {
    private var memory: Map<AggregateId, ArrayList<Event>> = ConcurrentHashMap()

    override fun save(id: AggregateId, events: ArrayList<Event>) {
        val committedEvents = this.memory[id] ?: arrayListOf()
        for (event in events.toList()) {
            committedEvents.add(event.deepCopy())
        }

        this.memory = this.memory.plus(Pair(id, committedEvents))
    }

    override fun load(id: AggregateId): ArrayList<Event> {
        val events = arrayListOf<Event>()
        val memory = this.memory[id] ?: arrayListOf()
        for (event in memory) {
            events.add(event.deepCopy())
        }

        return events
    }
}
