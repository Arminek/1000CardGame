package com.rocketarminek.thousandcardgame.server.unit

import com.rocketarminek.thousandcardgame.server.game.domain.event.BidIncreased
import com.rocketarminek.thousandcardgame.server.game.domain.event.BidStarted
import com.rocketarminek.thousandcardgame.server.game.domain.model.Game
import com.rocketarminek.thousandcardgame.server.shared.Event
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
                val event: Event? = game.uncommittedChanges.find { it is BidStarted }
                event.shouldNotBeNull()
                if (event is BidStarted) {
                    event.id shouldBeEqualTo "#123"
                    event.bidId shouldBeInstanceOf String::class
                    event.type shouldBeEqualTo "bid-started"
                    event.playerIds shouldBeEqualTo arrayListOf("player#123", "player#321", "player#333")
                }
            }
            And("The bid have been increased by 100") {
                val event: Event? = game.uncommittedChanges.find { it is BidIncreased }
                event.shouldNotBeNull()
                if (event is BidIncreased) {
                    event.id shouldBeEqualTo "#123"
                    event.bidId shouldBeInstanceOf String::class
                    event.playerId shouldBeEqualTo "player#123"
                    event.amount shouldBeEqualTo 100
                }
            }
        }
    }
})