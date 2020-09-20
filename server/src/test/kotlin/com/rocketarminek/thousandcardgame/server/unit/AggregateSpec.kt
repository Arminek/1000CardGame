package com.rocketarminek.thousandcardgame.server.unit

import com.rocketarminek.thousandcardgame.server.shared.Aggregate
import com.rocketarminek.thousandcardgame.server.shared.AggregateId
import com.rocketarminek.thousandcardgame.server.shared.Event
import org.amshove.kluent.shouldBeEqualTo
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object AggregateSpec: Spek({
    describe("The aggregate") {
        it("has uncommitted events") {
            val aggregate = User("#123", "Jessie Pinkman")

            aggregate.uncommittedChanges shouldBeEqualTo arrayListOf(Created("#123", "Jessie Pinkman"))
            aggregate.id shouldBeEqualTo "#123"
            aggregate.name shouldBeEqualTo "Jessie Pinkman"
        }
        it("can load the state from stream of events") {
            val aggregate = User(
                    arrayListOf<Event>(
                            Created("#321", "Walter White"),
                            NameChanged("#321", "Saul Goodman")
                    )
            )
            aggregate.id shouldBeEqualTo "#321"
            aggregate.name shouldBeEqualTo "Saul Goodman"
        }
    }
})

internal data class Created(val id: AggregateId, val name: String): Event()
internal data class NameChanged(val id: AggregateId, val newName: String): Event()
internal class User: Aggregate {
    lateinit var name: String
        private set

    constructor(id: AggregateId, name: String) {
        this.apply(Created(id, name))
    }

    constructor(events: ArrayList<Event>): super(events) {}

    fun changeName(newName: String) {
        this.apply(NameChanged(this.id, newName))
    }

    override fun handle(event: Event) {
        when(event) {
            is Created -> handle(event)
            is NameChanged -> handle(event)
        }
    }

    private fun handle(event: Created) {
        this.id = event.id
        this.name = event.name
    }
    private fun handle(event: NameChanged) {
        this.name = event.newName
    }
}
