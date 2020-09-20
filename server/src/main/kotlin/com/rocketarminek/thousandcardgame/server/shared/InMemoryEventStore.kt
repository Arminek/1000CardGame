package com.rocketarminek.thousandcardgame.server.shared

class InMemoryEventStore : EventStore {
    private var memory: Map<AggregateId, ArrayList<Event>> = mapOf()

    override fun save(id: AggregateId, events: ArrayList<Event>) {
        this.memory = this.memory.plus(Pair(id, events))
    }

    override fun load(id: AggregateId): ArrayList<Event>? = this.memory.get(id)
}
