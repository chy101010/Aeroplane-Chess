package com.areoplane.game.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a AreoPlane chess of four {@code player} that
 */
public class AirBoardImpl implements AirModel {
    private Player[] players;
    private Player curPlayerNo;
    private Random rand;

    /**
     * A convenient constructor
     */
    public AirBoardImpl() {
        this(new PlayerImpl("Red"), new PlayerImpl("Yellow"),
                new PlayerImpl("Blue"), new PlayerImpl("Green"), new Random());
    }

    /**
     * @param one
     * @param two
     * @param three
     * @param four
     * @param rand
     */
    public AirBoardImpl(Player one, Player two, Player three, Player four, Random rand) {
        this.players = new Player[]{one, two, three, four};
        this.curPlayerNo = one;
        this.rand = rand;
    }

    @Override
    public int rollDice() {
        return rand.nextInt(6) + 1;
    }


    @Override
    public void move(Player player, int identity, int roll) {
        this.isValidRoll(roll);
        this.doesThisExist(player);
        this.isValidPlane(identity);
        int prev = player.getPositions()[identity];
        if (prev == -1 && roll > 4) {
            player.setPath(identity);
        } else {
            int pos = player.move(identity, roll);
            if ((pos - 2) % 4 == 0) {
                if (prev + roll == 14) {
                    this.crash(player, 18);
                    this.crash(player, 14);
                }
                if (prev + roll == 18) {
                    this.crash(player, 18);
                    this.crash(player, 30);
                } else {
                    this.crash(player, pos - 4);
                }
            }
            this.crash(player, pos);
        }
    }

    /**
     * Crashes all the planes at this {@code pos} that are not the planes of the {@code player}.
     *
     * @param pos position on the board.
     */
    private void crash(Player player, int pos) {
        for (int i = 0; i < 4; i++) {
            if (!this.players[i].equals(player)) {
                int[] positions = this.players[i].getPositions();
                for (int j = 0; j < 4; j++) {
                    if (offSet(positions[j], this.players[i]) == offSet(pos, player)) {
                        this.players[i].crash(j);
                    }
                }
            }
        }
    }

    @Override
    public Player getTurn() {
        return this.curPlayerNo;
    }

    @Override
    public boolean isGameOver() {
        for (Player p : this.players) {
            if (p.isWin()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Player getWinner() {
        for (Player p : this.players) {
            if (p.isWin()) {
                return p;
            }
        }
        return null;
    }

    @Override
    public Player[] getPlayers() {
        Player[] copyPlayers = new Player[this.players.length];
        for (int i = 0; i < copyPlayers.length; i++) {
            copyPlayers[i] = this.players[i];
        }
        return copyPlayers;
    }


    @Override
    public List<Integer> movablePlanes(Player player, int roll) {
        this.isValidRoll(roll);
        this.doesThisExist(player);
        List<Integer> arr = new ArrayList<>();
        int[] positions = this.curPlayerNo.getPositions();
        for (int i = 0; i < 4; i++) {
            if (roll > 4 && positions[i] != 56) {
                arr.add(i);
            }
            if (positions[i] < 56 && positions[i] > -1 && !arr.contains(i)) {
                arr.add(i);
            }
        }
        return arr;
    }

    @Override
    public void nextPlayer(int roll) {
        this.isValidRoll(roll);
        if (roll != 6) {
            this.curPlayerNo = this.players[(this.indexOfThis(this.curPlayerNo) + 1) % 4];
        }
    }

    @Override
    public int[] jumpTile(Player player) {
        this.doesThisExist(player);
        int index = this.indexOfThis(player);
        int[] arr = new int[13];
        if (index != 3) {
            for (int i = 0; i < 13; i++) {
                arr[i] = 4 * i + 2 + index;
            }
        } else {
            for (int i = 0; i < 13; i++) {
                arr[i] = 4 * i + 1;
            }
        }
        return arr;
    }

    @Override
    public int[] positions(Player player) {
        this.doesThisExist(player);
        return this.getPositions(player);
    }

    @Override
    public int[][] getBoard() {
        int[][] board = new int[4][4];
        for (int i = 0; i < 4; i++) {
            board[i] = this.getPositions(this.players[i]);
        }
        return board;
    }

    private int[] getPositions(Player player) {
        int[] pos = player.getPositions();
        int[] arr = new int[4];
        for (int i = 0; i < 4; i++) {
            arr[i] = this.offSet(pos[i], player);
        }
        return arr;
    }

    @Override
    public List<Integer> stayedPositions(Player player, int identity, int roll) {
        this.doesThisExist(player);
        this.isValidPlane(identity);
        List<Integer> positions = player.stayedPositions(identity, roll);
        List<Integer> arr = new ArrayList<>();
        for (int pos : positions) {
            arr.add(this.offSet(pos, player));
        }
        return arr;
    }

    @Override
    public List<Integer> intermediatePositions(Player player, int identity, int roll) {
        this.doesThisExist(player);
        this.isValidPlane(identity);
        List<Integer> positions = player.intermediatePositions(identity, roll);
        List<Integer> arr = new ArrayList<>();
        for (int pos : positions) {
            arr.add(this.offSet(pos, player));
        }
        return arr;
    }

    /**
     * Returns the index of this {@code player}.
     *
     * @param player the player
     * @return the index of the player correspond to its corner.
     */
    private int indexOfThis(Player player) {
        for (int i = 0; i < 4; i++) {
            if (this.players[i].equals(player)) {
                return i;
            }
        }
        throw new IllegalArgumentException("The given player doesn't exist!");
    }


    /**
     * Throws an exception if this {@code player} isn't in this game.
     *
     * @param player the player
     */
    private void doesThisExist(Player player) {
        for (Player i : this.players) {
            if (i.equals(player)) {
                return;
            }
        }
        throw new IllegalArgumentException("The given player isn't in this game!");
    }

    /**
     * Throws an exception if this {@code roll} isn't 1 to 6 inclusive.
     *
     * @param roll the roll
     */
    private void isValidRoll(int roll) {
        if (roll < 1 || roll > 6) {
            throw new IllegalArgumentException("The given roll is invalid");
        }
    }

    /**
     * Throws an exception if this {@code plane} isn't 0-3 inclusive
     *
     * @param plane the plane
     */
    private void isValidPlane(int plane) {
        if (plane < 0 || plane > 3) {
            throw new IllegalArgumentException("The given plane doesn't exist!");
        }
    }


    /**
     * Offsets the {@code pos} according to which corners of this {@code player} is located.
     *
     * @param pos    the position in this player's own path
     * @param player the player
     * @return the offset position
     */
    private int offSet(int pos, Player player) {
        if (player.equals(this.players[0])) {
            return this.offSetBottomLeft(pos);
        } else if (player.equals(this.players[1])) {
            return this.offSetTopLeft(pos);
        } else if (player.equals(this.players[2])) {
            return this.offSetTopRight(pos);
        } else {
            return this.offSetBottomRight(pos);
        }
    }

    // 53 - 58 is the inner path of this TopLeftPlayer and 79 is the port and 80 is the path
    private int offSetTopLeft(int pos) {
        if (pos == -1) {
            return 79;
        } else if (pos == 0) {
            return 80;
        } else if (pos >= 40 && pos <= 50) {
            return pos - 39;
        } else if (pos > 50) {
            return pos - 50 + 52;
        } else {
            return pos + 13;
        }
    }

    // 59 - 64 is the inner path of this TopRightPlayer and 81 is the port and 82 is the path
    private int offSetTopRight(int pos) {
        if (pos == -1) {
            return 81;
        } else if (pos == 0) {
            return 82;
        } else if (pos >= 27 && pos <= 50) {
            return pos - 26;
        } else if (pos > 50) {
            return pos - 50 + 58;
        } else {
            return pos + 26;
        }
    }

    // 65 - 70 is the inner path of this BottomRightPlayer and 83 is the port and 84 is path
    private int offSetBottomRight(int pos) {
        if (pos == -1) {
            return 83;
        } else if (pos == 0) {
            return 84;
        } else if (pos >= 14 && pos <= 50) {
            return pos - 13;
        } else if (pos > 50) {
            return pos - 50 + 64;
        } else {
            return pos + 39;
        }
    }

    // 71 - 76 is the inner path of this BottomLeftPlayer and 77 is the Port and 78 is the Path
    private int offSetBottomLeft(int pos) {
        if (pos == -1) {
            return 77;
        } else if (pos == 0) {
            return 78;
        } else if (pos > 50) {
            return pos - 50 + 70;
        } else {
            return pos;
        }
    }
}
