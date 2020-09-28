package com.rocketarminek.thousandcardgame.server.game.domain.model

import com.rocketarminek.thousandcardgame.server.game.domain.event.*
import com.rocketarminek.thousandcardgame.server.shared.ChildEntity
import com.rocketarminek.thousandcardgame.server.shared.Event
import java.util.*
import kotlin.collections.ArrayList

class Bid(id: BidId, playerIds: ArrayList<PlayerId>): ChildEntity(id) {
    var amount = 0
        private set
    var lastPlayer: PlayerId? = null
        private set
    private val turnSequence: TurnSequence = TurnSequence(id, playerIds)
    private var won: Boolean = false
    private var declared: Boolean = false

    fun increase(amount: Int) {
        if (this.won) {
            throw IllegalArgumentException("Player ${this.lastPlayer} already won the bid!")
        }
        this.root?.let {
            if (this.amount + amount > 300) {
                throw IllegalArgumentException("Cannot increase the bid over 300")
            }
            if (this.turnSequence.canSwitchTurn()) {
                this.apply(BidIncreased(it.id, this.id, this.turnSequence.current, amount))
                if (this.isWinning()) {
                    this.lastPlayer?.let { player ->
                        BidWon(it.id, this.id, player, this.amount)
                    }?.let { event ->
                        this.apply(event)
                    }
                }
                this.turnSequence.next()
            }
        }
    }

    fun pass() {
        if (this.won) {
            throw IllegalArgumentException("Player ${this.lastPlayer} already won the bid!")
        }
        this.root?.let {
            if (this.turnSequence.canSwitchTurn()) {
                this.apply(BidPassed(it.id, this.id, this.turnSequence.current))
                if (this.isWinning()) {
                    this.lastPlayer?.let { player ->
                        BidWon(it.id, this.id, player, this.amount)
                    }?.let { event -> this.apply(event) }
                }
                this.turnSequence.next()
            }
        }
    }

    fun declare(amount: Int) {
        if (this.declared) {
            throw IllegalArgumentException("Cannot declare already declared bid!")
        }
        if (amount > 300) {
            throw IllegalArgumentException("Cannot increase the bid over 300!")
        }
        if (amount < this.amount) {
            throw IllegalArgumentException("Cannot decrease current bid!")
        }
        if (!this.won) {
            throw IllegalArgumentException("Cannot declare not settled bid!")
        }
        if (amount != this.amount) {
            val increaseAmount = amount - this.amount
            this.root?.let { this.apply(BidIncreased(it.id, this.id, this.turnSequence.current, increaseAmount)) }
        }
        this.root?.let { this.apply(BidDeclared(it.id, this.id, this.turnSequence.current, this.amount)) }
        startNewBid()
    }

    override fun handle(event: Event) {
        when(event) {
            is BidIncreased -> this.handle(event)
            is BidWon -> this.handle(event)
            is BidDeclared -> this.handle(event)
        }
    }

    override fun childEntities(): ArrayList<ChildEntity> {
        return arrayListOf(this.turnSequence)
    }

    private fun handle(event: BidIncreased) {
        this.amount += event.amount
        this.lastPlayer = event.playerId
    }

    private fun handle(event: BidWon) {
        this.won = true
    }

    private fun handle(event: BidDeclared) {
        this.declared = true
    }

    private fun isWinning() = !this.turnSequence.canSwitchTurn() && !this.won

    private fun startNewBid() {
        val bidId = UUID.randomUUID().toString()
        val nextSequence = this.turnSequence.nextSequence(bidId)
        this.root?.let { this.apply(BidStarted(it.id, bidId, nextSequence.playerIds)) }
        this.root?.let { this.apply(BidIncreased(it.id, bidId, nextSequence.current, 100)) }
        this.root?.let { this.apply(TurnStarted(it.id, nextSequence.getNext())) }
    }
}
