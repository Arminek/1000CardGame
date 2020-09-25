package com.rocketarminek.thousandcardgame.server.unit

import com.rocketarminek.thousandcardgame.server.shared.*
import org.amshove.kluent.shouldBeEqualTo
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.util.*
import kotlin.collections.ArrayList

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
                    arrayListOf(
                            Created("#321", "Walter White"),
                            NameChanged("#321", "Saul Goodman")
                    )
            )
            aggregate.id shouldBeEqualTo "#321"
            aggregate.name shouldBeEqualTo "Saul Goodman"
        }
        it("has uncommitted events from child entities") {
            val aggregate = User("#123", "Jessie Pinkman")

            aggregate.addAddress("Warsaw")
            aggregate.address?.city shouldBeEqualTo "Warsaw"
            aggregate.address?.change("London")
            aggregate.address?.city shouldBeEqualTo "London"
            aggregate.addressChanged shouldBeEqualTo true
        }
        it("can init with child entites") {
            val aggregate = User(
                    arrayListOf(
                            Created("#321", "Walter White"),
                            NameChanged("#321", "Saul Goodman"),
                            AddressAdded("#12", "Warsaw"),
                            AddressChanged("#12", "London")
                    )
            )
            aggregate.id shouldBeEqualTo "#321"
            aggregate.name shouldBeEqualTo "Saul Goodman"
            aggregate.address?.city shouldBeEqualTo "London"
            aggregate.addressChanged shouldBeEqualTo true
        }
    }
})

internal data class Created(override val id: AggregateId, val name: String): Event
internal data class NameChanged(override val id: AggregateId, val newName: String): Event
internal data class AddressAdded(override val id: ChildId, val city: String): Event
internal data class AddressChanged(override val id: ChildId, val newCity: String): Event
internal class User: Aggregate {
    var addressChanged: Boolean = false
        private set
    var name: String? = null
        private set
    var address: Address? = null
        private set

    constructor(id: AggregateId, name: String) {
        this.apply(Created(id, name))
    }

    constructor(events: ArrayList<Event>): super(events)

    fun changeName(newName: String) {
        val address = this.address
        if (address != null) {
            this.apply(NameChanged(address.id, newName))
        }
    }

    fun addAddress(city: String) {
        this.apply(AddressAdded(UUID.randomUUID().toString(), city))
    }

    override fun handle(event: Event) {
        when(event) {
            is Created -> handle(event)
            is NameChanged -> handle(event)
            is AddressAdded -> handle(event)
            is AddressChanged -> handle(event)
        }
    }
    override fun childEntities(): ArrayList<ChildEntity> {
        val address = this.address ?: return arrayListOf()

        return arrayListOf(address)
    }

    private fun handle(event: Created) {
        this.id = event.id
        this.name = event.name
    }
    private fun handle(event: NameChanged) {
        this.name = event.newName
    }
    private fun handle(event: AddressAdded) {
        this.address = Address(event.id, event.city)
    }
    private fun handle(event: AddressChanged) {
        this.addressChanged = true
    }
}
internal class Address(id: ChildId, city: String): ChildEntity(id) {
    var city = city
        private set

    fun change(newCity: String) {
        this.apply(AddressChanged(this.id, newCity))
    }

    override fun handle(event: Event) {
        when (event) {
            is AddressChanged -> handle(event)
        }
    }

    private fun handle(event: AddressChanged) {
        this.city = event.newCity
    }
}
