package com.rocketarminek.thousandcardgame.server.game.infrastructure.repository

import com.rocketarminek.thousandcardgame.server.game.domain.model.Game
import com.rocketarminek.thousandcardgame.server.shared.AggregateId
import com.rocketarminek.thousandcardgame.server.shared.EventStore
import com.rocketarminek.thousandcardgame.server.shared.Repository

class EventSourcedGameRepository(private val store: EventStore) : Repository<Game> {
    override fun save(aggregate: Game) = this.store.save(aggregate.id, aggregate.uncommittedChanges)

    override fun find(id: AggregateId): Game? = when(this.store.load(id)) {
        null -> null
        else -> Game(this.store.load(id)!!)
    }
}
