package com.rocketarminek.thousandcardgame.server.unit

import com.rocketarminek.thousandcardgame.server.game.domain.model.Game
import com.rocketarminek.thousandcardgame.server.game.infrastructure.repository.EventSourcedGameRepository
import com.rocketarminek.thousandcardgame.server.shared.InMemoryEventStore
import com.rocketarminek.thousandcardgame.server.shared.Repository
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object GameRepositorySpec: Spek({
    describe("The game repository") {
        val repository: Repository<Game> by memoized { EventSourcedGameRepository(InMemoryEventStore()) }
        it("can save the game") {
            repository.save(Game("#123", arrayOf("#22", "#32", "#34")))
            repository.find("#123")?.id shouldBeEqualTo "#123"
            repository.find("#123")?.playerIds shouldBeEqualTo arrayOf("#22", "#32", "#34")
        }
        it("returns null if cannot find any game") {
            repository.find("#123") shouldBe null
        }
        it("commits all changes") {
            val game = Game("#123", arrayOf("#22", "#32", "#34"));
            repository.save(game)
            repository.find("#123")?.uncommittedChanges shouldBeEqualTo arrayListOf()
        }
    }
})
