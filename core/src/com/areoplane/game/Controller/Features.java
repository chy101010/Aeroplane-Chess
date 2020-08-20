package com.areoplane.game.Controller;

public interface Features {
    void instruction(int time);

    void playMove(int position);

    void rollDice();

    void isEndGame();
}
