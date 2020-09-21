package com.rocketarminek.thousandcardgame.server.shared

import reactor.core.publisher.Flux

interface ReactiveEventStore {
    fun save(events: ArrayList<Event>)
    fun load(id: AggregateId): Flux<Event>
}
