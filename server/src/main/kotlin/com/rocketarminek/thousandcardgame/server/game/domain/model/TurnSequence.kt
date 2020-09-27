package com.rocketarminek.thousandcardgame.server.game.domain.model

import com.rocketarminek.thousandcardgame.server.game.domain.event.BidPassed
import com.rocketarminek.thousandcardgame.server.game.domain.event.TurnStarted
import com.rocketarminek.thousandcardgame.server.shared.ChildEntity
import com.rocketarminek.thousandcardgame.server.shared.ChildId
import com.rocketarminek.thousandcardgame.server.shared.Event

class TurnSequence(id: ChildId, private val sequence: ArrayList<PlayerId>): ChildEntity(id) {
    val playerIds: ArrayList<PlayerId>
    var current: PlayerId = sequence.first()
        private set
    private var removedIndex: Int? = null

    init {
        val playerIds = arrayListOf<PlayerId>()
        playerIds.addAll(sequence)
        this.playerIds = playerIds
    }

    fun canSwitchTurn(): Boolean = this.sequence.size != 1

    fun next() {
        this.root?.let {
            this.apply(TurnStarted(it.id, this.getNext()))
        }
    }

    fun nextSequence(id: BidId): TurnSequence {
        val playerId = this.playerIds.first()
        val newSequence = this.playerIds
        newSequence.remove(playerId)
        newSequence.add(playerId)

        return TurnSequence(id, newSequence)
    }

    fun getNext(): PlayerId {
        if (this.sequence.indexOf(this.current) == -1) {
            this.removedIndex?.let {
                return if (it < this.sequence.size) { this.sequence[it] } else { this.sequence.first() }
            }
        }
        val index = this.sequence.indexOf(this.current) + 1
        if (this.sequence.lastIndex < index) {
            return this.sequence.first()
        }

        return this.sequence[index]
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
        this.removedIndex = this.sequence.indexOf(event.playerId)
        this.sequence.remove(event.playerId)
    }
}
