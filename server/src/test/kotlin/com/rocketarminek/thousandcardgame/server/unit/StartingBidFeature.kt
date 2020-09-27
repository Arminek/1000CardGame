package com.rocketarminek.thousandcardgame.server.unit

import com.rocketarminek.thousandcardgame.server.game.domain.event.*
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

        Scenario("Starting the new bid after declaring the previous one") {
            lateinit var game: Game
            Given("there is a game") {
                game = Game(
                        arrayListOf(
                                GameCreated("#123", arrayListOf("playerA", "playerB", "playerC")),
                                BidStarted("#123", "bid1", arrayListOf("playerA", "playerB", "playerC")),
                                BidIncreased("#123", "bid1", "playerA", 100),
                                TurnStarted("#123", "playerB"),
                                BidPassed("#123", "bid1", "playerB"),
                                TurnStarted("#123", "playerC"),
                                BidIncreased("#123", "bid1", "playerC", 10),
                                TurnStarted("#123", "playerA"),
                                BidPassed("#123", "bid1", "playerA"),
                                BidWon("#123", "bid1", "playerC", 110),
                                TurnStarted("#123", "playerC")
                        )
                )
            }
            When("I declare the bid with 110") {
                game.declareBid(110)
            }
            Then("The bid should be declared with 110") {
                game.uncommittedChanges.find { it is BidDeclared }.let {
                    it.shouldNotBeNull()
                    if (it is BidDeclared) {
                        it.id shouldBeEqualTo "#123"
                        it.bidId shouldBeEqualTo "bid1"
                        it.playerId shouldBeEqualTo "playerC"
                        it.amount shouldBeEqualTo 110
                    }
                }
            }
            And("the bid have been started") {
                game.uncommittedChanges.find { it is BidStarted }.let {
                    it.shouldNotBeNull()
                    if (it is BidStarted) {
                        it.id shouldBeEqualTo "#123"
                        it.bidId shouldBeInstanceOf String::class
                        it.playerIds shouldBeEqualTo arrayListOf("playerB", "playerC", "playerA")
                    }
                }
            }
            And("The bid have been increased by 100") {
                game.uncommittedChanges.find { it is BidIncreased }.let {
                    it.shouldNotBeNull()
                    if (it is BidIncreased) {
                        it.id shouldBeEqualTo "#123"
                        it.bidId shouldBeInstanceOf String::class
                        it.playerId shouldBeEqualTo "playerB"
                        it.amount shouldBeEqualTo 100
                    }
                }
            }
            And("the next turn is for the player next in queue") {
                game.uncommittedChanges.find { it is TurnStarted }.let {
                    it.shouldNotBeNull()
                    if (it is TurnStarted) {
                        it.id shouldBeEqualTo "#123"
                        it.playerId shouldBeEqualTo "playerC"
                    }
                }
            }
        }
    }
})