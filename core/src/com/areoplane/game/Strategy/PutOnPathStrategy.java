package com.areoplane.game.Strategy;


import com.areoplane.game.model.AirModel;
import com.areoplane.game.model.Player;

public class PutOnPathStrategy implements IStrategy {
    public PutOnPathStrategy() {

    }

    @Override
    public Integer choosePlane(int roll, AirModel board) {
        Player player = board.getTurn();
        int[] positions = player.getPositions();
        if (roll > 4) {
            for (int i = 0; i < 4; i++) {
                if (positions[i] == -1) {
                    return i;
                }
            }
        }
        return null;
    }
}
