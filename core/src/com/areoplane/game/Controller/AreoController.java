package com.areoplane.game.Controller;

import com.areoplane.game.View.AirView;

/**
 * This interface represents an areoplane chess game controller that allows the client to play the game.
 */
public interface AreoController {
    /**
     * Initializes the game.
     */
    void play();

    /**
     * Sets the view of this {@code AreoController} to {@code view}.
     *
     * @param view an {@code AirView}
     * @throws NullPointerException if {@code view} is null.
     */
    void setView(AirView view);
}
