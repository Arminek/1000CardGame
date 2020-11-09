package com.rocketarminek.thousandcardgame.server.game.infrastructure.controller

import com.rocketarminek.thousandcardgame.server.game.domain.model.Game
import com.rocketarminek.thousandcardgame.server.shared.Repository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthAction(@Autowired private val repository: Repository<Game>) {
    @CrossOrigin
    @GetMapping(value = ["/_health"])
    fun health(): ResponseEntity<Response> {
        return ResponseEntity(HttpStatus.OK)
    }

    @CrossOrigin
    @GetMapping(value = ["/_ready"])
    fun ready(): ResponseEntity<Response> {
        return ResponseEntity(HttpStatus.OK)
    }
}
