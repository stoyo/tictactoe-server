package com.three.d.tic.tac.toe.ai.minimax;

import static java.util.stream.Collectors.toMap;

import java.util.Map;
import java.util.stream.IntStream;

import com.three.d.tic.tac.toe.domain.GameBoardTileState;
import com.three.d.tic.tac.toe.domain.GamePlayer;
import com.three.d.tic.tac.toe.dto.Game;

public final class AlphaBetaPruningSolver {

    private static final int MAX_DEPTH = 2; // because it works instantly for 4 * 4 * 4

    private AlphaBetaPruningSolver() throws InstantiationException {
        throw new InstantiationException();
    }

    public static int minimaxDecision(Game game, GamePlayer player, int alpha, int beta) {
        return minimaxDecision(game, player, alpha, beta, 0);
    }

    private static int minimaxDecision(Game game, GamePlayer player, int alpha, int beta, int currentDepth) {
        if (game.getNextTurn() == GamePlayer.X) {
            return minValue(game, player, alpha, beta, currentDepth);
        } else {
            return maxValue(game, player, alpha, beta, currentDepth);
        }
    }

    private static int minValue(Game game, GamePlayer player, int alpha, int beta, int currentDepth) {
        if (game.getWinner() != null) {
            return score(game, player, currentDepth);
        }

        if (MAX_DEPTH <= currentDepth) {
            return heuristic(game, player);
        }

        int bestIndex = -1;
        for (int move : game.getBlankTilesIndexes()) {

            Game modifiedGame = Game.builder()
                    .n(game.getN())
                    .board(game.getBoard())
                    .winner(game.getWinner())
                    .nextTurn(game.getNextTurn())
                    .blankTilesIndexes(game.getBlankTilesIndexes())
                    .possibleWins(game.getPossibleWins())
                    .build();
            modifiedGame.move(move);

            int score = minimaxDecision(
                    modifiedGame, player, alpha, beta, currentDepth + 1);
            if (score < beta) {
                beta = score;
                bestIndex = move;
            }

            if (alpha >= beta) {
                break;
            }
        }

        if (bestIndex != -1) {
            game.move(bestIndex);
        }

        return beta;
    }

    private static int maxValue(Game game, GamePlayer player, int alpha, int beta, int currentDepth) {
        if (game.getWinner() != null) {
            return score(game, player, currentDepth);
        }

        if (MAX_DEPTH <= currentDepth) {
            return heuristic(game, player);
        }

        int bestIndex = -1;
        for (int move : game.getBlankTilesIndexes()) {
            Game modifiedGame = Game.builder()
                    .n(game.getN())
                    .board(game.getBoard())
                    .winner(game.getWinner())
                    .nextTurn(game.getNextTurn())
                    .blankTilesIndexes(game.getBlankTilesIndexes())
                    .possibleWins(game.getPossibleWins())
                    .build();
            modifiedGame.move(move);

            int score = minimaxDecision(
                    modifiedGame, player, alpha, beta, currentDepth + 1);
            if (score > alpha) {
                alpha = score;
                bestIndex = move;
            }

            if (alpha >= beta) {
                break;
            }
        }

        if (bestIndex != -1) {
            game.move(bestIndex);
        }

        return alpha;
    }

    private static int heuristic(Game game, GamePlayer currPlayer) {
        int heuristicO = nInARow(game, GamePlayer.O).entrySet().stream()
                .map(entry -> entry.getKey() * entry.getKey() * entry.getValue())
                .reduce(0, (a, b) -> a + b);

        int heuristicX = nInARow(game, GamePlayer.X).entrySet().stream()
                .map(entry -> entry.getKey() * entry.getKey() * entry.getValue())
                .reduce(0, (a, b) -> a + b);

        if (currPlayer == GamePlayer.O) {
            return heuristicO - heuristicX;
        } else {
            return heuristicX - heuristicO;
        }
    }

    private static Map<Integer, Integer> nInARow(Game game, GamePlayer player) {
        GameBoardTileState p = (player == GamePlayer.X) ? GameBoardTileState.X : GameBoardTileState.O;
        GameBoardTileState o = (player == GamePlayer.X) ? GameBoardTileState.O : GameBoardTileState.X;

        Map<Integer, Integer> nInARow = IntStream.range(0, game.getN()).boxed()
                .collect(toMap(i -> i, i -> 0));
        game.getPossibleWins().forEach(possibleWin -> {
            if (possibleWin.stream().anyMatch(
                    i -> o == GameBoardTileState.fromChar(game.getBoard().charAt(i)))) {
                return;
            }

            int alreadyPlacedCount = (int) possibleWin.stream()
                    .filter(i -> p == GameBoardTileState.fromChar(game.getBoard().charAt(i))).count();
            nInARow.put(alreadyPlacedCount, nInARow.get(alreadyPlacedCount) + 1);
        });

        return nInARow;
    }

    private static int score(Game game, GamePlayer currPlayer, int currentDepth) {
        if (game.getWinner() == currPlayer) {
            return game.getN() * game.getN() * game.getN() + 1 - currentDepth;
        } else {
            return -(game.getN() * game.getN() * game.getN() + 1) + currentDepth;
        }
    }
}
