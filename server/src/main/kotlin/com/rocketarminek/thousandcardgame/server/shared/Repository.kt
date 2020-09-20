package com.rocketarminek.thousandcardgame.server.shared

interface Repository<T: Aggregate> {
    fun save(aggregate: T)
    fun find(id: AggregateId): T?
}
