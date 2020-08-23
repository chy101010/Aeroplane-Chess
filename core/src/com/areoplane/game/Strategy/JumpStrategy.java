package com.areoplane.game.Strategy;

import com.areoplane.game.Model.AreoModel;
import com.areoplane.game.Model.Player;

import java.util.List;

/**
 * This strategy returns the plane can jump after moving the given {@code steps}, else return {@code null} if there is
 * no such a plane.
 */
public class JumpStrategy implements IStrategy {
    /**
     * Constructs a jump strategy.
     */
    public JumpStrategy() {
    }

    // Finding which Planes can jump
    @Override
    public Integer choosePlane(int steps, AreoModel board) {
        Player player = board.getTurn();
        int[] positions = player.getPositions();
        List<Integer> planes = board.movablePlanes(player, steps);
        for (int plane : planes) {
            if ((steps + positions[plane] - 2) % 4 == 0 && (steps + positions[plane]) < 50) {
                return plane;
            }
        }
        return null;
    }
}
