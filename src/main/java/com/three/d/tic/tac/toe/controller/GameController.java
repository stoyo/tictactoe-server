package com.three.d.tic.tac.toe.controller;

import static com.three.d.tic.tac.toe.controller.GameController.SELF_LINK;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.three.d.tic.tac.toe.common.Constants.UriParts;
import com.three.d.tic.tac.toe.domain.GameBoardTileState;
import com.three.d.tic.tac.toe.domain.GameEntity;
import com.three.d.tic.tac.toe.domain.UserEntity;
import com.three.d.tic.tac.toe.dto.Game;
import com.three.d.tic.tac.toe.dto.GameBoardSpecification;
import com.three.d.tic.tac.toe.dto.UpdateBoardSpecification;
import com.three.d.tic.tac.toe.dto.User;
import com.three.d.tic.tac.toe.service.GameService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = SELF_LINK)
public class GameController {

    public static final String SELF_LINK = UriParts.API_PREFIX + "/games";

    private static final String N_MUST_BE_BETWEEN_MESSAGE = "'n' must be between '%d' and '%d'";
    private static final String GAME_NOT_FOUND_MESSAGE = "game with id: '%d' not found";
    private static final String FINISHED_GAME_MESSAGE = "game already over";
    private static final String MOVE_OUT_OF_BOUNDS_MESSAGE = "'move' must be between '%d' and '%d'";
    private static final String MOVE_IS_ALREADY_TAKEN_MESSAGE = "'move' is already taken";
    private static final String OPTIMISTIC_LOCK_EXCEPTION_MESSAGE =
            "game with id: '%d' was updated or deleted by another transaction";

    @Value("${tic-tac-toe.min.n}")
    private Integer minN;

    @Value("${tic-tac-toe.max.n}")
    private Integer maxN;

    @Autowired
    private GameService gameService;

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<Game> getAllGames(
            @AuthenticationPrincipal User currentUser) {
        return gameService.findByUserEntityOrderByIdDesc(currentUser);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public Game createGame(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody GameBoardSpecification gameBoardSpecification) {
        validateCreateRequest(gameBoardSpecification);

        return gameService.createGame(Game.fromSpecification(gameBoardSpecification), currentUser);
    }

    @PatchMapping(
            value = "/{id:.+$}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Game playMove(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateBoardSpecification updateBoardSpecification,
            @AuthenticationPrincipal User currentUser) {
        Optional<GameEntity> oGameEntity =
                gameService.findByIdAndUserEntity(id, UserEntity.fromDto(currentUser));
        if (!oGameEntity.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(GAME_NOT_FOUND_MESSAGE, id));
        }

        GameEntity gameEntity = oGameEntity.get();

        validateMoveRequest(gameEntity, updateBoardSpecification);

        try {
            return gameService.move(gameEntity, updateBoardSpecification.getMove());
        } catch (OptimisticLockingFailureException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    String.format(OPTIMISTIC_LOCK_EXCEPTION_MESSAGE, gameEntity.getId())
            );
        }
    }

    private void validateCreateRequest(GameBoardSpecification gameBoardSpecification) {
        if (gameBoardSpecification.getN() < minN || gameBoardSpecification.getN() > maxN) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format(N_MUST_BE_BETWEEN_MESSAGE, minN, maxN));
        }
    }

    private void validateMoveRequest(
            GameEntity gameEntity, UpdateBoardSpecification updateBoardSpecification) {
        if (gameEntity.getNextTurn() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, FINISHED_GAME_MESSAGE);
        }

        int move = updateBoardSpecification.getMove();
        int boardTilesCount = gameEntity.getN() * gameEntity.getN() * gameEntity.getN();
        if (move < 0 || move > boardTilesCount - 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format(MOVE_OUT_OF_BOUNDS_MESSAGE, 0, boardTilesCount - 1));
        }

        if (GameBoardTileState.BLANK.asString().charAt(0) != gameEntity.getBoard().charAt(move)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MOVE_IS_ALREADY_TAKEN_MESSAGE);
        }
    }
}
