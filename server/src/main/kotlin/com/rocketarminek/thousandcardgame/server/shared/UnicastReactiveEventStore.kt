package com.rocketarminek.thousandcardgame.server.shared

import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import reactor.core.publisher.UnicastProcessor

class UnicastReactiveEventStore(processor: UnicastProcessor<Event>) : ReactiveEventStore {
    private val fluxSink: FluxSink<Event> = processor.sink(FluxSink.OverflowStrategy.LATEST)
    private val hotFlux: Flux<Event> = processor.replay().autoConnect()

    override fun save(events: ArrayList<Event>) {
        for (event in events) {
            this.fluxSink.next(event)
        }
    }

    override fun load(id: AggregateId): Flux<Event> = hotFlux.filter { event -> event.id == id }
}
