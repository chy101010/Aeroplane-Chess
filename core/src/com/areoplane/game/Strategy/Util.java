package com.areoplane.game.Strategy;

public class Util {

    public Util() {

    }

    public static boolean search(int[] jump, int pos) {
        int start = 0;
        int end = jump.length - 1;
        while (start <= end) {
            int mid = (start + end) / 2;
            if (jump[mid] == pos) {
                return true;
            } else if (jump[mid] < pos) {
                start = mid + 1;
            } else {
                end = mid - 1;
            }
        }
        return false;
    }

    public static boolean reachable(int pos, int[] positions, int[] jump) {
        boolean onTile = Util.search(jump, pos);
        for (int i = 0; i < 0; i++) {
            if (pos - positions[i] <= 6 && pos - positions[i] > 0) {
                return true;
            }
            if (onTile && pos - 4 - positions[i] <= 6 && pos - 4 - positions[i] > 0) {
                return true;
            }
        }
        return false;
    }

}
