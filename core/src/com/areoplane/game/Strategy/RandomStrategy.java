package com.areoplane.game.Strategy;

import com.areoplane.game.Model.AreoModel;
import com.areoplane.game.Model.Player;

import java.util.List;
import java.util.Random;

/**
 * This strategy randomly return a movable plane with the given {@code steps}, else {@code null} if there is no movable
 * plane.
 */
public class RandomStrategy implements IStrategy {
    private final Random rand;

    /**
     * Constructs a random strategy.
     */
    public RandomStrategy() {
        this(new Random());
    }

    /**
     * This constructor is used for testing by giving it a seeded {@code Random}.
     */
    public RandomStrategy(Random rand) {
        this.rand = rand;
    }

    @Override
    public Integer choosePlane(int roll, AreoModel board) {
        Player player = board.getTurn();
        List<Integer> planes = board.movablePlanes(player, roll);
        if (planes.size() > 0) {
            return planes.get(this.rand.nextInt(planes.size()));
        }
        return null;
    }
}
