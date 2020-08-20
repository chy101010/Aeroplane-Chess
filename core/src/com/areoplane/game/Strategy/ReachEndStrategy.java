package com.areoplane.game.Strategy;

import com.areoplane.game.model.AirModel;
import com.areoplane.game.model.Player;

import java.util.List;

public class ReachEndStrategy implements IStrategy {
    public ReachEndStrategy() {

    }

    // Return the furthest movable plane
    @Override
    public Integer choosePlane(int roll, AirModel board) {
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
