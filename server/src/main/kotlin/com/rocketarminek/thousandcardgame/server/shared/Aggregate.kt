package com.rocketarminek.thousandcardgame.server.shared

abstract class Aggregate() {
    lateinit var id: AggregateId
        protected set
    val uncommittedChanges: ArrayList<Event> = arrayListOf()

    constructor(events: ArrayList<Event>): this() {
        this.initState(events)
    }

    protected abstract fun handle(event: Event)
    protected open fun childEntities(): ArrayList<ChildEntity> {
        return arrayListOf();
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
    private fun initState(events: ArrayList<Event>) {
        for (event in events) {
            this.handleRecursively(event)
        }
    }
}
