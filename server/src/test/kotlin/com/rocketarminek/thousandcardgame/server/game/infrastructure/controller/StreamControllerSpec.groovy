package com.rocketarminek.thousandcardgame.server.game.infrastructure.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification

@WebMvcTest
class StreamControllerSpec extends Specification {
    @Autowired
    private MockMvc mvc

    def "it returns empty stream"() {
        when:
            def result = mvc.perform(MockMvcRequestBuilders.get("/v1/stream"))
        then:
            result.andExpect(MockMvcResultMatchers.status().isOk())
        and:
            result.andExpect(MockMvcResultMatchers.jsonPath('$.stream').value([]))
    }
}
