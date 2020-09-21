package com.rocketarminek.thousandcardgame.server.shared

class InMemoryEventStore : EventStore {
    private var memory: Map<AggregateId, ArrayList<Event>> = mapOf()

    override fun save(id: AggregateId, events: ArrayList<Event>) {
        val committedEvents = this.memory.get(id) ?: arrayListOf()
        committedEvents.addAll(events)

        this.memory = this.memory.plus(Pair(id, committedEvents))
    }

    override fun load(id: AggregateId): ArrayList<Event> = this.memory.get(id) ?: arrayListOf()
}
