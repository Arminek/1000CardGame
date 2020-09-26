package com.rocketarminek.thousandcardgame.server.unit

import com.rocketarminek.thousandcardgame.server.game.domain.event.*
import com.rocketarminek.thousandcardgame.server.game.domain.model.Game
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBeNull
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object WinningBidFeature : Spek({
    Feature("Winning bid") {
        Scenario("Winning the bid after every one passes") {
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
            When("I pass the bid") {
                game.passBid()
            }
            Then("player with highest bid should won") {
                game.uncommittedChanges.find { it is BidWon }.let {
                    it.shouldNotBeNull()
                    if (it is BidWon) {
                        it.id shouldBeEqualTo "#123"
                        it.playerId shouldBeEqualTo "playerC"
                        it.amount shouldBeEqualTo 110
                        it.bidId shouldBeEqualTo "bid1"
                    }
                }
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
