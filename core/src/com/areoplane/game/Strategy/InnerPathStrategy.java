package com.areoplane.game.Strategy;

import com.areoplane.game.Model.AreoModel;
import com.areoplane.game.Model.Player;

/**
 * This strategy returns the plane can get to the inner path or the end point after moving the given {@code steps}, else
 * {@code null} if there is no such a plane.
 */
public class InnerPathStrategy implements IStrategy {
    /**
     * Constructs an inner path strategy.
     */
    public InnerPathStrategy() {
    }

    // Choose the plane that can get into the inner path or land
    @Override
    public Integer choosePlane(int steps, AreoModel board) {
        Player player = board.getTurn();
        int[] positions = player.getPositions();
        for (int i = 0; i < 4; i++) {
            if (positions[i] + steps == 56) {
                return i;
            } else if (positions[i] <= 50 && positions[i] + steps > 50) {
                return i;
            }
        }
        return null;
    }
}
