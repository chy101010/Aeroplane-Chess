package com.areoplane.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.areoplane.game.Controller.AreoController;
import com.areoplane.game.Controller.singlePlayerController;
import com.areoplane.game.AreoPlaneGame;
import com.areoplane.game.Model.AreoBoardImpl;

/**
 * This class represents the MainMenuScreen of the game.
 */
public class MainMenuScreen extends ScreenAdapter {
    // the AreoPlaneGame
    private final AreoPlaneGame game;
    // the playButton
    private final Rectangle playButton;
    // the loadButton
    private final Rectangle loadButton;


    /**
     * Constructs the MainMenuScreen.
     *
     * @param game the AreoPlaneGame
     */
    public MainMenuScreen(AreoPlaneGame game) {
        this.game = game;
        this.playButton = new Rectangle(86, 43, 38, 8);
        this.loadButton = new Rectangle(86, 55, 38, 8);
    }


    /**
     * Handles the user's mouse clicking of the play button or the load button.
     */
    public void update() {
        if (Gdx.input.isTouched()) {
            Vector3 vector = this.game.camera.unproject(new Vector3(Gdx.input.getX(),
                    Gdx.graphics.getHeight() - Gdx.input.getY(), 0));
            AreoController controller = new singlePlayerController(new AreoBoardImpl(), AreoPlaneGame.file);
            // sets to the game screen
            if (this.playButton.contains(vector.x, vector.y)) {
                GameScene gameScene = new GameScene(this.game);
                controller.setView(gameScene);
                controller.play();
                this.game.setScreen(gameScene);
            }
            // sets to the load screen
            if (this.loadButton.contains(vector.x, vector.y)) {
                LoadScene loadScene = new LoadScene(this.game, false, this);
                controller.setView(loadScene);
                controller.play();
                this.game.setScreen(loadScene);
            }
        }
    }

    /**
     * Draws the elements onto the screen.
     */
    public void draw() {
        this.game.batch.begin();
        this.game.batch.draw(Assets.mainMenu, 0, 0, AreoPlaneGame.WORLD_WIDTH + 34, AreoPlaneGame.WORLD_HEIGHT);
        this.game.batch.end();
    }

    /**
     * This method is consistently called.
     *
     * @param delta time.
     */
    @Override
    public void render(float delta) {
        this.update();
        this.draw();
    }
}
