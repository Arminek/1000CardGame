package com.rocketarminek.thousandcardgame.server.game.infrastructure.controller

import com.rocketarminek.thousandcardgame.server.game.domain.model.Game
import com.rocketarminek.thousandcardgame.server.game.domain.model.GameId
import com.rocketarminek.thousandcardgame.server.shared.Repository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class CreateGameAction(@Autowired private val repository: Repository<Game>) {
    @CrossOrigin
    @PostMapping(value = ["/v1/games"])
    fun createGame(): ResponseEntity<Response> {
        val id = UUID.randomUUID().toString()
        val playerIds = arrayListOf(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString()
        )
        if (this.repository.find(id) != null) {
            return ResponseEntity(Response(id, "The game $id already exists"), HttpStatus.BAD_REQUEST)
        }
        this.repository.save(Game(id, playerIds))

        return ResponseEntity(Response(id, "Created"), HttpStatus.CREATED)
    }
}

data class Response(val id: GameId, val message: String = "")
