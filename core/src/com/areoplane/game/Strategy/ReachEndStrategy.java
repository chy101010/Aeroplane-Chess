package com.areoplane.game.Strategy;

import com.areoplane.game.Model.AreoModel;
import com.areoplane.game.Model.Player;

import java.util.List;

/**
 * This strategy returns the plane that is the furthest in its path, else return {@code null} if there is no movable
 * plane.
 */
public class ReachEndStrategy implements IStrategy {
    /**
     * Constructs a reach end strategy,
     */
    public ReachEndStrategy() {
    }

    // Return the furthest movable plane
    @Override
    public Integer choosePlane(int roll, AreoModel board) {
        int cur = Integer.MIN_VALUE;
        Integer prev = null;
        Player player = board.getTurn();
        int[] positions = player.getPositions();
        List<Integer> planes = board.movablePlanes(player, roll);
        for (int plane : planes) {
            if (positions[plane] > cur && positions[plane] <= 50) {
                cur = positions[plane];
                prev = plane;
            }
        }
        return prev;
    }
}
