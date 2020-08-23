package com.areoplane.game.Strategy;


import com.areoplane.game.Model.AreoModel;
import com.areoplane.game.Model.Player;

/**
 * This strategy returns the plane that can be put on path given the {@code steps}, else return {@code null} if there is
 * * no such a plane.
 */
public class PutOnPathStrategy implements IStrategy {
    /**
     * Constructs a put on path strategy.
     */
    public PutOnPathStrategy() {
    }

    @Override
    public Integer choosePlane(int steps, AreoModel board) {
        Player player = board.getTurn();
        int[] positions = player.getPositions();
        if (steps > 4) {
            for (int i = 0; i < 4; i++) {
                if (positions[i] == -1) {
                    return i;
                }
            }
        }
        return null;
    }
}
