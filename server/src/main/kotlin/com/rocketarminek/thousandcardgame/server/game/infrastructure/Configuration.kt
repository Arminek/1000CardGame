package com.rocketarminek.thousandcardgame.server.game.infrastructure

import com.rocketarminek.thousandcardgame.server.game.domain.model.Game
import com.rocketarminek.thousandcardgame.server.game.infrastructure.repository.EventSourcedGameRepository
import com.rocketarminek.thousandcardgame.server.shared.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.UnicastProcessor

@Configuration
class Configuration {
    @Bean
    fun gameRepository(
            @Autowired eventStore: ReactiveEventStore,
            @Autowired inMemoryStore: EventStore
    ): Repository<Game> = EventSourcedGameRepository(eventStore, inMemoryStore)
    @Bean
    fun eventStore(): EventStore = InMemoryEventStore()
    @Bean
    fun reactiveStore(
            @Autowired eventStore: EventStore,
            @Autowired unicastProcessor: UnicastProcessor<Event>
    ): ReactiveEventStore =
            UnicastReactiveEventStore(unicastProcessor)
    @Bean
    fun unicastProcessor(): UnicastProcessor<Event> = UnicastProcessor.create()
}
