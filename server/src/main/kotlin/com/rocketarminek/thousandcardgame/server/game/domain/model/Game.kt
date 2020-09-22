package com.rocketarminek.thousandcardgame.server.game.domain.model

import com.rocketarminek.thousandcardgame.server.game.domain.event.DealStarted
import com.rocketarminek.thousandcardgame.server.game.domain.event.GameCreated
import com.rocketarminek.thousandcardgame.server.shared.Aggregate
import com.rocketarminek.thousandcardgame.server.shared.AggregateId
import com.rocketarminek.thousandcardgame.server.shared.Event
import java.util.*
import kotlin.collections.ArrayList

typealias GameId = AggregateId
typealias PlayerId = String
typealias DealId = String

class Game: Aggregate {
    lateinit var playerIds: Array<PlayerId>
    constructor(events: ArrayList<Event>): super(events)
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
        this.apply(DealStarted(UUID.randomUUID().toString(), id))
    }

    override fun handle(event: Event) {
        when (event) {
            is GameCreated -> handle(event)
            is DealStarted -> handle(event)
        }
    }

    private fun handle(event: GameCreated) {
        this.id = event.id
        this.playerIds = event.playerIds
    }

    private fun handle(event: DealStarted) {}
}
