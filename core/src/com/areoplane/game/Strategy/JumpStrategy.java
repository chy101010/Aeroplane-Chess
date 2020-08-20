package com.areoplane.game.Strategy;

import com.areoplane.game.model.AirModel;
import com.areoplane.game.model.Player;

import java.util.List;


public class JumpStrategy implements IStrategy {
    public JumpStrategy() {
    }

    // Finding which Planes can jump
    @Override
    public Integer choosePlane(int roll, AirModel board) {
        Player player = board.getTurn();
        int[] positions = player.getPositions();
        List<Integer> planes = board.movablePlanes(player, roll);
        for (int plane : planes) {
            if ((roll + positions[plane] - 2) % 4 == 0 && (roll + positions[plane]) < 50) {
                return plane;
            }
        }
        return null;
    }
}
