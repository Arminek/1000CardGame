package com.rocketarminek.thousandcardgame.server.game.infrastructure

import com.rocketarminek.thousandcardgame.server.game.domain.model.Game
import com.rocketarminek.thousandcardgame.server.game.infrastructure.repository.EventSourcedGameRepository
import com.rocketarminek.thousandcardgame.server.shared.EventStore
import com.rocketarminek.thousandcardgame.server.shared.InMemoryEventStore
import com.rocketarminek.thousandcardgame.server.shared.Repository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Configuration {
    @Bean
    fun gameRepository(@Autowired eventStore: EventStore): Repository<Game> = EventSourcedGameRepository(eventStore)
    @Bean
    fun eventStore(): EventStore = InMemoryEventStore()
}
