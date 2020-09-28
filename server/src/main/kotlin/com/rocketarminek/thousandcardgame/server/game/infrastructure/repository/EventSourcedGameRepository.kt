package com.rocketarminek.thousandcardgame.server.game.infrastructure.repository

import com.rocketarminek.thousandcardgame.server.game.domain.model.Game
import com.rocketarminek.thousandcardgame.server.shared.*

class EventSourcedGameRepository(private val store: ReactiveEventStore, private val inMemoryStore: EventStore) : Repository<Game> {
    override fun save(aggregate: Game) {
        this.store.save(aggregate.uncommittedChanges.toList())
        this.inMemoryStore.save(aggregate.id, aggregate.uncommittedChanges)
    }

    override fun find(id: AggregateId): Game? {
        val events = this.inMemoryStore.load(id)
        if (events.isEmpty()) {
            return null
        }

        return Game(events)
    }
}
