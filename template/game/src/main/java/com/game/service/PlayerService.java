package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.PlayerFilter;
import com.game.exception.BadRequestException;
import com.game.repository.PlayersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlayerService {

    @Autowired
    private PlayersRepository playersRepository;
    private List<Player> playerList;


    public List<Player> getAllPlayers(PlayerFilter playerFilter, PlayerOrder playerOrder, Integer pageNumber, Integer pageSize) {

        playerList = playersRepository.findAll();

        playerList = PlayerFilterService.filterPlayers(playerFilter, playerList);

        playerList = PlayerFilterService.filterPlayersByOrder(playerOrder, playerList);

        int indexOfFirstPlayer = pageNumber * pageSize;
        int indexOfLastPlayer = (indexOfFirstPlayer + pageSize) < playerList.size() ?
                (indexOfFirstPlayer + pageSize) : playerList.size();

        List<Player> filteredPlayerList = playerList.subList(
                indexOfFirstPlayer, indexOfLastPlayer);

        return filteredPlayerList;
    }

    public Integer getAllPlayersCount(PlayerFilter playerFilter) {
        playerList = playersRepository.findAll();
        playerList = PlayerFilterService.filterPlayers(playerFilter, playerList);
        return playerList.size();
    }

    public Player getPlayerById(long id) {
        Optional<Player> player = playersRepository.findById(id);
        return player.get();
    }

    public Player createPlayer(Player player) {

        if (player.getBirthday() == null || player.getName() == null || player.getProfession() == null ||
                player.getRace() == null || player.getTitle() == null || player.getExperience() == null) {
            throw new BadRequestException();
        }
        if (player.getName().length() > 12 || player.getTitle().length() > 30) throw new BadRequestException();
        if (player.getName() == "") throw new BadRequestException();
        if (player.getBirthday().getTime() < 0) throw new BadRequestException();

        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, 0, 0);
        long dateBefore = calendar.getTimeInMillis();
        calendar.set(3000, 12, 31, 23, 59, 59);
        long dateAfter = calendar.getTimeInMillis();
        if (player.getBirthday().before(new Date(dateBefore)) ||
                player.getBirthday().after(new Date(dateAfter))) throw new BadRequestException();
        if (player.getExperience() < 0 || player.getExperience() > 10000000) throw new BadRequestException();


        int level = calculateLevel(player.getExperience());
        player.setLevel(level);
        player.setUntilNextLevel(calculateTillNextLevel(level, player.getExperience()));

        player.setId(playersRepository.count() + 1);

        playersRepository.save(player);

        return player;
    }

    public void deletePlayer(Long id) {
        playersRepository.deleteById(id);
    }


    private Integer calculateLevel(int experience) {
        Double level = (Math.sqrt(2500 + 200 * experience) - 50) / 100;
        return level.intValue();
    }

    private Integer calculateTillNextLevel(int level, int experience) {
        return 50 * (level + 1) * (level + 2) - experience;
    }

    public Player updatePlayer(Player player, long id) {
        Player updatedPlayer = playersRepository.findById(id).get();

        if (player.getName() != null) {
            if (player.getName().length() > 12 || player.getName() == "") throw new BadRequestException();
            updatedPlayer.setName(player.getName());
        }

        if (player.getTitle() != null) {
            if (player.getTitle().length() > 30) throw new BadRequestException();
            updatedPlayer.setTitle(player.getTitle());
        }

        if (player.getBirthday() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(2000, 0, 0);
            long dateBefore = calendar.getTimeInMillis();
            calendar.set(3000, 12, 31, 23, 59, 59);
            long dateAfter = calendar.getTimeInMillis();
            if (player.getBirthday().before(new Date(dateBefore)) ||
                    player.getBirthday().after(new Date(dateAfter))) throw new BadRequestException();
            updatedPlayer.setBirthday(player.getBirthday());
        }

        if (player.getExperience() != null && player.getExperience() != 0) {

            if (player.getExperience() < 0 || player.getExperience() > 10000000) throw new BadRequestException();
            updatedPlayer.setExperience(player.getExperience());
            int level = calculateLevel(player.getExperience());
            updatedPlayer.setLevel(level);
            updatedPlayer.setUntilNextLevel(calculateTillNextLevel(level, player.getExperience()));
        }

        if (player.isBanned() != null) updatedPlayer.setBanned(player.isBanned());
        if (player.getRace() != null) updatedPlayer.setRace(player.getRace());
        if (player.getProfession() != null) updatedPlayer.setProfession(player.getProfession());


        playersRepository.save(updatedPlayer);
        return updatedPlayer;
    }

    public boolean isPlayerFound(long id) {
        Optional<Player> optionalEditedPlayer = playersRepository.findById(id);
        if (optionalEditedPlayer.isPresent()) return true;
        return false;
    }
}
