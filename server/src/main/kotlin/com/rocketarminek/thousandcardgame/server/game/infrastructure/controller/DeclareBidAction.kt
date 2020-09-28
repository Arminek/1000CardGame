package com.rocketarminek.thousandcardgame.server.game.infrastructure.controller

import com.rocketarminek.thousandcardgame.server.game.domain.model.Game
import com.rocketarminek.thousandcardgame.server.shared.Repository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class DeclareBidAction(@Autowired private val repository: Repository<Game>) {
    @CrossOrigin
    @PostMapping(value = ["/v1/games/{id}/declare"])
    fun declareBid(@PathVariable id: String, @RequestBody command: DeclareBid): ResponseEntity<DeclareBidResponse> {
        val game = this.repository.find(id)
                ?: return ResponseEntity(DeclareBidResponse(id, "The game $id not found"), HttpStatus.NOT_FOUND)
        try {
            game.declareBid(command.amount)
        } catch (exception: IllegalArgumentException) {
            return ResponseEntity(DeclareBidResponse(id, "Illegal argument game ($id): ${exception.message}"), HttpStatus.BAD_REQUEST)
        }
        this.repository.save(game)

        return ResponseEntity(DeclareBidResponse(id, "Ack!"), HttpStatus.OK)
    }
}

data class DeclareBidResponse(val id: String, val message: String)
data class DeclareBid(val amount: Int)
