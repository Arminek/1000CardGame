package com.rocketarminek.thousandcardgame.server.unit

import com.rocketarminek.thousandcardgame.server.game.domain.event.GameCreated
import com.rocketarminek.thousandcardgame.server.game.domain.model.Game
import com.rocketarminek.thousandcardgame.server.shared.Event
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldThrow
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object CreateGameFeature : Spek({
    Feature("Create game") {
        Scenario("Creating new game for players") {
            lateinit var game: Game
            val cases = mapOf<String, Array<String>>(
                    "b51315d4-fb56-11ea-adc1-0242ac120002" to
                            arrayOf("bf659c0a-fb56-11ea-adc1-0242ac120002", "c66907bc-fb56-11ea-adc1-0242ac120002"),
                    "63918870-fb61-11ea-adc1-0242ac120002" to
                            arrayOf(
                                    "bf659c0a-fb56-11ea-adc1-0242ac120002",
                                    "c66907bc-fb56-11ea-adc1-0242ac120002",
                                    "6f90b380-fb61-11ea-adc1-0242ac120002"
                            ),
                    "758842d0-fb61-11ea-adc1-0242ac120002" to
                            arrayOf(
                                    "bf659c0a-fb56-11ea-adc1-0242ac120002",
                                    "c66907bc-fb56-11ea-adc1-0242ac120002",
                                    "6f90b380-fb61-11ea-adc1-0242ac120002"
                            )
            )
            for ((gameId, playerIds) in cases) {
                When("I create a game(${gameId.slice(IntRange(0, 7))}) for ${playerIds.size} players") {
                    game = Game(gameId, playerIds)
                }
                Then("the game(${gameId.slice(IntRange(0, 7))}) has been created for ${playerIds.size} players") {
                    game.uncommittedChanges.size shouldBeEqualTo 1
                    val gameCreated: Event? = game.uncommittedChanges.find { event -> event is GameCreated }
                    if (gameCreated is GameCreated) {
                        gameCreated.id shouldBeEqualTo gameId
                        gameCreated.playerIds shouldBeEqualTo playerIds
                    }
                    game.id shouldBeEqualTo gameId
                    game.playerIds shouldBeEqualTo playerIds
                }
            }
        }
        Scenario("Trying to create a game for not supported player amount or none unique players") {
            lateinit var game: Game
            val cases = mapOf<String, Array<String>>(
                    "b51315d4-fb56-11ea-adc1-0242ac120002" to
                            arrayOf("bf659c0a-fb56-11ea-adc1-0242ac120002"),
                    "758842d0-fb61-11ea-adc1-0242ac120002" to
                            arrayOf(
                                    "bf659c0a-fb56-11ea-adc1-0242ac120002",
                                    "c66907bc-fb56-11ea-adc1-0242ac120002",
                                    "6f90b380-fb61-11ea-adc1-0242ac120002",
                                    "7b4028d2-fb61-11ea-adc1-0242ac120002"
                            ),
                    "fd440674-fb65-11ea-adc1-0242ac120002" to
                            arrayOf(
                                    "bf659c0a-fb56-11ea-adc1-0242ac120002",
                                    "019557b4-fb66-11ea-adc1-0242ac120002",
                                    "019557b4-fb66-11ea-adc1-0242ac120002"
                            )
            )
            for ((gameId, playerIds) in cases) {
                Then("I should not be able to create a game(${gameId.slice(IntRange(0, 7))}) for ${playerIds.size} players") {
                    invoking { Game(gameId, playerIds) } shouldThrow IllegalArgumentException::class
                }
            }
        }
    }
})
