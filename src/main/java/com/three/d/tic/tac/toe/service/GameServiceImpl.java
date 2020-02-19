package com.three.d.tic.tac.toe.service;

import static com.three.d.tic.tac.toe.ai.minimax.AlphaBetaPruningSolver.minimaxDecision;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.three.d.tic.tac.toe.domain.GameEntity;
import com.three.d.tic.tac.toe.domain.UserEntity;
import com.three.d.tic.tac.toe.dto.Game;
import com.three.d.tic.tac.toe.dto.User;
import com.three.d.tic.tac.toe.repository.GameRepository;
import com.three.d.tic.tac.toe.util.ThreeDTicTacToePossibleWinsGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("gameService")
public class GameServiceImpl implements GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private ThreeDTicTacToePossibleWinsGenerator threeDTicTacToePossibleWinsGenerator;

    @Override
    public List<Game> findByUserEntityOrderByIdDesc(User user) {
        return gameRepository.findByUserEntityOrderByIdDesc(UserEntity.fromDto(user)).stream()
                .map(GameEntity::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<GameEntity> findByIdAndUserEntity(Long id, UserEntity userEntity) {
        return gameRepository.findByIdAndUserEntity(id, userEntity);
    }

    @Override
    public Game createGame(Game game, User user) {
        GameEntity gameEntity = GameEntity.fromDto(game);
        gameEntity.setUserEntity(UserEntity.fromDto(user));

        return Optional.of(gameRepository.save(gameEntity))
                .map(GameEntity::toDto)
                .get();
    }

    @Override
    public Game move(GameEntity gameEntity, Integer move) {
        Game game = GameEntity.toDto(gameEntity);
        game.setPossibleWins(
                threeDTicTacToePossibleWinsGenerator.getPossibleWins(game.getN()));
        game.move(move);

        if (game.getNextTurn() != null) {
            minimaxDecision(game, game.getNextTurn(), Integer.MIN_VALUE, Integer.MAX_VALUE);
        }

        gameEntity.setBoard(game.getBoard());
        gameEntity.setBlankTilesIndexes(game.getBlankTilesIndexes());
        gameEntity.setNextTurn(game.getNextTurn());
        gameEntity.setWinner(game.getWinner());
        gameEntity.setWinningTilesIndexes(game.getWinningTilesIndexes());

        return Optional.of(gameRepository.save(gameEntity))
                .map(GameEntity::toDto)
                .get();
    }

    @Override
    public void deleteByNextTurnNull() {
        gameRepository.deleteByNextTurnNull();
    }
}
