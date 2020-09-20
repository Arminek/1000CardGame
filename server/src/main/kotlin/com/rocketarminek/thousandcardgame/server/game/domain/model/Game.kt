package com.rocketarminek.thousandcardgame.server.game.domain.model

import com.rocketarminek.thousandcardgame.server.game.domain.event.GameCreated
import com.rocketarminek.thousandcardgame.server.shared.Aggregate
import com.rocketarminek.thousandcardgame.server.shared.AggregateId
import com.rocketarminek.thousandcardgame.server.shared.Event

typealias GameId = AggregateId
typealias PlayerId = String

class Game: Aggregate {
    lateinit var playerIds: Array<PlayerId>
    constructor(id: GameId, playerIds: Array<PlayerId>) {
        if (playerIds.distinct().size != playerIds.size) {
            throw IllegalArgumentException("Cannot create a game with duplicated players.")
        }
        if (playerIds.size !in 2..3) {
            throw IllegalArgumentException(
                    "Cannot create a game for ${playerIds.size}. Allowed player size is for 2 or 3 players"
            )
        }
        this.apply(GameCreated(id, playerIds))
    }
    constructor(events: ArrayList<Event>): super(events)

    override fun handle(event: Event) {
        when (event) {
            is GameCreated -> handle(event)
        }
    }

    private fun handle(event: GameCreated) {
        this.id = event.id
        this.playerIds = event.playerIds
    }
}
