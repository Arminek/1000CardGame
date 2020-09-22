package com.rocketarminek.thousandcardgame.server.unit

import com.rocketarminek.thousandcardgame.server.game.domain.event.*
import com.rocketarminek.thousandcardgame.server.game.domain.model.Game
import com.rocketarminek.thousandcardgame.server.shared.Event
import org.amshove.kluent.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object BiddingFeature : Spek({
    Feature("Bidding") {
        Scenario("Creating new game starts bidding by opening with 100 bid by first player") {
            lateinit var game: Game
            When("I create a new game") {
                game = Game("#123", arrayOf("player#123", "player#321", "player#333"))
            }
            Then("The bid have been increased by 100") {
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

        Scenario("Creating new game starts player turn") {
            lateinit var game: Game
            When("I create a new game") {
                game = Game("#123", arrayOf("player#123", "player#321", "player#333"))
            }
            Then("The second player turn should started") {
                val event: Event? = game.uncommittedChanges.find { it is TurnStarted }
                event.shouldNotBeNull()
                if (event is TurnStarted) {
                    event.id shouldBeEqualTo "#123"
                    event.playerId shouldBeEqualTo "player#321"
                }
            }
        }

        Scenario("Increasing the bid") {
            lateinit var game: Game
            Given("there is a game in progress") {
                game = Game(
                        arrayListOf(
                                GameCreated("#123", arrayOf("player#123", "player#321", "player#333")),
                                BidStarted("#123", "bid#123", arrayOf("player#123", "player#321", "player#333")),
                                BidIncreased("#123", "bid#123", "player#123", 100),
                                TurnStarted("#123", "player#321")
                        )
                )
            }
            When("I increase the bid by 10") {
                game.increaseBid(10)
            }
            Then("The bid should be increased by 10") {
                val event: Event? = game.uncommittedChanges.find { it is BidIncreased }
                event.shouldNotBeNull()
                if (event is BidIncreased) {
                    event.id shouldBeEqualTo "#123"
                    event.bidId shouldBeEqualTo "bid#123"
                    event.playerId shouldBeEqualTo "player#321"
                    event.amount shouldBeEqualTo 10
                }
            }
            And("The third player turn should started") {
                val event: Event? = game.uncommittedChanges.find { it is TurnStarted }
                event.shouldNotBeNull()
                if (event is TurnStarted) {
                    event.id shouldBeEqualTo "#123"
                    event.playerId shouldBeEqualTo "player#333"
                }
            }
        }

        Scenario("Passing the bid") {
            lateinit var game: Game
            Given("there is a game in progress") {
                game = Game(
                        arrayListOf(
                                GameCreated("#123", arrayOf("player#123", "player#321", "player#333")),
                                BidStarted("#123", "bid#123", arrayOf("player#123", "player#321", "player#333")),
                                BidIncreased("#123", "bid#123", "player#123", 100),
                                TurnStarted("#123", "player#321")
                        )
                )
            }
            When("I pass the bid") {
                game.passBid()
            }
            Then("The bid should be passed") {
                val event: Event? = game.uncommittedChanges.find { it is BidPassed }
                event.shouldNotBeNull()
                if (event is BidPassed) {
                    event.id shouldBeEqualTo "#123"
                    event.bidId shouldBeEqualTo "bid#123"
                    event.playerId shouldBeEqualTo "player#321"
                }
            }
            And("The third player turn should started") {
                val event: Event? = game.uncommittedChanges.find { it is TurnStarted }
                event.shouldNotBeNull()
                if (event is TurnStarted) {
                    event.id shouldBeEqualTo "#123"
                    event.playerId shouldBeEqualTo "player#333"
                }
            }
        }

        Scenario("Trying to increase the bid when there is no bidding in progress") {
            lateinit var game: Game
            Given("there is a game in progress") {
                game = Game(
                        arrayListOf(
                                GameCreated("#123", arrayOf("player#123", "player#321", "player#333")),
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
                                GameCreated("#123", arrayOf("player#123", "player#321", "player#333")),
                                TurnStarted("#123", "player#321")
                        )
                )
            }
            Then("I should not be able to pass the bid by 10") {
                invoking { game.passBid() } shouldThrow IllegalArgumentException::class
            }
        }
    }
})
