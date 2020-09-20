package com.rocketarminek.thousandcardgame.server.shared

abstract class Aggregate() {
    lateinit var id: AggregateId
        protected set
    val uncommittedChanges: ArrayList<Event> = arrayListOf<Event>()

    constructor(events: ArrayList<Event>): this() {
        this.handleRecursively(events)
    }

    protected abstract fun handle(event: Event)
    protected fun apply(event: Event) {
        this.uncommittedChanges.add(event)
        this.handle(event)
    }
    private fun handleRecursively(events: ArrayList<Event>) {
        for (event in events) {
            this.handle(event)
        }
    }
}
