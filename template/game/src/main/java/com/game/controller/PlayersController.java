package com.game.controller;

import com.game.entity.Player;

import com.game.entity.PlayerFilter;
import com.game.exception.PlayerNotFoundException;
import com.game.exception.BadRequestException;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/rest")
public class PlayersController {


    @Autowired
    PlayerService playerService;


    @GetMapping(value = "/players")
    public List<Player> getPlayers(@ModelAttribute("player") PlayerFilter playerFilter,
                                   @RequestParam(required = false, defaultValue = "ID") String order,
                                   @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                   @RequestParam(required = false, defaultValue = "3") Integer pageSize) {
        return playerService.getAllPlayers(playerFilter, PlayerOrder.valueOf(order), pageNumber, pageSize);
    }

    @GetMapping(value = "/players/count")
    public Integer getPlayersCount(@ModelAttribute("player") PlayerFilter playerFilter) {
        return playerService.getAllPlayersCount(playerFilter);
    }

    @GetMapping(value = "/players/{id}")
    public Player getPlayerById(@PathVariable Long id) {
        checkId(id);
        return playerService.getPlayerById(id);
    }

    @PostMapping(value = "/players/")
    public Player createPlayer(@RequestBody Player player) {
        return playerService.createPlayer(player);
    }

    @PostMapping(value = "/players/{id}")
    public Player updatePlayer(@RequestBody Player player, @PathVariable Long id) {
        checkId(id);
        return playerService.updatePlayer(player, id);
    }

    @DeleteMapping(value = "/players/{id}")
    public void deletePlayer(@PathVariable Long id) {
        checkId(id);
        playerService.deletePlayer(id);
    }

    private void checkId(Long id) {
        if (id < 1 || !(id instanceof Number) || (id % 1 != 0)) throw new BadRequestException();
        if (!playerService.isPlayerFound(id)) throw new PlayerNotFoundException();
    }
}
