package com.rocketarminek.thousandcardgame.server.acceptance

import com.rocketarminek.thousandcardgame.server.game.infrastructure.Configuration
import com.rocketarminek.thousandcardgame.server.game.infrastructure.controller.GetEventsAction
import com.rocketarminek.thousandcardgame.server.game.infrastructure.controller.Response
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContain
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpStatus
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.support.AnnotationConfigContextLoader

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [GetEventsAction::class, Configuration::class])
@ContextConfiguration(loader = AnnotationConfigContextLoader::class)
internal class CreateGameTest(@Autowired val client: TestRestTemplate) {
    @Test
    fun `it creates game`() {
        val createResponse = client.postForEntity("/v1/games", null, Response::class.java)
        createResponse.statusCode shouldBeEqualTo HttpStatus.CREATED
        createResponse.body?.id shouldBeEqualTo "671e7abd-5b20-425a-a70e-df1aa9142e0c"

        val result = client.getForEntity<String>("/v1/games/${createResponse.body?.id}/events")
        result.statusCode shouldBeEqualTo HttpStatus.OK
        result.body?.toString()!! shouldContain "671e7abd-5b20-425a-a70e-df1aa9142e0c"
        result.body?.toString()!! shouldContain "game-created"
    }

    @Test
    fun `it cannot be created with same id`() {
        client.postForEntity("/v1/games", null, Response::class.java)
        val createResponse = client.postForEntity("/v1/games", null, Response::class.java)
        createResponse.statusCode shouldBeEqualTo HttpStatus.BAD_REQUEST
    }
}
