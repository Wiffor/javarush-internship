package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.PlayerFilter;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerFilterService {

    public static List<Player> filterPlayers(PlayerFilter playerFilter, List<Player> playerList) {

        if (playerFilter.getName() != null) {
            playerList = playerList.stream().filter(s -> s.getName().toLowerCase().contains
                    (playerFilter.getName().toLowerCase())).collect(Collectors.toList());
        }

        if (playerFilter.getTitle() != null) {
            playerList = playerList.stream().filter(s -> s.getTitle().toLowerCase().contains
                    (playerFilter.getTitle().toLowerCase())).collect(Collectors.toList());
        }

        if (playerFilter.getProfession() != null) {
            playerList = playerList.stream().filter(s -> s.getProfession().equals(playerFilter.getProfession()))
                    .collect(Collectors.toList());
        }

        if (playerFilter.getRace() != null) {
            playerList = playerList.stream().filter(s -> s.getRace().equals(playerFilter.getRace()))
                    .collect(Collectors.toList());
        }

        if (playerFilter.getMaxExperience() != null && playerFilter.getMaxExperience() != 0) {
            playerList = playerList.stream().filter(p -> p.getExperience() <= playerFilter.getMaxExperience())
                    .collect(Collectors.toList());
        }

        if (playerFilter.getMinExperience() != null && playerFilter.getMinExperience() != 0) {
            playerList = playerList.stream().filter(p -> p.getExperience() >= playerFilter.getMinExperience())
                    .collect(Collectors.toList());
        }

        if (playerFilter.getMaxLevel() != null && playerFilter.getMaxLevel() != 0) {
            playerList = playerList.stream().filter(p -> p.getLevel() <= playerFilter.getMaxLevel())
                    .collect(Collectors.toList());
        }

        if (playerFilter.getMinLevel() != null && playerFilter.getMinLevel() != 0) {
            playerList = playerList.stream().filter(p -> p.getLevel() >= playerFilter.getMinLevel())
                    .collect(Collectors.toList());
        }

        if (playerFilter.getBefore() != null) {
            playerList = playerList.stream().filter(p -> p.getBirthday().before(new Date(playerFilter.getBefore())))
                    .collect(Collectors.toList());
        }

        if (playerFilter.getAfter() != null) {
            playerList = playerList.stream().filter(p -> p.getBirthday().after(new Date(playerFilter.getAfter())))
                    .collect(Collectors.toList());
        }

        if (playerFilter.isBanned() != null) {
            playerList = playerList.stream().filter(p -> p.isBanned().equals(playerFilter.isBanned()))
                    .collect(Collectors.toList());
        }

        return playerList;
    }

    public static List<Player> filterPlayersByOrder(PlayerOrder playerOrder, List <Player> playerList) {
        switch (playerOrder) {
            case ID:
                playerList.sort((o1, o2) -> (int) (o1.getId() - o2.getId()));
                break;
            case NAME:
                playerList.sort(Comparator.comparing(Player::getName));
                break;
            case EXPERIENCE:
                playerList.sort(Comparator.comparingInt(Player::getExperience));
                break;
            case BIRTHDAY:
                playerList.sort((o1, o2) -> {
                    if (o1.getBirthday().before(o2.getBirthday())) return -1;
                    if (o1.getBirthday().after(o2.getBirthday())) return 1;
                    return 0;
                });
                break;
            case LEVEL:
                playerList.sort(Comparator.comparingInt(Player::getLevel));
                break;
        }
        return playerList;
    }
}
