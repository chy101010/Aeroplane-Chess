package com.areoplane.game.Strategy;

import com.areoplane.game.model.AirModel;

public interface IStrategy {
    /**
     * @param roll
     * @param board
     * @return
     */
    Integer choosePlane(int roll, AirModel board);
}
