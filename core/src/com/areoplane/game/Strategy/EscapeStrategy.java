package com.areoplane.game.Strategy;

import com.areoplane.game.Model.AreoModel;
import com.areoplane.game.Model.Player;

import java.util.List;

/**
 * This strategy returns the plane that can be crashed/reached by planes of another player within six steps, else
 * {@code null} if there is no such a plane.
 */
public class EscapeStrategy implements IStrategy {
    /**
     * Constructs an escape strategy.
     */
    public EscapeStrategy() {
    }

    // Choose the plane that can escape the reachable range of other's plane
    @Override
    public Integer choosePlane(int steps, AreoModel board) {
        Player player = board.getTurn();
        Player[] players = board.getPlayers();
        int[] positions = board.positions(player);
        List<Integer> planes = board.movablePlanes(player, steps);
        for (int plane : planes) {
            for (int i = 0; i < 4; i++) {
                if (!players[i].equals(player)) {
                    for (int j = 0; j < 4; j++) {
                        for (int step = 1; step < 7; step++) {
                            if (board.stayedPositions(players[i], j, step).contains(positions[plane])) {
                                return plane;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}
