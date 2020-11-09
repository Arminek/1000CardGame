package com.rocketarminek.thousandcardgame.server.game.infrastructure.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthAction() {
    @CrossOrigin
    @GetMapping(value = ["/_health"])
    fun health(): ResponseEntity<Any> {
        return ResponseEntity(HttpStatus.OK)
    }

    @CrossOrigin
    @GetMapping(value = ["/_ready"])
    fun ready(): ResponseEntity<Any> {
        return ResponseEntity(HttpStatus.OK)
    }
}
