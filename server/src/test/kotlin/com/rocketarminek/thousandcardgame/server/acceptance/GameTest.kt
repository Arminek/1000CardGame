package com.rocketarminek.thousandcardgame.server.acceptance

import com.rocketarminek.thousandcardgame.server.game.infrastructure.Configuration
import com.rocketarminek.thousandcardgame.server.game.infrastructure.controller.GetEventsAction
import com.rocketarminek.thousandcardgame.server.game.infrastructure.controller.IncreaseBid
import org.amshove.kluent.invoking
import com.rocketarminek.thousandcardgame.server.game.infrastructure.controller.Response as CreateResponse
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotThrow
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.support.AnnotationConfigContextLoader
import org.springframework.web.client.RestClientException

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [GetEventsAction::class, Configuration::class])
@ContextConfiguration(loader = AnnotationConfigContextLoader::class)
internal class GameTest(@Autowired val client: TestRestTemplate) {
    @Test
    fun `it creates game`() {
        val createResponse = client.postForEntity("/v1/games", null, CreateResponse::class.java)
        createResponse.statusCode shouldBeEqualTo HttpStatus.CREATED
        createResponse.body?.message shouldBeEqualTo "Created"
    }
    @Test
    fun `it increases bid`() {
        val createResponse = client.postForEntity("/v1/games", null, CreateResponse::class.java)
        val increaseBidResponse = client
                .postForEntity(
                        "/v1/games/${createResponse.body?.id}/bid",
                        IncreaseBid(10),
                        CreateResponse::class.java
                )
        increaseBidResponse.statusCode shouldBeEqualTo HttpStatus.OK
        increaseBidResponse.body?.message shouldBeEqualTo "Ack!"
    }
    @Test
    fun `it handle domain errors`() {
        val createResponse = client.postForEntity("/v1/games", null, CreateResponse::class.java)
        val id = createResponse.body?.id
        val increaseBidResponse = client
                .postForEntity(
                        "/v1/games/${id}/bid",
                        IncreaseBid(400),
                        CreateResponse::class.java
                )
        increaseBidResponse.statusCode shouldBeEqualTo HttpStatus.BAD_REQUEST
        increaseBidResponse.body?.message shouldBeEqualTo "Illegal argument game ($id): Cannot increase the bid over 300"
    }
    @Test
    fun `it passes bid`() {
        val createResponse = client.postForEntity("/v1/games", null, CreateResponse::class.java)
        invoking { client.delete("/v1/games/${createResponse.body?.id}/bid") } shouldNotThrow RestClientException::class
    }
}
