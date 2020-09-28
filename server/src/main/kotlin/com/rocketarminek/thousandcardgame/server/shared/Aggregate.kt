package com.rocketarminek.thousandcardgame.server.shared

abstract class Aggregate() {
    lateinit var id: AggregateId
        protected set
    val uncommittedChanges: MutableList<Event> = mutableListOf()

    constructor(events: List<Event>): this() {
        this.initState(events)
    }

    protected abstract fun handle(event: Event)
    protected open fun childEntities(): List<ChildEntity> {
        return listOf()
    }
    fun apply(event: Event) {
        this.handleRecursively(event)
        this.uncommittedChanges.add(event)
    }
    private fun handleRecursively(event: Event) {
        this.handle(event)
        for (child in this.childEntities()) {
            child.root = this
            child.handleRecursively(event)
        }
    }
    private fun initState(events: List<Event>) {
        for (event in events) {
            this.handleRecursively(event)
        }
    }
}
