package com.areoplane.game.model;

import java.util.List;

/**
 *
 */
public interface AirModel {
    // Moved to view
    String toString();

    /**
     * Rolls the dice.
     *
     * @return a random integer from 1 to 6 inclusive.
     */
    int rollDice();

    /**
     * Moves/Puts On Path the plane pointed by {@code identity} of this {@code player} with the {@code
     * roll} dice.
     *
     * @param player   the player
     * @param identity of the plane
     * @param roll     the dice rolled
     * @throws IllegalArgumentException if the given identity is incorrect or the move is
     *                                  inappropriate or the player isn't in the game or the roll
     *                                  isn't 1-6 inclusive
     */
    void move(Player player, int identity, int roll);

    /**
     * Returns the player who is in turn.
     *
     * @return the player who is in turn.
     */
    Player getTurn();

    /**
     * Determines whether the game is over. The game is over when one of the four {@code player} has
     * four landed planes.
     *
     * @return true if the game is over, otherwise false.
     */
    boolean isGameOver();

    /**
     * Returns the winner if there is one.
     *
     * @return the {@code player} who has landed all his planes otherwise {@code null}
     */
    Player getWinner();

    /**
     * Returns the players who are currently in this game.
     *
     * @return a copied list of the players.
     */
    Player[] getPlayers();


    /**
     * Returns the movable planes of this {@code player} with the {@code roll} dice.
     *
     * @param player the player
     * @param roll   the dice rolled
     * @return a list of movable planes
     * @throws IllegalArgumentException if the player isn't in the game or the roll isn't 1-6
     *                                  inclusive
     */
    List<Integer> movablePlanes(Player player, int roll);


    /**
     * Sets the next player in turn with the {@code roll} dice.
     *
     * @throws IllegalArgumentException if the roll isn't 1-6 inclusive
     */
    void nextPlayer(int roll);


    /**
     * Returns the jump-able tiles of this {@code player}
     *
     * @param player the player
     * @return a list of jump-able
     * @throws IllegalArgumentException if the player isn't in the game
     */
    int[] jumpTile(Player player);

    /**
     * Returns the position on the board of this {@code player}'s planes.
     *
     * @param player the player
     * @return the positions
     * @throws IllegalArgumentException if the player isn't in the game
     */
    int[] positions(Player player);


    /**
     * Returns the position of every plane in this game.
     *
     * @return position of every plane
     */
    int[][] getBoard();


    // todo Testing
    /**
     * Returns the positions of the {@code plane} of this {@code player} will crash with given the {@code steps}
     * in this game board.
     *
     * @param player   the player
     * @param identity of the plane
     * @param roll     the dice rolled
     * @return list of positions of which the plane will crash
     * @throws IllegalArgumentException if the given identity is incorrect or the move is inappropriate
     *                                  or the player isn't in the game or the roll isn't 1-6 inclusive
     */
    List<Integer> stayedPositions(Player player, int identity, int roll);


    /**
     * Returns a list of intermediate positions of the {@code plane} of this {@code player} will pass given
     * the {@code steps} in this game board.
     *
     * @param player   the player
     * @param identity of the plane
     * @param roll     the dice rolled
     * @return list of intermediate positions
     * @throws IllegalArgumentException if the given identity is incorrect or the move is inappropriate or
     *                                  the player isn't in the game or the roll isn't 1-6 inclusive
     */
    List<Integer> intermediatePositions(Player player, int identity, int roll);
}
