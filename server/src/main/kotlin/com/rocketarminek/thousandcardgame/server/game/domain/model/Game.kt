package com.rocketarminek.thousandcardgame.server.game.domain.model

import com.rocketarminek.thousandcardgame.server.game.domain.event.*
import com.rocketarminek.thousandcardgame.server.shared.Aggregate
import com.rocketarminek.thousandcardgame.server.shared.AggregateId
import com.rocketarminek.thousandcardgame.server.shared.Event
import java.util.*
import kotlin.collections.ArrayList

typealias GameId = AggregateId
typealias PlayerId = String
typealias BidId = String

class Game: Aggregate {
    lateinit var playerIds: Array<PlayerId>
        private set
    private var currentPlayerIndex: Int = 1
    private var bidInProgress: Bid? = null

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
        val firstBidId: BidId = UUID.randomUUID().toString()
        this.apply(GameCreated(id, playerIds))
        this.apply(BidStarted(id, firstBidId, playerIds))
        this.apply(BidIncreased(id, firstBidId, playerIds[this.currentPlayerIndex - 1], 100))
        this.apply(TurnStarted(id, playerIds[this.currentPlayerIndex]))
    }

    fun increaseBid(amount: Int) {
        val bid = this.bidInProgress
                ?: throw IllegalArgumentException("Cannot increase the bid if there is no bid in progress!")

        this.apply(BidIncreased(this.id, bid.id, this.currentPlayerId(), amount))
        this.endTurn()
    }

    fun passBid() {
        val bid = this.bidInProgress
                ?: throw IllegalArgumentException("Cannot increase the bid if there is no bid in progress!")
        this.apply(BidPassed(this.id, bid.id, this.currentPlayerId()))
        this.endTurn()
    }

    override fun handle(event: Event) {
        when (event) {
            is GameCreated -> handle(event)
            is BidStarted -> handle(event)
            is BidIncreased -> handle(event)
            is TurnStarted -> handle(event)
        }
    }

    private fun handle(event: GameCreated) {
        this.id = event.id
        this.playerIds = event.playerIds
    }

    private fun handle(event: BidStarted) {
        this.bidInProgress = Bid(event.bidId, event.id)
    }

    private fun handle(event: BidIncreased) {
        this.bidInProgress?.winingPlayer = event.playerId
        this.bidInProgress?.amount = this.bidInProgress?.amount?.plus(event.amount)
    }

    private fun handle(event: TurnStarted) {
        this.currentPlayerIndex = this.playerIds.indexOf(event.playerId)
    }

    private fun currentPlayerId(): PlayerId {
        return this.playerIds[this.currentPlayerIndex]
    }

    private fun endTurn() {
        if (this.currentPlayerIndex >= this.playerIds.size) {
            this.currentPlayerIndex = 0
        } else {
            this.currentPlayerIndex += 1
        }
        this.apply(TurnStarted(this.id, this.currentPlayerId()))
    }
}
