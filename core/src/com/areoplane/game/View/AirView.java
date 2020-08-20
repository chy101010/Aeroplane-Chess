package com.areoplane.game.View;

import com.areoplane.game.Controller.Features;

import java.util.List;

/**
 * This interface represents interactive views of the Areo-Plane Chess Game by linking it to {@code Features}.
 */
public interface AirView {
    /**
     * Takes in a list of features/services that is offered by the {@code features}.
     *
     * @param features the features
     * @throws IllegalArgumentException if the given {@code features} is null
     */
    void setFeatures(Features features);

    /**
     * Takes in the dice {@code roll} by the client and sets up the rolling animation.
     *
     * @param roll the dice roll
     * @throws IllegalArgumentException if the roll is less than 1 or greater than 6;
     */
    void setDice(int roll);

    /**
     * Takes in every plane's {@code positions} to be displayed.
     *
     * @param positions of the planes
     * @throws IllegalArgumentException if the positions are null
     */
    void setBoard(int[][] positions);

    /**
     * Takes in the intermediate {@code steps} of this {@code player} to be displayed
     *
     * @param player identity of the player
     * @param steps  the intermediate steps
     * @throws IllegalArgumentException if the player is less than 1 or greater than 4.
     */
    void setIntermediate(int player, List<Integer> steps);

    /**
     * Takes in the name of the {@code player} who is in turn to be displayed.
     *
     * @param player name of the player
     */
    void setTurn(String player);

    /**
     * Takes in the count down of each turn to be displayed.
     *
     * @param time the countdown
     */
    void setTime(int time);

    /**
     * Takes in the message to be displayed.
     *
     * @param message the message
     */
    void setMessage(String message);


    /**
     * Displays the given winner.
     *
     * @param winner name of the winner.
     */
    void setWinner(String winner);
}
