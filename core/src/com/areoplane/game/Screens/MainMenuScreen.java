package com.areoplane.game.Screens;

import com.areoplane.game.model.PlayerImpl;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.areoplane.game.Assets;
import com.areoplane.game.Controller.AirController;
import com.areoplane.game.Controller.singlePlayerController;
import com.areoplane.game.AreoPlaneGame;
import com.areoplane.game.model.AirBoardImpl;

import java.util.Random;


public class MainMenuScreen extends ScreenAdapter {
    private final AreoPlaneGame game;

    private final Rectangle playButton;
    private final Rectangle loadButton;



    public MainMenuScreen(AreoPlaneGame game) {
        this.game = game;
        this.playButton = new Rectangle(86, 43, 38, 8);
        this.loadButton = new Rectangle(86, 55, 38, 8);
    }


    public void update() {
        if (Gdx.input.isTouched()) {
            Vector3 vector = this.game.camera.unproject(new Vector3(Gdx.input.getX(),
                    Gdx.graphics.getHeight() - Gdx.input.getY(), 0));
            if (this.playButton.contains(vector.x, vector.y)) {
                GameScene scene = new GameScene(this.game);
                AirController controller = new singlePlayerController(
                        new AirBoardImpl(new PlayerImpl("red", 50,50,50,50),
                                new PlayerImpl("yel", 50,50,50,50),
                                new PlayerImpl("blue", 50,50,50,50),
                                new PlayerImpl("green", 50,50,50,50), new Random()), scene);
                controller.play();
                this.game.setScreen(scene);
            }
            if (this.loadButton.contains(vector.x, vector.y)) {
                this.game.setScreen(new WinScene(this.game, "Hello"));
            }
        }
    }

    public void draw() {
        GL20 gl = Gdx.gl;
        gl.glClearColor(0, 0, 1, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.game.batch.begin();
        this.game.batch.draw(Assets.mainMenu, 0, 0, AreoPlaneGame.WORLD_WIDTH + 34, AreoPlaneGame.WORLD_HEIGHT);
        this.game.batch.end();
    }

    @Override
    public void render(float delta) {
        this.update();
        this.draw();
    }
}
