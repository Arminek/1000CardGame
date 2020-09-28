package com.rocketarminek.thousandcardgame.server.game.domain.event

import com.rocketarminek.thousandcardgame.server.game.domain.model.BidId
import com.rocketarminek.thousandcardgame.server.game.domain.model.GameId
import com.rocketarminek.thousandcardgame.server.game.domain.model.PlayerId
import com.rocketarminek.thousandcardgame.server.shared.BaseEvent
import com.rocketarminek.thousandcardgame.server.shared.Event

data class GameCreated(
        override val id: GameId,
        val playerIds: ArrayList<PlayerId>,
        val type: String = "game-created"
) : BaseEvent<GameCreated>() {
    override fun deepCopy(): Event = this.deepCopy(GameCreated::class.java)
}

data class BidStarted(
        override val id: GameId,
        val bidId: BidId,
        val playerIds: ArrayList<PlayerId>,
        val type: String = "bid-started"
) : BaseEvent<BidStarted>() {
    override fun deepCopy(): Event = this.deepCopy(BidStarted::class.java)
}

data class BidWon(
        override val id: GameId,
        val bidId: BidId,
        val playerId: PlayerId,
        val amount: Int,
        val type: String = "bid-won"
) : BaseEvent<BidWon>() {
    override fun deepCopy(): Event = this.deepCopy(BidWon::class.java)
}

data class BidPassed(
        override val id: GameId,
        val bidId: BidId,
        val playerId: PlayerId,
        val type: String = "bid-passed"
) : BaseEvent<BidPassed>() {
    override fun deepCopy(): Event = this.deepCopy(BidPassed::class.java)
}

data class BidIncreased(
        override val id: GameId,
        val bidId: BidId,
        val playerId: PlayerId,
        val amount: Int,
        val type: String = "bid-increased"
) : BaseEvent<BidIncreased>() {
    override fun deepCopy(): Event = this.deepCopy(BidIncreased::class.java)
}

data class BidDeclared(
        override val id: GameId,
        val bidId: BidId,
        val playerId: PlayerId,
        val amount: Int,
        val type: String = "bid-declared"
): BaseEvent<BidDeclared>() {
    override fun deepCopy(): Event = this.deepCopy(BidDeclared::class.java)
}

data class TurnStarted(
        override val id: GameId,
        val playerId: PlayerId,
        val type: String = "turn-started"
): BaseEvent<TurnStarted>() {
    override fun deepCopy(): Event = this.deepCopy(TurnStarted::class.java)
}
