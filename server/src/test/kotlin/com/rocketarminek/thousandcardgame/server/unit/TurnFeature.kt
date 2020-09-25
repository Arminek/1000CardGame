package com.rocketarminek.thousandcardgame.server.unit

import com.rocketarminek.thousandcardgame.server.game.domain.event.*
import com.rocketarminek.thousandcardgame.server.game.domain.model.Game
import com.rocketarminek.thousandcardgame.server.shared.Event
import org.amshove.kluent.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object TurnFeature : Spek({
    Feature("Turn") {
        Scenario("Creating new game starts player turn") {
            lateinit var game: Game
            When("I create a new game") {
                game = Game("#123", arrayListOf("playerA", "playerB", "playerC"))
            }
            Then("The second player turn should started") {
                val event: Event? = game.uncommittedChanges.find { it is TurnStarted }
                event.shouldNotBeNull()
                if (event is TurnStarted) {
                    event.id shouldBeEqualTo "#123"
                    event.playerId shouldBeEqualTo "playerB"
                }
            }
        }
        Scenario("Increasing the bid starts turn for next player") {
            lateinit var game: Game
            Given("there is a game") {
                game = Game(
                        arrayListOf(
                                GameCreated("#123", arrayListOf("playerA", "playerB", "playerC")),
                                BidStarted("#123", "bid1", arrayListOf("playerA", "playerB", "playerC")),
                                TurnStarted("#123", "playerA")
                        )
                )
            }
            When("I increase the bid by 10") {
                game.increaseBid(10)
            }
            Then("the next turn is for the player next in queue") {
                val event: Event? = game.uncommittedChanges.find { it is TurnStarted }
                event.shouldNotBeNull()
                if (event is TurnStarted) {
                    event.id shouldBeEqualTo "#123"
                    event.playerId shouldBeEqualTo "playerB"
                }
            }
        }
        Scenario("Passing the bid starts turn for next player") {
            lateinit var game: Game
            Given("there is a game") {
                game = Game(
                        arrayListOf(
                                GameCreated("#123", arrayListOf("playerA", "playerB", "playerC")),
                                BidStarted("#123", "bid1", arrayListOf("playerA", "playerB", "playerC")),
                                BidIncreased("#123", "bid1", "playerA", 100),
                                TurnStarted("#123", "playerB")
                        )
                )
            }
            When("I pass the bid") {
                game.passBid()
            }
            Then("the next turn is for the next player") {
                val event: Event? = game.uncommittedChanges.find { it is TurnStarted }
                event.shouldNotBeNull()
                if (event is TurnStarted) {
                    event.id shouldBeEqualTo "#123"
                    event.playerId shouldBeEqualTo "playerC"
                }
            }
        }
        Scenario("Players that passes cannot take any action") {
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
                                BidIncreased("#123", "bid1", "playerC", 110),
                                TurnStarted("#123", "playerA"),
                                BidPassed("#123", "bid1", "playerA")
                        )
                )
            }
            When("I pass the bid") {
                game.passBid()
                game.increaseBid(10)
            }
            Then("nothing should happened") {
                game.uncommittedChanges.find { it is BidPassed }.shouldBeNull()
                game.uncommittedChanges.find { it is BidIncreased }.shouldBeNull()
            }
        }
    }
})
