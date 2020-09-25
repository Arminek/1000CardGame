package com.rocketarminek.thousandcardgame.server.shared

abstract class ChildEntity {
    lateinit var id: ChildId
        protected set
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
    protected fun childEntities(): ArrayList<ChildEntity> = arrayListOf()
}
