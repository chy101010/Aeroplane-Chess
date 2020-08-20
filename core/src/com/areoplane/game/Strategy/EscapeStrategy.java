package com.areoplane.game.Strategy;

import com.areoplane.game.model.AirModel;
import com.areoplane.game.model.Player;

import java.util.List;

public class EscapeStrategy implements IStrategy {
    public EscapeStrategy() {

    }

    // Choose the plane that can escape the reachable range of other's plane
    @Override
    public Integer choosePlane(int roll, AirModel board) {
        Player player = board.getTurn();
        Player[] players = board.getPlayers();
        int[] positions = board.positions(player);
        List<Integer> planes = board.movablePlanes(player, roll);
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
