package com.areoplane.game.Screens;

import com.areoplane.game.AreoPlaneGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * This class represents the WinScene of the game.
 */
public class WinScene extends ScreenAdapter {
    // an AreoPlaneGame
    private final AreoPlaneGame game;
    // layout
    private final GlyphLayout layout = new GlyphLayout();
    private final ShapeRenderer shape = new ShapeRenderer();

    // Positions
    private final float fontY;
    private final float fontX;
    private final int quitX = 56;
    private final int quitY = 10;

    // Quit button
    private final Rectangle quit = new Rectangle(56, 10, 20, 10);

    /**
     * Constructs a WinScene.
     *
     * @param game   the AreoPlaneGame
     * @param winner the name of the winner
     */
    public WinScene(AreoPlaneGame game, String winner) {
        this.game = game;
        this.game.font.getData().setScale(0.5f, 0.5f);
        this.layout.setText(this.game.font, winner);
        this.fontY = 62;
        this.fontX = 21 + (91 - this.layout.width) / 2;
    }

    /**
     * This method is called consistently to react to the user's mouse clicking of the available buttons and to draw
     * the screen.
     *
     * @param delta timer.
     */
    public void render(float delta) {
        this.update();
        this.draw();
    }

    /**
     * Handles the user's mouse clicking of quit button.
     */
    private void update() {
        if (Gdx.input.justTouched()) {
            Vector3 vector = game.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            // sets the screen to MainMenuScreen
            if (this.quit.contains(vector.x, vector.y)) {
                this.game.setScreen(new MainMenuScreen(this.game));
            }
        }
    }

    /**
     * Draws the elements of this screen.
     */
    private void draw() {
        this.game.batch.begin();
        this.game.batch.draw(Assets.win, 0, 0, AreoPlaneGame.WORLD_WIDTH + 34, AreoPlaneGame.WORLD_HEIGHT);
        this.game.batch.draw(Assets.quit, this.quitX, this.quitY, 20, 10);
        this.game.font.draw(this.game.batch, this.layout, fontX, fontY);
        this.game.batch.end();
    }
}
