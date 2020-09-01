package com.areoplane.game.Controller;

/**
 * This interface represents a list of features that the {code AirView} client can query to interact with the model.
 */
public interface Features {
    /**
     * Updates the game state given {@code time}.
     *
     * @param time the count down timer
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
     * Saves the current game data {@code AreoModel} into the file if there is no game data in the {@code slot}.
     *
     * @throws IllegalArgumentException if the given {@code slot} is less than 0 of greater than 3.
     */
    void saveData(int slot);

    /**
     * Loads the game data {@code AreoModel} in the {@code slot}.
     *
     * @param slot the index of the game data to load
     * @throws NullPointerException     if the there is no game data in the {@code slot}.
     * @throws IllegalArgumentException if {@code slot} is less than 0 or greater than 3.
     */
    void loadGame(int slot);

    /**
     * Deletes the game data from the file in the {@code slot}.
     *
     * @param slot the index of the game data to delete
     * @throws IllegalArgumentException if {@code slot} is less than 0 or greater than 3.
     */
    void deleteData(int slot);

    /**
     * Asks this {@code Features} for the data to be displayed.
     */
    void loadData();
}
