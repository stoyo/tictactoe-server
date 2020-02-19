package com.three.d.tic.tac.toe.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.three.d.tic.tac.toe.common.Constants.CacheNames;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class ThreeDTicTacToePossibleWinsGenerator {

    @Value("${tic-tac-toe.min.n}")
    private Integer minN;

    @Value("${tic-tac-toe.max.n}")
    private Integer maxN;

    @Cacheable(value = CacheNames.POSSIBLE_WINS, key = "#n")
    public List<List<Integer>> getPossibleWins(int n) {
        return getPossibleWinsMap().get(n);
    }

    public Map<Integer, List<List<Integer>>> getPossibleWinsMap() {
        return IntStream.rangeClosed(minN, maxN).boxed()
                .collect(Collectors.toMap(n -> n,
                        ThreeDTicTacToePossibleWinsGenerator::calculateAllPossibleWins));
    }

    private static List<List<Integer>> calculateAllPossibleWins(int n) {
        List<List<Integer>> rowAndColumnWins = getRowAndColumnWins(n);
        List<List<Integer>> mainAndSecondaryDiagonalWins = getMainAndSecondaryDiagonalWins(n);
        List<List<Integer>> straightDownWins = getAllStraightDownWins(n);
        List<List<Integer>> diagonalsThroughBoardsWins = getAllDiagonalsThroughBoardWins(n);

        Stream<List<Integer>> twoDimensionalWins = Stream.concat(
                rowAndColumnWins.stream(),
                mainAndSecondaryDiagonalWins.stream());

        Stream<List<Integer>> threeDimensionalWins = Stream.concat(
                straightDownWins.stream(),
                diagonalsThroughBoardsWins.stream());

        return Stream.concat(twoDimensionalWins, threeDimensionalWins)
                .collect(Collectors.toList());
    }

    private static List<List<Integer>> getRowAndColumnWins(int n) {
        List<List<Integer>> rowBoardWins = new ArrayList<>();
        for (int i = 0; i < n * n * n; i += n * n) {
            for (int j = 0; j < n * n; j += n) {
                List<Integer> rowBoardWin = new ArrayList<>();
                for (int k = j; k < j + n; k++) {
                    rowBoardWin.add(k + i);
                }
                rowBoardWins.add(rowBoardWin);
            }
        }

        List<List<Integer>> columnBoardWins = new ArrayList<>();
        for (int i = 0; i < n * n * n; i += n * n) {
            for (int j = 0; j < n; j++) {
                List<Integer> columnBoardWin = new ArrayList<>();
                for (int k = j; k < n * n; k += n) {
                    columnBoardWin.add(k + i);
                }
                columnBoardWins.add(columnBoardWin);
            }
        }

        return Stream.concat(rowBoardWins.stream(), columnBoardWins.stream())
                .collect(Collectors.toList());
    }

    private static List<List<Integer>> getMainAndSecondaryDiagonalWins(int n) {
        List<List<Integer>> mainDiagBoardWins = new ArrayList<>();
        for (int i = 0; i < n * n * n; i += n * n) {
            List<Integer> mainDiagBoardWin = new ArrayList<>();
            for (int j = 0; j < n * n; j += n + 1) {
                mainDiagBoardWin.add(j + i);
            }
            mainDiagBoardWins.add(mainDiagBoardWin);
        }

        List<List<Integer>> secondaryDiagBoardWins = new ArrayList<>();
        for (int i = 0; i < n * n * n; i += n * n) {
            List<Integer> secondaryDiagBoardWin = new ArrayList<>();
            for (int j = 0, k = n - 1; j < n; j++, k += n - 1) {
                secondaryDiagBoardWin.add(k + i);
            }
            secondaryDiagBoardWins.add(secondaryDiagBoardWin);
        }

        return Stream.concat(mainDiagBoardWins.stream(), secondaryDiagBoardWins.stream())
                .collect(Collectors.toList());
    }

    private static List<List<Integer>> getAllStraightDownWins(int n) {
        List<List<Integer>> straightDownWins = new ArrayList<>();
        for (int i = 0; i < n * n; i++) {
            List<Integer> winMoves = new ArrayList<>();
            for (int j = 0, k = i; j < n; k += n * n, j++) {
                winMoves.add(j, k);
            }
            straightDownWins.add(i, winMoves);
        }

        return straightDownWins;
    }

    private static List<List<Integer>> getAllDiagonalsThroughBoardWins(int n) {
        List<List<Integer>> diagonalsThroughBoardsWins = new ArrayList<>();

        List<Integer> topLeftBottomRightBoardWin = new ArrayList<>();
        for (int i = 0, j = 0; i < n; i++, j += n * n + n + 1) {
            topLeftBottomRightBoardWin.add(i, j);
        }
        diagonalsThroughBoardsWins.add(topLeftBottomRightBoardWin);

        List<Integer> bottomRightTopLeftBoardWin = new ArrayList<>();
        for (int i = 0, j = n * n - 1; i < n; i++, j += n * n - n - 1) {
            bottomRightTopLeftBoardWin.add(i, j);
        }
        diagonalsThroughBoardsWins.add(bottomRightTopLeftBoardWin);

        List<Integer> bottomLeftTopRightBoardWin = new ArrayList<>();
        for (int i = 0, j = n * n - n; i < n; i++, j += n * n - n + 1) {
            bottomLeftTopRightBoardWin.add(i, j);
        }
        diagonalsThroughBoardsWins.add(bottomLeftTopRightBoardWin);

        List<Integer> topRightBottomLeftBoardWin = new ArrayList<>();
        for (int i = 0, j = n - 1; i < n; i++, j += n * n + n - 1) {
            topRightBottomLeftBoardWin.add(i, j);
        }
        diagonalsThroughBoardsWins.add(topRightBottomLeftBoardWin);

        List<List<Integer>> outerTopInnerBottomBoardWins = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            List<Integer> outerTopInnerBottomBoardWin = new ArrayList<>();
            for (int j = 0, k = i; j < n; j++, k += n * n + n) {
                outerTopInnerBottomBoardWin.add(j, k);
            }
            outerTopInnerBottomBoardWins.add(outerTopInnerBottomBoardWin);
        }
        diagonalsThroughBoardsWins.addAll(outerTopInnerBottomBoardWins);

        List<List<Integer>> innerTopOuterBottomBoardWins = new ArrayList<>();
        for (int i = n * n - n; i < n * n; i++) {
            List<Integer> innerTopOuterBottomBoardWin = new ArrayList<>();
            for (int j = 0, k = i; j < n; j++, k += n * n - n) {
                innerTopOuterBottomBoardWin.add(j, k);
            }
            innerTopOuterBottomBoardWins.add(innerTopOuterBottomBoardWin);
        }
        diagonalsThroughBoardsWins.addAll(innerTopOuterBottomBoardWins);

        List<List<Integer>> leftRightDiagsThroughBoardWins = new ArrayList<>();
        for (int i = 0; i < n * n; i += n) {
            List<Integer> leftRightDiagsThroughBoardWin = new ArrayList<>();
            for (int j = 0, k = i; j < n; j++, k += n * n + 1) {
                leftRightDiagsThroughBoardWin.add(j, k);
            }
            leftRightDiagsThroughBoardWins.add(leftRightDiagsThroughBoardWin);
        }
        diagonalsThroughBoardsWins.addAll(leftRightDiagsThroughBoardWins);

        List<List<Integer>> rightLeftDiagsThroughBoardWins = new ArrayList<>();
        for (int i = n - 1; i < n * n; i += n) {
            List<Integer> rightLeftDiagThroughBoardWin = new ArrayList<>();
            for (int j = 0, k = i; j < n; j++, k += n * n - 1) {
                rightLeftDiagThroughBoardWin.add(j, k);
            }
            rightLeftDiagsThroughBoardWins.add(rightLeftDiagThroughBoardWin);
        }

        diagonalsThroughBoardsWins.addAll(rightLeftDiagsThroughBoardWins);

        return diagonalsThroughBoardsWins;
    }
}
