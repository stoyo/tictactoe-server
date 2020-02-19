package com.three.d.tic.tac.toe.domain;

public enum GamePlayer {
    X,
    O;

    public static GamePlayer fromChar(char ch) {
        return GamePlayer.valueOf(Character.toString(ch));
    }
}
