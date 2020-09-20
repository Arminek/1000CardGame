package com.rocketarminek.thousandcardgame.server.unit

import com.rocketarminek.thousandcardgame.server.shared.*
import org.amshove.kluent.shouldBeEqualTo
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object EventStoreSpec: Spek({
    describe("The event store") {
        val eventStore: EventStore by memoized { InMemoryEventStore() }
        it("can save the aggregate events") {
            eventStore.save(
                    "#123",
                    arrayListOf<Event>(
                            Created("#123", "Jon"),
                            NameChanged("#123", "Snow")
                    )
            )

            eventStore.load("#123") shouldBeEqualTo arrayListOf<Event>(
                    Created("#123", "Jon"),
                    NameChanged("#123", "Snow")
            )
        }
    }
})
