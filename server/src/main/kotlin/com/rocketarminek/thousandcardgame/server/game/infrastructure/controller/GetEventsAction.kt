package com.rocketarminek.thousandcardgame.server.game.infrastructure.controller

import com.rocketarminek.thousandcardgame.server.game.domain.event.DealWasBid
import com.rocketarminek.thousandcardgame.server.game.domain.model.GameId
import com.rocketarminek.thousandcardgame.server.shared.Event
import com.rocketarminek.thousandcardgame.server.shared.EventStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import kotlin.random.Random

@RestController
class GetEventsAction(@Autowired private val store: EventStore) {
    @CrossOrigin(origins = ["http://localhost:4200"])
    @GetMapping(value = ["/v1/games/{id}/events"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun events(@PathVariable id: GameId): Flux<Event> {
        val playerIds = arrayOf(
                "69549ece-5e97-431a-93ba-09d47348eafb",
                "c07dd997-3ef0-41df-a893-91b7cd9e3391",
                "c8f1e7f0-2a73-4555-9e32-8188cc50a7b8"
        )

        return Flux.merge(
                Flux.fromIterable(this.store.load(id))
        )
    }

    private fun generateDealWasBid(playerIds: Array<String>): DealWasBid {
        val randomPlayer = Random.nextInt(0, 2)
        val randomPoints = Random.nextInt(1, 10)

        return DealWasBid("9896a288-f6d3-4e94-85ae-af0ab2770261", playerIds[randomPlayer], randomPoints)
    }
}
