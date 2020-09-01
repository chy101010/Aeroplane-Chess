package com.areoplane.game.Model;

/**
 * This interface resembles a builder pattern for {@code AreoModel}.
 */
public interface AreoBoardBuilder {
    /**
     * Constructs a final Board.
     *
     * @return the newly constructed Board.
     */
    AreoModel build();

    /**
     * Adds a {@code Player} with the given to the player.
     *
     * @param player the player
     * @return this {@link AreoBoardBuilder}
     */
    AreoBoardBuilder addPlayer(Player player);
}
