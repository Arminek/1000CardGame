package com.rocketarminek.thousandcardgame.server.game.domain.model

data class Bid(val id: BidId, val gameId: GameId, var amount: Int? = null, var winingPlayer: PlayerId? = null)
