package com.three.d.tic.tac.toe.service;

import java.util.List;
import java.util.Optional;

import com.three.d.tic.tac.toe.domain.GameEntity;
import com.three.d.tic.tac.toe.domain.UserEntity;
import com.three.d.tic.tac.toe.dto.Game;
import com.three.d.tic.tac.toe.dto.User;

public interface GameService {

    Game createGame(Game game, User user);

    List<Game> findByUserEntityOrderByIdDesc(User user);

    Game move(GameEntity gameEntity, Integer move);

    Optional<GameEntity> findByIdAndUserEntity(Long id, UserEntity userEntity);

    void deleteByNextTurnNull();
}
