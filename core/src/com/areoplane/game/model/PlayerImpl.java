package com.areoplane.game.model;


import java.util.*;

public class PlayerImpl implements Player {
    private final String name;
    private final int[] planes;


    public PlayerImpl(String name) {
        this(name, -1, -1, -1, -1);
    }

    public PlayerImpl(String name, int one, int two, int three, int four) {
        this.name = name;
        this.planes = new int[]{one, two, three, four};
        for (int plane : this.planes) {
            if (plane < -1 || plane > 56) {
                throw new IllegalArgumentException("The Positions Of The Planes Must Be -1 to 56 Inclusive!");
            }
        }
    }

    /**
     * @param plane
     */
    private void checkPlaneCondition(int plane) {
        if (plane < 0 || plane > 3) {
            throw new IllegalArgumentException("The given plane doesn't exist!");
        }
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
    public int setPath(int plane) {
        this.checkPlaneCondition(plane);
        if (this.planes[plane] > -1) {
            throw new IllegalArgumentException("The given plane is already in the path!");
        }
        this.planes[plane] = 0;
        return 0;
    }

    @Override
    public String getName() {
        return this.name;
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
    public int[] getPositions() {
        int[] arr = new int[4];
        System.arraycopy(this.planes, 0, arr, 0, 4);
        return arr;
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
        this.planes[plane] = this.endingMove(this.getNewPos(steps + planes[plane]));
        return planes[plane];
    }

    @Override
    public List<Integer> stayedPositions(int plane, int steps) {
        this.checkPlaneCondition(plane);
        this.checkSteps(steps);
        return this.steps(this.planes[plane], steps);
    }


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
            arr.add(this.endingMove(curPos));
        }
        return arr;
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

    private void checkSteps(int steps) {
        if (steps < 1 || 6 < steps) {
            throw new IllegalArgumentException("The steps should be 1 to 6.");
        }
    }

    private int endingMove(int move) {
        if (move > 56) {
            return 56 - (move - 56);
        } else {
            return move;
        }
    }

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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PlayerImpl)) {
            return false;
        }
        PlayerImpl cur = (PlayerImpl) obj;
        return cur.getName() == this.name && Arrays.equals(cur.getPositions(), this.planes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.planes);
    }
}
