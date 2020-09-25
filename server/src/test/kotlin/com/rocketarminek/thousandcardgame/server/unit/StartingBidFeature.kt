package com.rocketarminek.thousandcardgame.server.unit

import com.rocketarminek.thousandcardgame.server.game.domain.event.BidIncreased
import com.rocketarminek.thousandcardgame.server.game.domain.event.BidStarted
import com.rocketarminek.thousandcardgame.server.game.domain.model.Game
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldNotBeNull
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object StartingBidFeature: Spek({
    Feature("Starting the bid") {
        Scenario("Creating a new game automatically starts the bid") {
            lateinit var game: Game
            When("I create a game for players") {
                game = Game("#123", arrayListOf("player#123", "player#321", "player#333"))
            }
            Then("the bid have been started") {
                game.uncommittedChanges.find { it is BidStarted }.let {
                    it.shouldNotBeNull()
                    if (it is BidStarted) {
                        it.id shouldBeEqualTo "#123"
                        it.bidId shouldBeInstanceOf String::class
                        it.type shouldBeEqualTo "bid-started"
                        it.playerIds shouldBeEqualTo arrayListOf("player#123", "player#321", "player#333")
                    }
                }
            }
            And("The bid have been increased by 100") {
                game.uncommittedChanges.find { it is BidIncreased }.let {
                    it.shouldNotBeNull()
                    if (it is BidIncreased) {
                        it.id shouldBeEqualTo "#123"
                        it.bidId shouldBeInstanceOf String::class
                        it.playerId shouldBeEqualTo "player#123"
                        it.amount shouldBeEqualTo 100
                    }
                }
            }
        }
    }
})