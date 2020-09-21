package com.rocketarminek.thousandcardgame.server.unit

import com.rocketarminek.thousandcardgame.server.game.domain.model.Game
import com.rocketarminek.thousandcardgame.server.game.infrastructure.repository.EventSourcedGameRepository
import com.rocketarminek.thousandcardgame.server.shared.EventStore
import com.rocketarminek.thousandcardgame.server.shared.InMemoryEventStore
import com.rocketarminek.thousandcardgame.server.shared.ReactiveEventStore
import com.rocketarminek.thousandcardgame.server.shared.Repository
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import reactor.core.publisher.Flux

object GameRepositorySpec: Spek({
    describe("The game repository") {
        val reactiveEventStore: ReactiveEventStore = mockk()
        val inMemoryEventStore: EventStore by memoized { InMemoryEventStore() }
        val repository: Repository<Game> by memoized { EventSourcedGameRepository(reactiveEventStore, inMemoryEventStore) }

        it("can save the game") {
            val game = Game("#123", arrayOf("#22", "#32", "#34"))
            justRun { reactiveEventStore.save(game.uncommittedChanges) }
            every { reactiveEventStore.load(game.id) } returns Flux.fromIterable(game.uncommittedChanges)

            repository.save(game)
            repository.find("#123")?.id shouldBeEqualTo "#123"
            repository.find("#123")?.playerIds shouldBeEqualTo arrayOf("#22", "#32", "#34")
            repository.find("#123")?.uncommittedChanges shouldBeEqualTo arrayListOf()
        }
        it("returns null if cannot find any game") {
            repository.find("#123") shouldBe null
        }
    }
})
