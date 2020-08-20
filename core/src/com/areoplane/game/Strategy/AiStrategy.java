package com.areoplane.game.Strategy;

import com.areoplane.game.model.AirModel;

public class AiStrategy implements IStrategy {
    private IStrategy[] strategies;

    public AiStrategy(IStrategy... strategies) {
        this.strategies = strategies;
    }

    @Override
    public Integer choosePlane(int roll, AirModel board) {
        for (int i = 0; i < this.strategies.length; i++) {
            Integer move = this.strategies[i].choosePlane(roll, board);
            if (move != null) {
                return move;
            }
        }
        return null;
    }
}
