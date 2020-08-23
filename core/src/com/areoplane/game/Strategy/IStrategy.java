package com.areoplane.game.Strategy;

import com.areoplane.game.Model.AreoModel;

/**
 * This interface represents an areoplan chess strategy that calculates the plane of the player in turn to move under
 * specific criteria.
 */
public interface IStrategy {
    /**
     * Return the plane in the {@code board} to move given the {@code steps}.
     *
     * @param steps the steps that the plane is going to move
     * @param board the board model
     * @return the of the plane to move, else {@code null} if there is no movable plane under the criteria.
     */
    Integer choosePlane(int steps, AreoModel board);
}
