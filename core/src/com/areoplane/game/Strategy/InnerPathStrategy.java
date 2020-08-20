package com.areoplane.game.Strategy;

import com.areoplane.game.model.AirModel;
import com.areoplane.game.model.Player;

public class InnerPathStrategy implements IStrategy {
    public InnerPathStrategy() {

    }

    // Choose the plane that can get into the inner path or land
    @Override
    public Integer choosePlane(int roll, AirModel board) {
        Player player = board.getTurn();
        int[] positions = player.getPositions();
        for (int i = 0; i < 4; i++) {
            if (positions[i] + roll == 56) {
                return i;
            } else if (positions[i] <= 50 && positions[i] + roll > 50) {
                return i;
            }
        }
        return null;
    }
}
