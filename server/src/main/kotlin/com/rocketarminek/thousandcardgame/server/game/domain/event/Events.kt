package com.rocketarminek.thousandcardgame.server.game.domain.event

import com.rocketarminek.thousandcardgame.server.game.domain.model.DealId
import com.rocketarminek.thousandcardgame.server.game.domain.model.GameId
import com.rocketarminek.thousandcardgame.server.shared.Event

data class GameCreated(override val id: GameId, val playerIds: Array<String>, val type: String = "game-created"): Event
data class DealStarted(override val id: DealId, val gameId: GameId, val type: String = "deal-started"): Event
data class DealWasBid(override val id: DealId, val gameId: GameId, val playerId: String, val amount: Int, val type: String = "deal-was-bid"): Event
