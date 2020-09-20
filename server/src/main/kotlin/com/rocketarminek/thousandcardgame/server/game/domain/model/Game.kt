package com.rocketarminek.thousandcardgame.server.game.domain.model

import com.rocketarminek.thousandcardgame.server.game.domain.event.GameCreated
import com.rocketarminek.thousandcardgame.server.shared.Event

typealias GameId = String

class Game constructor(val id: GameId, val playerIds: Array<String>) {
    val uncommittedChanges = ArrayList<Event>()

    init {
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

    private fun apply(event: Event) {
        this.uncommittedChanges.add(event)
        this.handle(event)
    }

    private fun handle(event: Event) {
        when (event) {
            is GameCreated -> handle(event)
        }
    }

    private fun handle(event: GameCreated) {}
}
