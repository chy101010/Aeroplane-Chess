package com.areoplane.game.Model;

/**
 * This interface resembles a builder pattern for {@code Player}.
 */
public interface PlayerBuilder {
    /**
     * Constructs a final Player.
     *
     * @return the newly constructed Player.
     */
    Player build();

    /**
     * Specify the name of the player.
     *
     * @param name the name of the player
     * @return this {@link PlayerBuilder}
     */
    PlayerBuilder setName(String name);

    /**
     * Adds a plane with the given {@code position} to the player.
     *
     * @param position the position of the plane
     * @return this {@link PlayerBuilder}
     */
    PlayerBuilder addPlanes(int position);
}
