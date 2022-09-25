package com.game.repository;

import com.game.entity.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayersRepository extends CrudRepository<Player, Long> {

    @Override
    Optional<Player> findById(Long s);

    @Override
    <S extends Player> S save(S entity);

    Optional<Player> findByName(String name);

    @Override
    List<Player> findAll();
}