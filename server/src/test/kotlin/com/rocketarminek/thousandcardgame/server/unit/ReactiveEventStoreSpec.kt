package com.rocketarminek.thousandcardgame.server.unit

import com.rocketarminek.thousandcardgame.server.shared.*
import io.mockk.every
import io.mockk.mockk
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import reactor.core.publisher.UnicastProcessor
import reactor.test.StepVerifier

object ReactiveEventStoreSpec: Spek({
    describe("The reactive event store") {
        val unicastProcessor = mockk<UnicastProcessor<Event>>()
        val fluxSink = mockk<FluxSink<Event>>()
        every { unicastProcessor.sink(FluxSink.OverflowStrategy.LATEST) } returns fluxSink

        val eventStore: ReactiveEventStore by memoized { UnicastReactiveEventStore(unicastProcessor) }

        it("publishes new events into to the reactive stream during storing events") {
            every { unicastProcessor.replay().autoConnect() } returns Flux.empty()
            val id = "#123"
            val events = arrayListOf(Created(id, "Jon"), NameChanged(id, "Snow"))
            for (event in events) {
                every { fluxSink.next(event) } returns fluxSink
            }
            eventStore.save(events)
        }
        it("loads events as reactive stream") {
            val events = arrayListOf(
                    Created("#123", "Jon"),
                    NameChanged("#123", "Snow"),
                    Created("#321", "Walter"),
                    Created("#444", "Mike")
            )
            val hotFlux = Flux.fromIterable(events)
            every { unicastProcessor.replay().autoConnect() } returns hotFlux

            StepVerifier
                    .create(eventStore.load("#123"))
                    .expectNextCount(2)
                    .verifyComplete()

            StepVerifier
                    .create(eventStore.load("#321"))
                    .expectNextCount(1)
                    .verifyComplete()

            StepVerifier
                    .create(eventStore.load("#444"))
                    .expectNextCount(1)
                    .verifyComplete()

            StepVerifier
                    .create(eventStore.load("#111"))
                    .expectNextCount(0)
                    .verifyComplete()

        }
    }
})
