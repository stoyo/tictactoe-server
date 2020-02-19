package com.three.d.tic.tac.toe.domain;

public enum GameBoardTileState {
    X("x"),
    O("o"),
    BLANK("_");

    private String tileState;

    GameBoardTileState(String tileState) {
        this.tileState = tileState;
    }

    public static GameBoardTileState fromChar(char ch) {
        for (GameBoardTileState gameBoardTileState : GameBoardTileState.values()) {
            if (gameBoardTileState.tileState.equalsIgnoreCase(Character.toString(ch))) {
                return gameBoardTileState;
            }
        }

        throw new IllegalArgumentException(Character.toString(ch));
    }

    public String asString() {
        return tileState;
    }
}
