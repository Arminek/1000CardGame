package com.rocketarminek.thousandcardgame.server.game.domain.model

import com.rocketarminek.thousandcardgame.server.game.domain.event.BidPassed
import com.rocketarminek.thousandcardgame.server.game.domain.event.TurnStarted
import com.rocketarminek.thousandcardgame.server.shared.ChildEntity
import com.rocketarminek.thousandcardgame.server.shared.ChildId
import com.rocketarminek.thousandcardgame.server.shared.Event

class TurnSequence(id: ChildId, private val players: ArrayList<PlayerId>): ChildEntity(id) {
    var current: PlayerId = players.first()
        private set
    private var removedIndex: Int? = null

    fun canSwitchTurn(): Boolean = this.players.size != 1

    fun next() {
        this.root?.let {
            this.apply(TurnStarted(it.id, this.players[this.calculateNextIndex()]))
        }
    }

    override fun handle(event: Event) {
        when(event) {
            is TurnStarted -> handle(event)
            is BidPassed -> handle(event)
        }
    }

    private fun handle(event: TurnStarted) {
        this.current = event.playerId
    }

    private fun handle(event: BidPassed) {
        this.removedIndex = this.players.indexOf(event.playerId)
        this.players.remove(event.playerId)
    }

    private fun calculateNextIndex(): Int {
        if (this.players.indexOf(this.current) == -1) {
            this.removedIndex?.let {
                return if (it <= this.players.size) { it } else { 0 }
            }
        }
        val index = this.players.indexOf(this.current) + 1
        if (this.players.lastIndex < index) {
            return 0
        }

        return index
    }
}
