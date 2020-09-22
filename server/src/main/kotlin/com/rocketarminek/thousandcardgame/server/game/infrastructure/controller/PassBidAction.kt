package com.rocketarminek.thousandcardgame.server.game.infrastructure.controller

import com.rocketarminek.thousandcardgame.server.game.domain.model.Game
import com.rocketarminek.thousandcardgame.server.shared.Repository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class PassBidAction(@Autowired private val repository: Repository<Game>) {
    @CrossOrigin
    @DeleteMapping(value = ["/v1/games/{id}/bid"])
    fun increaseBid(@PathVariable id: String): ResponseEntity<PassBidResponse> {
        val game = this.repository.find(id)
                ?: return ResponseEntity(PassBidResponse(id, "The game $id not found"), HttpStatus.NOT_FOUND)
        try {
            game.passBid()
        } catch (exception: IllegalArgumentException) {
            return ResponseEntity(PassBidResponse(id, "The game $id not found"), HttpStatus.BAD_REQUEST)
        }
        this.repository.save(game)

        return ResponseEntity(PassBidResponse(id, "Ack!"), HttpStatus.OK)
    }
}

data class PassBidResponse(val id: String, val message: String)
