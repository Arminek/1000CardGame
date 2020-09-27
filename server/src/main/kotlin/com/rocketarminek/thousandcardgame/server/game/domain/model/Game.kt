package com.rocketarminek.thousandcardgame.server.game.domain.model

import com.rocketarminek.thousandcardgame.server.game.domain.event.*
import com.rocketarminek.thousandcardgame.server.shared.Aggregate
import com.rocketarminek.thousandcardgame.server.shared.AggregateId
import com.rocketarminek.thousandcardgame.server.shared.ChildEntity
import com.rocketarminek.thousandcardgame.server.shared.Event
import java.util.*
import kotlin.collections.ArrayList

typealias GameId = AggregateId
typealias PlayerId = String
typealias BidId = String

class Game: Aggregate {
    lateinit var playerIds: ArrayList<PlayerId>
        private set
    private var bidInProgress: Bid? = null

    constructor(events: ArrayList<Event>): super(events)
    constructor(id: GameId, playerIds: ArrayList<PlayerId>) {
        if (playerIds.distinct().size != playerIds.size) {
            throw IllegalArgumentException("Cannot create a game with duplicated players.")
        }
        if (playerIds.size !in 2..3) {
            throw IllegalArgumentException(
                    "Cannot create a game for ${playerIds.size}. Allowed player size is for 2 or 3 players"
            )
        }
        this.apply(GameCreated(id, playerIds))
        this.startBid(id, playerIds)
    }

    fun increaseBid(amount: Int) {
        val bid = this.bidInProgress
                ?: throw IllegalArgumentException("Cannot increase the bid if there is no bid in progress!")
        bid.increase(amount)
    }

    fun passBid() {
        val bid = this.bidInProgress
                ?: throw IllegalArgumentException("Cannot pass the bid if there is no bid in progress!")
        bid.pass()
    }

    fun declareBid(amount: Int) {
        val bid = this.bidInProgress
                ?: throw IllegalArgumentException("Cannot declare the bid if there is no bid in progress!")
        bid.declare(amount)
    }

    override fun handle(event: Event) {
        when (event) {
            is GameCreated -> handle(event)
            is BidStarted -> handle(event)
        }
    }

    override fun childEntities(): ArrayList<ChildEntity> {
        val bid = this.bidInProgress ?: return arrayListOf()

        return arrayListOf(bid)
    }

    private fun startBid(id: GameId, playerIds: ArrayList<PlayerId>) {
        this.apply(BidStarted(id, UUID.randomUUID().toString(), playerIds))
        this.bidInProgress?.increase(100)
    }

    private fun handle(event: GameCreated) {
        this.id = event.id
        this.playerIds = event.playerIds
    }

    private fun handle(event: BidStarted) {
        this.bidInProgress = Bid(event.bidId, event.playerIds)
    }
}
