package com.rocketarminek.thousandcardgame.server.game.domain.event

import com.rocketarminek.thousandcardgame.server.game.domain.model.BidId
import com.rocketarminek.thousandcardgame.server.game.domain.model.GameId
import com.rocketarminek.thousandcardgame.server.game.domain.model.PlayerId
import com.rocketarminek.thousandcardgame.server.shared.Event

data class GameCreated(
        override val id: GameId,
        val playerIds: ArrayList<PlayerId>,
        val type: String = "game-created"
) : Event
data class BidStarted(
        override val id: GameId,
        val bidId: BidId,
        val playerIds: ArrayList<PlayerId>,
        val type: String = "bid-started"
) : Event
data class BidWon(
        override val id: GameId,
        val bidId: BidId,
        val playerId: PlayerId,
        val amount: Int,
        val type: String = "bid-won"
) : Event
data class BidPassed(
        override val id: GameId,
        val bidId: BidId,
        val playerId: PlayerId,
        val type: String = "bid-passed"
) : Event
data class BidIncreased(
        override val id: GameId,
        val bidId: BidId,
        val playerId: PlayerId,
        val amount: Int,
        val type: String = "bid-increased"
) : Event
data class BidDeclared(
        override val id: GameId,
        val bidId: BidId,
        val playerId: PlayerId,
        val amount: Int,
        val type: String = "bid-declared"
): Event
data class TurnStarted(
        override val id: GameId,
        val playerId: PlayerId,
        val type: String = "turn-started"
): Event
