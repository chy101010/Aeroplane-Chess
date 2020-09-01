package com.areoplane.game;

import com.areoplane.game.Screens.MainMenuScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


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


	@Override
	public void create() {
		Assets.load();
		this.font = new BitmapFont();
		this.batch = new SpriteBatch();
		this.camera = new OrthographicCamera();
		this.viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, this.camera);
		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void resize(int width, int height) {
		this.viewport.update(width, height, true);
		this.batch.setProjectionMatrix(this.camera.combined);
	}
}
