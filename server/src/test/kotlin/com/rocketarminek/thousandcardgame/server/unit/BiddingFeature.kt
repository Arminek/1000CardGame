package com.rocketarminek.thousandcardgame.server.unit

import com.rocketarminek.thousandcardgame.server.game.domain.event.*
import com.rocketarminek.thousandcardgame.server.game.domain.model.Game
import org.amshove.kluent.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object BiddingFeature : Spek({
    Feature("Bidding") {
        Scenario("Increasing the bid") {
            lateinit var game: Game
            Given("there is a game in progress") {
                game = Game(
                        arrayListOf(
                                GameCreated("#123", arrayListOf("player#123", "player#321", "player#333")),
                                BidStarted("#123", "bid#123", arrayListOf("player#123", "player#321", "player#333")),
                                BidIncreased("#123", "bid#123", "player#123", 100),
                                TurnStarted("#123", "player#321")
                        )
                )
            }
            When("I increase the bid by 10") {
                game.increaseBid(10)
            }
            Then("The bid should be increased by 10") {
                game.uncommittedChanges.find { it is BidIncreased }.let {
                    it.shouldNotBeNull()
                    if (it is BidIncreased) {
                        it.id shouldBeEqualTo "#123"
                        it.bidId shouldBeEqualTo "bid#123"
                        it.playerId shouldBeEqualTo "player#321"
                        it.amount shouldBeEqualTo 10
                    }
                }
            }
        }

        Scenario("Passing the bid") {
            lateinit var game: Game
            Given("there is a game in progress") {
                game = Game(
                        arrayListOf(
                                GameCreated("#123", arrayListOf("player#123", "player#321", "player#333")),
                                BidStarted("#123", "bid#123", arrayListOf("player#123", "player#321", "player#333")),
                                BidIncreased("#123", "bid#123", "player#123", 100),
                                TurnStarted("#123", "player#321")
                        )
                )
            }
            When("I pass the bid") {
                game.passBid()
            }
            Then("The bid should be passed") {
                game.uncommittedChanges.find { it is BidPassed }.let {
                    it.shouldNotBeNull()
                    if (it is BidPassed) {
                        it.id shouldBeEqualTo "#123"
                        it.bidId shouldBeEqualTo "bid#123"
                        it.playerId shouldBeEqualTo "player#321"
                    }
                }
            }
        }

        Scenario("Trying to increase the bid when there is no bidding in progress") {
            lateinit var game: Game
            Given("there is a game in progress") {
                game = Game(
                        arrayListOf(
                                GameCreated("#123", arrayListOf("player#123", "player#321", "player#333")),
                                TurnStarted("#123", "player#321")
                        )
                )
            }
            Then("I should not be able to increase the bid by 10") {
                invoking { game.increaseBid(10) } shouldThrow IllegalArgumentException::class
            }
        }

        Scenario("Trying to pass the bid when there is no bidding in progress") {
            lateinit var game: Game
            Given("there is a game in progress") {
                game = Game(
                        arrayListOf(
                                GameCreated("#123", arrayListOf("player#123", "player#321", "player#333")),
                                TurnStarted("#123", "player#321")
                        )
                )
            }
            Then("I should not be able to pass the bid") {
                invoking { game.passBid() } shouldThrow IllegalArgumentException::class
            }
        }

        Scenario("Trying to increase the bid over 300") {
            lateinit var game: Game
            Given("there is a game in progress") {
                game = Game(
                        arrayListOf(
                                GameCreated("#123", arrayListOf("player#123", "player#321", "player#333")),
                                BidStarted("#123", "bid#123", arrayListOf("player#123", "player#321", "player#333")),
                                BidIncreased("#123", "bid#123", "player#123", 100),
                                TurnStarted("#123", "player#321"),
                                BidIncreased("#123", "bid#123", "player#321", 200),
                                TurnStarted("#123", "player#333")
                        )
                )
            }
            Then("I should not be able to increase the bid by 10") {
                invoking { game.increaseBid(10) } shouldThrow IllegalArgumentException::class
            }
        }

        Scenario("Increasing the bid to 300") {
            lateinit var game: Game
            Given("there is a game in progress") {
                game = Game(
                        arrayListOf(
                                GameCreated("#123", arrayListOf("player#123", "player#321", "player#333")),
                                BidStarted("#123", "bid#123", arrayListOf("player#123", "player#321", "player#333")),
                                BidIncreased("#123", "bid#123", "player#123", 100),
                                TurnStarted("#123", "player#321"),
                                BidIncreased("#123", "bid#123", "player#321", 190),
                                TurnStarted("#123", "player#333")
                        )
                )
            }
            When("I increase the bid by 10") {
                game.increaseBid(10)
            }
            Then("The bid should be increased by 10") {
                game.uncommittedChanges.find { it is BidIncreased }.let {
                    it.shouldNotBeNull()
                    if (it is BidIncreased) {
                        it.id shouldBeEqualTo "#123"
                        it.bidId shouldBeEqualTo "bid#123"
                        it.playerId shouldBeEqualTo "player#333"
                        it.amount shouldBeEqualTo 10
                    }
                }
            }
        }
    }
})
