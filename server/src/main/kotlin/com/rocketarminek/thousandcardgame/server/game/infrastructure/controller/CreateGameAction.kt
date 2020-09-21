package com.rocketarminek.thousandcardgame.server.game.infrastructure.controller

import com.rocketarminek.thousandcardgame.server.game.domain.model.Game
import com.rocketarminek.thousandcardgame.server.game.domain.model.GameId
import com.rocketarminek.thousandcardgame.server.shared.Repository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CreateGameAction(@Autowired private val repository: Repository<Game>) {
    @PostMapping(value = ["/v1/games"])
    fun createGame(): ResponseEntity<Response> {
        val id = "671e7abd-5b20-425a-a70e-df1aa9142e0c"
        val playerIds = arrayOf(
                "69549ece-5e97-431a-93ba-09d47348eafb",
                "c07dd997-3ef0-41df-a893-91b7cd9e3391",
                "c8f1e7f0-2a73-4555-9e32-8188cc50a7b8"
        )
        if (this.repository.find(id) != null) {
            return ResponseEntity(Response(id, "The game $id already exists"), HttpStatus.BAD_REQUEST)
        }
        this.repository.save(Game(id, playerIds))

        return ResponseEntity(Response(id, "Created"), HttpStatus.CREATED)
    }
}

data class Response(val id: GameId, val message: String = "")
