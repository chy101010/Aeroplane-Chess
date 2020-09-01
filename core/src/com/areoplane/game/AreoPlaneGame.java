package com.areoplane.game;

import com.areoplane.game.Screens.Assets;
import com.areoplane.game.Screens.MainMenuScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


/**
 * This class identifies the attributes of the game.
 */
public class AreoPlaneGame extends Game {
    // Value
    public static final int WORLD_WIDTH = 100;
    public static final int WORLD_HEIGHT = 100;
    public static final String file = "Games.txt";

    // Drawing
    public SpriteBatch batch;
    public BitmapFont font;

    // Camera
    public OrthographicCamera camera;
    public Viewport viewport;

    /**
     * This will be called when an {@code AreoPlaneGame} is constructed. This method loads the assets, camera,
     * viewport, drawing font and batch, and sets the screen to {@code MainMenuScree}.
     */
    @Override
    public void create() {
        Assets.load();
        this.font = new BitmapFont();
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, this.camera);
        // go to the MainMenuScreen
        this.setScreen(new MainMenuScreen(this));
    }

    /**
     * Updates the {@code viewport} and {@code batch} according to the given {@code width} and {@code height}.
     *
     * @param width  of the game screen
     * @param height of the game screen
     */
    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height, true);
        this.batch.setProjectionMatrix(this.camera.combined);
    }
}
