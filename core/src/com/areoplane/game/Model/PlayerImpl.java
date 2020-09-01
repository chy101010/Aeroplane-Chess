package com.areoplane.game.Model;

import java.util.*;

/**
 * This class represents an areoplane chess player with four planes. The positions of the planes are stored in
 * {@code planes}, and the indexes of the array are the identities of the planes.
 */
public class PlayerImpl implements Player {
    // name of the player
    private final String name;
    // position of the planes/
    // position -1            : the port.
    // position  0            : the starting position.
    // (position - 2) % 4 == 0: jump tiles
    // position 14 & 18       : short cut
    // positions 50 - 56      : the inner path
    private final int[] planes;

    /**
     * Constructs a PlayerImpl whose planes are all in port.
     *
     * @param name the name of the player.
     */
    public PlayerImpl(String name) {
        this(name, -1, -1, -1, -1);
    }

    /**
     * Constructs a PlayerImpl.
     *
     * @param name  the name of the player
     * @param one   the position of plane one
     * @param two   the position of plane two
     * @param three the position of plane three
     * @param four  the position of plane four
     */
    public PlayerImpl(String name, int one, int two, int three, int four) {
        this.name = name;
        this.planes = new int[]{one, two, three, four};
        for (int plane : this.planes) {
            if (plane < -1 || plane > 56) {
                throw new IllegalArgumentException("The Positions Of The Planes Must Be -1 to 56 Inclusive!");
            }
        }
    }

    @Override
    public String toString() {
        return this.planes[0] + " " + this.planes[1] + " " + this.planes[2] + " " + this.planes[3];
    }

    @Override
    public int move(int plane, int steps) {
        this.checkPlaneCondition(plane);
        this.checkSteps(steps);
        if (planes[plane] == -1) {
            throw new IllegalArgumentException("Plane cannot be moved. Not in a path.");
        }
        if (planes[plane] == 56) {
            throw new IllegalArgumentException("The plane has landed.");
        }
        this.planes[plane] = this.innerPathMove(this.getNewPos(steps + planes[plane]));
        return planes[plane];
    }

    @Override
    public int setPath(int plane) {
        this.checkPlaneCondition(plane);
        if (this.planes[plane] > -1) {
            throw new IllegalArgumentException("The given plane is already in the path!");
        }
        this.planes[plane] = 0;
        return 0;
    }

    @Override
    public void crash(int plane) {
        this.checkPlaneCondition(plane);
        if (this.planes[plane] < 1 || this.planes[plane] > 50) {
            throw new IllegalArgumentException("The given plane can't be crashed!");
        }
        this.planes[plane] = -1;
    }

    @Override
    public List<Integer> planesAt(int position) {
        if (position < -1 || position > 56) {
            throw new IllegalArgumentException("The given position is out of bound!");
        }
        List<Integer> planes = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (this.planes[i] == position) {
                planes.add(i);
            }
        }
        return planes;
    }

    @Override
    public boolean isWin() {
        for (int pos : this.planes) {
            if (pos != 56) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int[] getPositions() {
        int[] arr = new int[4];
        System.arraycopy(this.planes, 0, arr, 0, 4);
        return arr;
    }

    @Override
    public List<Integer> stayedPositions(int plane, int steps) {
        this.checkPlaneCondition(plane);
        this.checkSteps(steps);
        return this.steps(this.planes[plane], steps);
    }

    @Override
    public List<Integer> intermediatePositions(int plane, int steps) {
        this.checkPlaneCondition(plane);
        this.checkSteps(steps);
        List<Integer> arr;
        // If the plane is not bouncing off the ending points
        if (this.planes[plane] + steps <= 56) {
            arr = this.steps(this.planes[plane], steps);
            arr.add(0, this.planes[plane]);
            int len = arr.size();
            for (int i = 1; i < len; i++) {
                int gap = arr.get(i) - arr.get(i - 1) - 1;
                int start = arr.get(i - 1);
                if (gap < 11) {
                    while (gap != 0) {
                        start++;
                        arr.add(start);
                        gap--;
                    }
                }
            }
            Collections.sort(arr);
        }
        // If the plane is bouncing off the ending points
        else {
            arr = new ArrayList<>();
            int move = this.planes[plane];
            arr.add(move);
            boolean bounce = false;
            while (steps != 0) {
                if (move + 1 > 56 && !bounce) {
                    bounce = true;
                }
                if (bounce) {
                    move--;
                } else {
                    move++;
                }
                arr.add(move);
                steps--;
            }
        }
        return arr;
    }

    /**
     * Returns the positions of which the plane at {@code pos} will crash by moving {@code steps}.
     *
     * @param pos   the position of the plane
     * @param steps the steps to move the pos
     * @return a list of positions
     */
    private List<Integer> steps(int pos, int steps) {
        List<Integer> arr = new ArrayList<>();
        int move = pos + steps;
        int curPos = this.getNewPos(move);
        if (pos < 50) {
            if (pos != -1) {
                arr.add(move);
                if (!arr.contains(curPos)) {
                    arr.add(curPos);
                }
                if (move == 18) {
                    arr.add(arr.size() - 1, 30);
                } else if (move == 14) {
                    arr.add(arr.size() - 1, 18);
                }
            } else if (steps >= 5) {
                arr.add(0);
            }
        } else {
            arr.add(this.innerPathMove(curPos));
        }
        return arr;
    }

    /**
     * Returns the fixed {@code pos}. If it has exceeded the length of the path, 56, then bounce it backward.
     * Else, just return it.
     *
     * @param pos the position
     * @return the fixed position which will not be larger than 56
     */
    private int innerPathMove(int pos) {
        if (pos > 56) {
            return 56 - (pos - 56);
        } else {
            return pos;
        }
    }

    /**
     * Returns the fixed {@code pos}. If it is on a jump tile or a short cut tile, then move it forward. Else, just
     * return it.
     *
     * @param pos the position
     * @return the fixed position
     */
    private int getNewPos(int pos) {
        if ((pos - 2) % 4 == 0 && pos < 50) {
            if (pos == 14) {
                return 30;
            }
            if (pos == 18) {
                return 34;
            } else {
                return pos + 4;
            }
        } else {
            return pos;
        }
    }

    /**
     * Two players are equal if they have the same name and the positions of the planes are the same.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PlayerImpl)) {
            return false;
        }
        PlayerImpl cur = (PlayerImpl) obj;
        return cur.getName().equals(this.name) && Arrays.equals(cur.getPositions(), this.planes);
    }

    /**
     * Hasing the name and the positions of the planes.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.planes);
    }


    /**
     * Checks whether the given {@code steps} is within 1 to 6.
     *
     * @param steps the steps
     * @throws IllegalArgumentException if {@code steps} is not withint 1 to 6
     */
    private void checkSteps(int steps) {
        if (steps < 1 || 6 < steps) {
            throw new IllegalArgumentException("The steps should be 1 to 6.");
        }
    }

    /**
     * Checks whether the given {@code plane} is within 0 to 3.
     *
     * @param plane identity of the plan
     * @throws IllegalArgumentException if the given {@code plane} is not within 0 to 3.
     */
    private void checkPlaneCondition(int plane) {
        if (plane < 0 || plane > 3) {
            throw new IllegalArgumentException("The given plane doesn't exist!");
        }
    }


    /**
     * Represents a Builder pattern for this {@code PlayerImpl}.
     */
    public static final class Builder implements PlayerBuilder {
        private final int[] dp = new int[4];
        private String name = "";
        private int index = 0;

        public Builder() {
            Arrays.fill(this.dp, -1);
        }

        @Override
        public Player build() {
            return new PlayerImpl(this.name, this.dp[0], this.dp[1], this.dp[2], this.dp[3]);
        }

        @Override
        public PlayerBuilder setName(String name) {
            this.name = name;
            return this;
        }


        @Override
        public PlayerBuilder addPlanes(int position) {
            this.dp[index] = position;
            this.index = this.index == 3 ? 0 : this.index + 1;
            return this;
        }
    }

}
