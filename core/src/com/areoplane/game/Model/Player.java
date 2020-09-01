package com.areoplane.game.Model;

import java.util.List;

/**
 * This interface represents a player in an areoplane chess game. This interface supports the function that allows
 * the client to move/set path/crash its planes, and acquire information of the status and positions of its planes.
 */
public interface Player {
    //todo Testing
    String toString();

    /**
     * Moves the given move-able {@code plane} to with the amount of {@code steps}.
     *
     * @param plane the identity of the plane.
     * @param steps the steps to be moved.
     * @return the new position of the moved plane
     * @throws IllegalArgumentException if the given plane is not move-able or the given plane doesn't
     *                                  exist or the given steps aren't in the range of 1 to 6;
     */
    int move(int plane, int steps);

    /**
     * Sets the given in-port {@code plane} to the path.
     *
     * @param plane the identity of the plane.
     * @throws IllegalArgumentException if the given plane is in path or the given plane doesn't
     *                                  exist.
     * @returns the new position of the plane
     */
    int setPath(int plane);


    /**
     * Sends the given {@code plane} back to the port.
     *
     * @param plane the identity of the plane.
     * @throws IllegalArgumentException if the given plane is in the port or in the inner path or the
     *                                  given plane doesn't exist.
     */
    void crash(int plane);


    /**
     * Returns the list of planes at the given {@code position}.
     *
     * @param position the position
     * @return list of planes
     * @throws IllegalArgumentException if the position is out of bound
     */
    List<Integer> planesAt(int position);

    /**
     * Checks whether all the planes are landed.
     *
     * @return true if all the planes are landed, false otherwise.
     */
    boolean isWin();

    /**
     * Returns the name of this player.
     *
     * @return name.
     */
    String getName();

    /**
     * Returns the positions of the planes of this player.
     *
     * @return the positions.
     */
    int[] getPositions();

    // todo testing

    /**
     * Returns the positions of which the {@code plane} will crash given the {@code steps}.
     *
     * @param plane identity of the plane
     * @param steps the steps to be moved
     * @return list of positions of which the plane will crash
     * @throws IllegalArgumentException if the given plane is not move-able or the given plane doesn't
     *                                  exist or the given steps aren't in the range of 1 to 6;
     */
    List<Integer> stayedPositions(int plane, int steps);

    // todo testing exception

    /**
     * Returns a list of intermediate positions of which the {@code plane} will pass given the {@code steps}.
     *
     * @param plane identity of the plane
     * @param steps the steps to be moved
     * @return list of intermediate positions
     * @throws IllegalArgumentException if the given plane is not move-able or the given plane doesn't
     *                                  exist or the given steps aren't in the range of 1 to 6;
     */
    List<Integer> intermediatePositions(int plane, int steps);
}
