package com.rocketarminek.thousandcardgame.server.unit

import com.rocketarminek.thousandcardgame.server.game.domain.event.DealStarted
import com.rocketarminek.thousandcardgame.server.game.domain.event.DealWasBid
import com.rocketarminek.thousandcardgame.server.game.domain.event.Event
import com.rocketarminek.thousandcardgame.server.game.infrastructure.controller.StreamController
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.time.Duration

object StreamFeature : Spek({
    Feature("Getting stream of game events") {
        val controller: StreamController by memoized { StreamController() }
        Scenario("Returning hardcoded stream of two strings") {
            lateinit var result: Flux<Event>
            When("I ask for stream of events") {
                result = controller.events()

            }
            Then("I get two events") {
                StepVerifier
                        .create(result)
                        .expectNextCount(4)
                        .verifyTimeout(Duration.ofSeconds(1))
            }
            And("These events are") {
                StepVerifier
                        .create(result)
                        .expectNextMatches { it is DealStarted }
                        .expectNext(
                                DealWasBid(
                                        "9896a288-f6d3-4e94-85ae-af0ab2770261",
                                        "e88f95e6-f9ed-11ea-adc1-0242ac120002",
                                        100
                                )
                        )
                        .expectNext(
                                DealWasBid(
                                        "9896a288-f6d3-4e94-85ae-af0ab2770261",
                                        "ec50e27a-f9ed-11ea-adc1-0242ac120002",
                                        100
                                )
                        )
                        .expectNext(
                                DealWasBid(
                                        "9896a288-f6d3-4e94-85ae-af0ab2770261",
                                        "ef712db6-f9ed-11ea-adc1-0242ac120002",
                                        100
                                )
                        )
                        .verifyTimeout(Duration.ofSeconds(1))
            }
        }
    }
})
