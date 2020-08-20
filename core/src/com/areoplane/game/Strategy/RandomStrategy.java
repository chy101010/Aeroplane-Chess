package com.areoplane.game.Strategy;

import com.areoplane.game.model.AirModel;
import com.areoplane.game.model.Player;

import java.util.List;
import java.util.Random;

public class RandomStrategy implements IStrategy {
    private Random rand;

    public RandomStrategy() {
        this(new Random());
    }

    public RandomStrategy(Random rand) {
        this.rand = rand;
    }

    @Override
    public Integer choosePlane(int roll, AirModel board) {
        Player player = board.getTurn();
        List<Integer> planes = board.movablePlanes(player, roll);
        if (planes.size() > 0) {
            return planes.get(this.rand.nextInt(planes.size()));
        }
        return null;
    }
}
