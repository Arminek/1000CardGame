package com.rocketarminek.thousandcardgame.server.unit

import com.rocketarminek.thousandcardgame.server.shared.*
import org.amshove.kluent.shouldBeEqualTo
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object RepositorySpec: Spek({
    describe("The repository") {
        val repository: Repository<User> by memoized { EventSourcedUserRepository(InMemoryEventStore()) }
        it("can save the aggregate events") {
            val user = User("#123", "Jon")
            user.changeName("Snow")
            repository.save(user)

            repository.find("#123")?.name shouldBeEqualTo user.name
            repository.find("#123")?.id shouldBeEqualTo user.id
        }
    }
})

internal class EventSourcedUserRepository(private val store: EventStore): Repository<User> {
    override fun save(aggregate: User) {
        this.store.save(aggregate.id, aggregate.uncommittedChanges)
    }

    override fun find(id: AggregateId): User? {
        return when(store.load(id)) {
            null -> null
            else -> User(store.load(id)!!)
        }
    }
}
