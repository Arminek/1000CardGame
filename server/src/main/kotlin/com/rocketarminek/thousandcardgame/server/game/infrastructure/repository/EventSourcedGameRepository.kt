package com.rocketarminek.thousandcardgame.server.game.infrastructure.repository

import com.rocketarminek.thousandcardgame.server.game.domain.model.Game
import com.rocketarminek.thousandcardgame.server.shared.AggregateId
import com.rocketarminek.thousandcardgame.server.shared.EventStore
import com.rocketarminek.thousandcardgame.server.shared.Repository

class EventSourcedGameRepository(private val store: EventStore) : Repository<Game> {
    override fun save(aggregate: Game) = this.store.save(aggregate.id, aggregate.uncommittedChanges)

    override fun find(id: AggregateId): Game? = if(store.load(id).isEmpty()) { null } else { Game(store.load(id)) }
}
