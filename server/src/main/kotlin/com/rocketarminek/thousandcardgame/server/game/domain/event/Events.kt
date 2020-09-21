package com.rocketarminek.thousandcardgame.server.game.domain.event

import com.rocketarminek.thousandcardgame.server.shared.Event

data class GameCreated(override val id: String, val playerIds: Array<String>, val type: String = "game-created"): Event
data class DealStarted(override val id: String, val playerIds: Array<String>, val type: String = "deal-started"): Event
data class DealWasBid(override val id: String, val playerId: String, val amount: Int, val type: String = "deal-was-bid"): Event
