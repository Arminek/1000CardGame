package com.rocketarminek.thousandcardgame.server.acceptance

import com.rocketarminek.thousandcardgame.server.game.infrastructure.Configuration
import com.rocketarminek.thousandcardgame.server.game.infrastructure.controller.DeclareBid
import com.rocketarminek.thousandcardgame.server.game.infrastructure.controller.DeclareBidResponse
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
    fun `it creates a new game`() {
        val createResponse = client.postForEntity("/v1/games", null, CreateResponse::class.java)
        createResponse.statusCode shouldBeEqualTo HttpStatus.CREATED
        createResponse.body?.message shouldBeEqualTo "Created"
    }
    @Test
    fun `it increases the bid`() {
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
    fun `it handles the domain errors during increasing the bid`() {
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
    fun `it declares the bid`() {
        val createResponse = client.postForEntity("/v1/games", null, CreateResponse::class.java)
        val id = createResponse.body?.id
        client.delete("/v1/games/$id/bid")
        client.delete("/v1/games/$id/bid")
        val increaseBidResponse = client
                .postForEntity(
                        "/v1/games/${id}/declare",
                        DeclareBid(120),
                        DeclareBidResponse::class.java
                )
        increaseBidResponse.statusCode shouldBeEqualTo HttpStatus.OK
        increaseBidResponse.body?.message shouldBeEqualTo "Ack!"
    }
    @Test
    fun `it handles the domain errors during declaring the bid`() {
        val createResponse = client.postForEntity("/v1/games", null, CreateResponse::class.java)
        val id = createResponse.body?.id
        val increaseBidResponse = client
                .postForEntity(
                        "/v1/games/${id}/declare",
                        DeclareBid(120),
                        DeclareBidResponse::class.java
                )
        increaseBidResponse.statusCode shouldBeEqualTo HttpStatus.BAD_REQUEST
        increaseBidResponse.body?.message shouldBeEqualTo "Illegal argument game ($id): Cannot declare not settled bid!"
    }
    @Test
    fun `it passes the bid`() {
        val createResponse = client.postForEntity("/v1/games", null, CreateResponse::class.java)
        invoking { client.delete("/v1/games/${createResponse.body?.id}/bid") } shouldNotThrow RestClientException::class
    }
}
