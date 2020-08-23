package com.areoplane.game.Strategy;

import com.areoplane.game.Model.AreoModel;
import com.areoplane.game.Model.Player;

import java.util.List;

/**
 * This strategy returns the plane that can crash planes of another player after moving the given {@code steps}, else
 * {@code null} if there is no such a plane.
 */
public class CrashStrategy implements IStrategy {
    /**
     * Constructs a crash strategy.
     */
    public CrashStrategy() {
    }

    @Override
    public Integer choosePlane(int steps, AreoModel board) {
        Player player = board.getTurn();
        Player[] players = board.getPlayers();
        List<Integer> planes = board.movablePlanes(player, steps);
        for (int plane : planes) {
            List<Integer> crashing = board.stayedPositions(player, plane, steps);
            for (int j = 0; j < 4; j++) {
                if (!players[j].equals(player)) {
                    int[] pos = board.positions(players[j]);
                    for (int p : pos) {
                        if (crashing.contains(p)) {
                            return plane;
                        }
                    }
                }
            }
        }
        return null;
    }
}
