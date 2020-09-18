package com.rocketarminek.thousandcardgame.server.game.infrastructure.controller

import com.rocketarminek.thousandcardgame.server.game.domain.event.DealStarted
import com.rocketarminek.thousandcardgame.server.game.domain.event.DealWasBid
import com.rocketarminek.thousandcardgame.server.game.domain.event.Event
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.time.Duration
import kotlin.random.Random

@RestController
class StreamController {
    val playerIds: Array<String> = arrayOf(
            "e88f95e6-f9ed-11ea-adc1-0242ac120002",
            "ec50e27a-f9ed-11ea-adc1-0242ac120002",
            "ef712db6-f9ed-11ea-adc1-0242ac120002"
    )

    @GetMapping(value = ["/v1/events"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun events(): Flux<Event> {
        val static: Flux<Event> = Flux.fromArray(
                arrayOf(
                        DealStarted(
                                "9896a288-f6d3-4e94-85ae-af0ab2770261",
                                playerIds
                        ),
                        DealWasBid("9896a288-f6d3-4e94-85ae-af0ab2770261", "e88f95e6-f9ed-11ea-adc1-0242ac120002", 100),
                        DealWasBid("9896a288-f6d3-4e94-85ae-af0ab2770261", "ec50e27a-f9ed-11ea-adc1-0242ac120002", 100),
                        DealWasBid("9896a288-f6d3-4e94-85ae-af0ab2770261", "ef712db6-f9ed-11ea-adc1-0242ac120002", 100)
                )
        )

        return Flux.merge(
                static,
                Flux.interval(Duration.ofSeconds(3)).map { generateDealWasBid() }
        )
    }

    private fun generateDealWasBid(): DealWasBid {
        val randomPlayer = Random.nextInt(0, 2)
        val randomPoints = Random.nextInt(1, 10)

        return DealWasBid("9896a288-f6d3-4e94-85ae-af0ab2770261", playerIds[randomPlayer], randomPoints)
    }
}