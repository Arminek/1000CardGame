package com.rocketarminek.thousandcardgame.server.game.domain.model

import com.rocketarminek.thousandcardgame.server.game.domain.event.BidIncreased
import com.rocketarminek.thousandcardgame.server.game.domain.event.BidPassed
import com.rocketarminek.thousandcardgame.server.game.domain.event.BidWon
import com.rocketarminek.thousandcardgame.server.shared.ChildEntity
import com.rocketarminek.thousandcardgame.server.shared.Event

class Bid(id: BidId, playerIds: ArrayList<PlayerId>): ChildEntity(id) {
    var amount = 0
        private set
    var lastPlayer: PlayerId? = null
        private set
    private val turnSequence: TurnSequence = TurnSequence(id, playerIds)

    fun start() {
        this.increase(100)
    }

    fun increase(amount: Int) {
        this.root?.let {
            if (this.turnSequence.canSwitchTurn()) {
                this.apply(BidIncreased(it.id, this.id, this.turnSequence.current, amount))
                this.turnSequence.next()
            }
            if (!this.turnSequence.canSwitchTurn()) {
                this.lastPlayer?.let { player ->
                    BidWon(it.id, this.id, player, this.amount)
                }?.let { event ->
                    this.apply(event)
                    this.turnSequence.next()
                }
            }
        }
    }

    fun pass() {
        this.root?.let {
            if (this.turnSequence.canSwitchTurn()) {
                this.apply(BidPassed(it.id, this.id, this.turnSequence.current))
                this.turnSequence.next()
            }
            if (!this.turnSequence.canSwitchTurn()) {
                this.lastPlayer?.let { player ->
                    BidWon(it.id, this.id, player, this.amount)
                }?.let { event ->
                    this.apply(event)
                    this.turnSequence.next()
                }
            }
        }
    }

    override fun handle(event: Event) {
        when(event) {
            is BidIncreased -> this.handle(event)
        }
    }

    override fun childEntities(): ArrayList<ChildEntity> {
        return arrayListOf(this.turnSequence)
    }

    private fun handle(event: BidIncreased) {
        this.amount += event.amount
        this.lastPlayer = event.playerId
    }
}
