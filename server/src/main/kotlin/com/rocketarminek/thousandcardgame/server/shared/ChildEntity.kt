package com.rocketarminek.thousandcardgame.server.shared

abstract class ChildEntity(val id: ChildId) {
    var root: Aggregate? = null

    fun apply(event: Event) {
        this.root?.apply(event)
    }
    fun handleRecursively(event: Event) {
        this.handle(event)
        for (child in this.childEntities()) {
            child.root = this.root
            child.handleRecursively(event)
        }
    }

    protected abstract fun handle(event: Event)
    protected open fun childEntities(): ArrayList<ChildEntity> = arrayListOf()
}
