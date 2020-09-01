package com.areoplane.game.Controller;

import com.areoplane.game.View.AirView;

/**
 * This interface represents an areoplane chess game controller that allows the client to start the game.
 */
public interface AreoController {
    /**
     * Initialize the game.
     */
    void play();

    /**
     *
     * @param view
     */
    void setView(AirView view);
}
