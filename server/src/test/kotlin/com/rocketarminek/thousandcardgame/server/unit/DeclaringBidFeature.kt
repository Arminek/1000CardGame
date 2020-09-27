package com.rocketarminek.thousandcardgame.server.unit

import com.rocketarminek.thousandcardgame.server.game.domain.event.*
import com.rocketarminek.thousandcardgame.server.game.domain.model.Game
import org.amshove.kluent.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object DeclaringBidFeature : Spek({
    Feature("Declaring the bid") {
        Scenario("Declaring the bid") {
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
            Then("The bid should not be increased") {
                game.uncommittedChanges.find { it is BidIncreased }.shouldBeNull()
            }
            And("The bid should be declared with 110") {
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
        }

        Scenario("Cannot declare the bid if there is no bid in progress") {
            lateinit var game: Game
            Given("there is a game") {
                game = Game(
                        arrayListOf(
                                GameCreated("#123", arrayListOf("playerA", "playerB", "playerC")),
                                TurnStarted("#123", "playerB")
                        )
                )
            }
            Then("I should not be able to declare the bid") {
                invoking { game.declareBid(100) } shouldThrow IllegalArgumentException::class
            }
        }

        Scenario("Cannot declare the bid if the bid still is not settled") {
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
                                TurnStarted("#123", "playerA")
                        )
                )
            }
            Then("I should not be able to declare the bid") {
                invoking { game.declareBid(100) } shouldThrow IllegalArgumentException::class
            }
        }

        Scenario("Declaring the bid with increasing the bid amount") {
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
            When("I declare the bid with 200") {
                game.declareBid(200)
            }
            Then("The bid should be increased with 90") {
                game.uncommittedChanges.find { it is BidIncreased }.let {
                    it.shouldNotBeNull()
                    if (it is BidIncreased) {
                        it.id shouldBeEqualTo "#123"
                        it.bidId shouldBeEqualTo "bid1"
                        it.playerId shouldBeEqualTo "playerC"
                        it.amount shouldBeEqualTo 90
                    }
                }
            }
            And("The bid should be declared with 200") {
                game.uncommittedChanges.find { it is BidDeclared }.let {
                    it.shouldNotBeNull()
                    if (it is BidDeclared) {
                        it.id shouldBeEqualTo "#123"
                        it.bidId shouldBeEqualTo "bid1"
                        it.playerId shouldBeEqualTo "playerC"
                        it.amount shouldBeEqualTo 200
                    }
                }
            }
        }

        Scenario("Trying to declaring the bid lower then the current one") {
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
            Then("I should not be able to declare the bid with 100") {
                invoking { game.declareBid(100) } shouldThrow IllegalArgumentException::class
            }
        }

        Scenario("Trying to declaring the bid higher than 300") {
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
            Then("I should not be able to declare the bid with 310") {
                invoking { game.declareBid(310) } shouldThrow IllegalArgumentException::class
                invoking { game.declareBid(300) } shouldNotThrow IllegalArgumentException::class
            }
        }

        Scenario("Trying to declare the bid second time") {
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
                                TurnStarted("#123", "playerC"),
                                BidDeclared("#123", "bid1", "playerC", 110)
                        )
                )
            }
            Then("I should not be able to declare the bid again") {
                invoking { game.declareBid(200) } shouldThrow IllegalArgumentException::class
                invoking { game.declareBid(110) } shouldThrow IllegalArgumentException::class
            }
        }
    }
})
