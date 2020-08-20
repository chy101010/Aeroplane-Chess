package com.areoplane.game.Strategy;

import com.areoplane.game.model.AirModel;
import com.areoplane.game.model.Player;

import java.util.List;

public class CrashStrategy implements IStrategy {
    public CrashStrategy() {

    }

    @Override
    public Integer choosePlane(int roll, AirModel board) {
        Player player = board.getTurn();
        Player[] players = board.getPlayers();
        List<Integer> planes = board.movablePlanes(player, roll);
        for (int plane : planes) {
            List<Integer> crashing = board.stayedPositions(player, plane, roll);
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
