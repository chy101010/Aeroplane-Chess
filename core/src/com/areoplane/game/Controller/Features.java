package com.areoplane.game.Controller;

/**
 * This interface represents a list of features that the {code AirView} client can query to interact with the model.
 */
public interface Features {
    /**
     * Updates the game state given {@code time}.
     *
     * @param time
     */
    void instruction(int time);

    /**
     * Moves the plane at {@code position}.
     *
     * @param position the position of a tile on the board.
     */
    void playMove(int position);

    /**
     * Rolls the dice.
     */
    void rollDice();

    /**
     * Checks whether there is a winner.
     */
    void isEndGame();


    /**
     *
     */
    void saveData(int slot);

    /**
     *
     * @param slot
     */
    void loadGame(int slot);

    /**
     *
     * @param slot
     */
    void deleteData(int slot);

    /**
     *
     */
    void loadData();
}
