package com.rocketarminek.thousandcardgame.server.shared

import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import reactor.core.publisher.UnicastProcessor

class UnicastReactiveEventStore(
        private val store: EventStore,
        processor: UnicastProcessor<Event>
) : ReactiveEventStore {
    private val fluxSink: FluxSink<Event> = processor.sink(FluxSink.OverflowStrategy.LATEST)
    private val hotFlux: Flux<Event> = processor.replay().autoConnect()

    override fun save(id: AggregateId, events: ArrayList<Event>) {
        this.store.save(id, events)
        for (event in events) {
            this.fluxSink.next(event)
        }
    }

    override fun load(id: AggregateId): Flux<Event> {
        val events: ArrayList<Event> = this.store.load(id)

        return Flux.fromIterable(events).concatWith(hotFlux)
    }
}
