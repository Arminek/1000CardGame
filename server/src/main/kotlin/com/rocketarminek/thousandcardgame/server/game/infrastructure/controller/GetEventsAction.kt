package com.rocketarminek.thousandcardgame.server.game.infrastructure.controller

import com.rocketarminek.thousandcardgame.server.game.domain.model.GameId
import com.rocketarminek.thousandcardgame.server.shared.Event
import com.rocketarminek.thousandcardgame.server.shared.ReactiveEventStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
class GetEventsAction(@Autowired private val store: ReactiveEventStore) {
    @CrossOrigin
    @GetMapping(value = ["/v1/games/{id}/events"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun events(@PathVariable id: GameId): Flux<Event> {
        return this.store.load(id)
    }
}
