package com.three.d.tic.tac.toe.dto;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.three.d.tic.tac.toe.domain.GameBoardTileState;
import com.three.d.tic.tac.toe.domain.GamePlayer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({ "blankTilesIndexes", "possibleWins" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Game {

    private Long id;

    private Integer n;

    private String board;

    private GamePlayer winner;

    private GamePlayer nextTurn;

    private Integer[] blankTilesIndexes;

    private List<List<Integer>> possibleWins;

    private Integer[] winningTilesIndexes;

    public void move(int move) {
        if (Arrays.stream(blankTilesIndexes).noneMatch(i -> i == move)) {
            return;
        }

        board = board.substring(0, move) + nextTurn.toString().charAt(0) + board.substring(move + 1);
        blankTilesIndexes = Arrays.stream(blankTilesIndexes)
                .filter(i -> i != move)
                .toArray(Integer[]::new);

        if (blankTilesIndexes.length == 0) {
            nextTurn = null;
        } else {
            nextTurn = (nextTurn == GamePlayer.X) ? GamePlayer.O : GamePlayer.X;
        }

        Optional<List<Integer>> winnerTilesIndexesX = winnerTilesIndexes(GamePlayer.X);
        if (winnerTilesIndexesX.isPresent()) {
            nextTurn = null;
            winner = GamePlayer.X;
            winningTilesIndexes = winnerTilesIndexesX.get().toArray(new Integer[0]);
        } else {
            Optional<List<Integer>> winnerTilesIndexesO = winnerTilesIndexes(GamePlayer.O);
            if (winnerTilesIndexesO.isPresent()) {
                nextTurn = null;
                winner = GamePlayer.O;
                winningTilesIndexes = winnerTilesIndexesO.get().toArray(new Integer[0]);
            }
        }
    }

    private Optional<List<Integer>> winnerTilesIndexes(GamePlayer gamePlayer) {
        return possibleWins.stream()
                .filter(possibleWin -> possibleWin.stream()
                        .allMatch(tileIndex ->
                                GameBoardTileState.BLANK.asString().charAt(0) != board.charAt(tileIndex)
                                        && GamePlayer.fromChar(board.charAt(tileIndex)) == gamePlayer)
                )
                .findFirst();

    }

    public static Game fromSpecification(GameBoardSpecification gameBoardSpecification) {
        int n = gameBoardSpecification.getN();
        return Game.builder()
                .n(n)
                .board(String.join("", Collections.nCopies(n * n * n,
                        GameBoardTileState.BLANK.asString())))
                .nextTurn(GamePlayer.X)
                .blankTilesIndexes(IntStream.range(0, n * n * n).boxed()
                        .toArray(Integer[]::new))
                .build();
    }
}
