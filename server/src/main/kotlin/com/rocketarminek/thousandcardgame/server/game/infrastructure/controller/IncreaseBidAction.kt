package com.rocketarminek.thousandcardgame.server.game.infrastructure.controller

import com.rocketarminek.thousandcardgame.server.game.domain.model.Game
import com.rocketarminek.thousandcardgame.server.game.domain.model.GameId
import com.rocketarminek.thousandcardgame.server.shared.Repository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class IncreaseBidAction(@Autowired private val repository: Repository<Game>) {
    @CrossOrigin
    @PostMapping(value = ["/v1/games/{id}/bid"])
    fun increaseBid(@PathVariable id: String, @RequestBody command: IncreaseBid): ResponseEntity<IncreaseBidResponse> {
        val game = this.repository.find(id)
                ?: return ResponseEntity(IncreaseBidResponse(id, "The game $id not found"), HttpStatus.NOT_FOUND)
        try {
            game.increaseBid(command.amount)
        } catch (exception: IllegalArgumentException) {
            return ResponseEntity(IncreaseBidResponse(id, "Illegal argument game ($id): ${exception.message}"), HttpStatus.BAD_REQUEST)
        }
        this.repository.save(game)

        return ResponseEntity(IncreaseBidResponse(id, "Ack!"), HttpStatus.OK)
    }
}

data class IncreaseBidResponse(val id: String, val message: String)
data class IncreaseBid(val amount: Int)
