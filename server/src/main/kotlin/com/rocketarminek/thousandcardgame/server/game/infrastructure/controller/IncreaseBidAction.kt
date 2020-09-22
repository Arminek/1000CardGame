package com.rocketarminek.thousandcardgame.server.game.infrastructure.controller

import com.rocketarminek.thousandcardgame.server.game.domain.model.Game
import com.rocketarminek.thousandcardgame.server.game.domain.model.GameId
import com.rocketarminek.thousandcardgame.server.shared.Repository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class IncreaseBidAction(@Autowired private val repository: Repository<Game>) {
    @CrossOrigin
    @PostMapping(value = ["/v1/games/{id}/bid"])
    fun increaseBid(@PathVariable id: String): ResponseEntity<IncreaseBidResponse> {
        val game = this.repository.find(id)
                ?: return ResponseEntity(IncreaseBidResponse(id, "The game $id not found"), HttpStatus.NOT_FOUND)
        try {
            game.increaseBid(10)
        } catch (exception: IllegalArgumentException) {
            return ResponseEntity(IncreaseBidResponse(id, "The game $id not found"), HttpStatus.BAD_REQUEST)
        }
        this.repository.save(game)

        return ResponseEntity(IncreaseBidResponse(id, "Ack!"), HttpStatus.OK)
    }
}

data class IncreaseBidResponse(val id: String, val message: String)
