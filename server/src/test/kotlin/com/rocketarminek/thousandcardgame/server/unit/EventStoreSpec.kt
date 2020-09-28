package com.rocketarminek.thousandcardgame.server.unit

import com.rocketarminek.thousandcardgame.server.shared.*
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBeNull
import org.amshove.kluent.shouldNotContain
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object EventStoreSpec: Spek({
    describe("The event store") {
        val eventStore: EventStore by memoized { InMemoryEventStore() }
        it("can save the aggregate events") {
            eventStore.save(
                    "#123",
                    arrayListOf(
                            Created("#123", "Jon"),
                            NameChanged("#123", "Snow")
                    )
            )

            eventStore.load("#123") shouldBeEqualTo arrayListOf(
                    Created("#123", "Jon"),
                    NameChanged("#123", "Snow")
            )
        }
        it("merges all events") {
            eventStore.save(
                    "#123",
                    arrayListOf(
                            Created("#123", "Jon"),
                            NameChanged("#123", "Snow")
                    )
            )
            eventStore.save(
                    "#123",
                    arrayListOf(
                            NameChanged("#123", "Jon")
                    )
            )

            eventStore.load("#123") shouldBeEqualTo arrayListOf(
                    Created("#123", "Jon"),
                    NameChanged("#123", "Snow"),
                    NameChanged("#123", "Jon")
            )
        }
        it("saves copy of the events to prevent memory leaks") {
            val roles = arrayListOf("King in the North", "Lord Commander of the Night's Watch")
            eventStore.save(
                    "#123",
                    arrayListOf(
                            Created("#123", "Jon"),
                            NameChanged("#123", "Snow"),
                            RolesAdded("#123", roles)
                    )
            )
            roles.add("The Bastard of Winterfell")

            eventStore.load("#123").find { it is RolesAdded }.let {
                it.shouldNotBeNull()
                if (it is RolesAdded) {
                    it.id shouldBeEqualTo "#123"
                    it.roles shouldBeEqualTo arrayListOf("King in the North", "Lord Commander of the Night's Watch")
                    it.roles shouldNotContain "The Bastard of Winterfell"
                }
            }
        }
        it("loads from copy of the events to prevent memory leaks") {
            eventStore.save(
                    "#123",
                    arrayListOf(
                            Created("#123", "Jon"),
                            NameChanged("#123", "Snow"),
                            RolesAdded("#123", arrayListOf("King in the North", "Lord Commander of the Night's Watch"))
                    )
            )

            eventStore.load("#123").find { it is RolesAdded }?.let {
                if (it is RolesAdded) {
                    it.roles.add("The Bastard of Winterfell")
                }
            }

            eventStore.load("#123").find { it is RolesAdded }.let {
                it.shouldNotBeNull()
                if (it is RolesAdded) {
                    it.id shouldBeEqualTo "#123"
                    it.roles shouldBeEqualTo arrayListOf("King in the North", "Lord Commander of the Night's Watch")
                    it.roles shouldNotContain "The Bastard of Winterfell"
                }
            }
        }
    }
})

internal data class RolesAdded(override val id: AggregateId, val roles: ArrayList<String>): BaseEvent<RolesAdded>() {
    override fun deepCopy(): Event {
        return this.deepCopy(RolesAdded::class.java)
    }
}
