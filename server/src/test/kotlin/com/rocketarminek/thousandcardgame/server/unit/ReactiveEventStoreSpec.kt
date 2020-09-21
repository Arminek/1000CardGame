package com.rocketarminek.thousandcardgame.server.unit

import com.rocketarminek.thousandcardgame.server.shared.*
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import reactor.core.publisher.UnicastProcessor
import reactor.test.StepVerifier

object ReactiveEventStoreSpec: Spek({
    describe("The reactive event store") {
        val baseStore = mockk<EventStore>()
        val unicastProcessor = mockk<UnicastProcessor<Event>>()
        val fluxSink = mockk<FluxSink<Event>>()
        every { unicastProcessor.sink(FluxSink.OverflowStrategy.LATEST) } returns fluxSink
        every { unicastProcessor.replay().autoConnect() } returns mockk<Flux<Event>>()

        val eventStore: ReactiveEventStore by memoized { UnicastReactiveEventStore(baseStore, unicastProcessor) }

        it("publishes new events into to the reactive stream during storing events") {
            val id = "#123"
            val events = arrayListOf(Created(id, "Jon"), NameChanged(id, "Snow"))
            for (event in events) {
                every { fluxSink.next(event) } returns fluxSink
            }
            justRun { baseStore.save(id, events) }
            eventStore.save(id, events)
        }
        it("loads events as reactive stream") {
            val id = "#123"
            val events = arrayListOf(Created(id, "Jon"), NameChanged(id, "Snow"))
            every { baseStore.load(id) } returns events

            StepVerifier
                    .create(eventStore.load(id))
                    .expectNextCount(2)
                    .expectNext(Created(id, "Jon"))
                    .expectNext(NameChanged(id, "Snow"))
        }
    }
})
