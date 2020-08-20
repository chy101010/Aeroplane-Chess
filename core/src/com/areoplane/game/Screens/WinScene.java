package com.areoplane.game.Screens;

import com.areoplane.game.AreoPlaneGame;
import com.areoplane.game.Assets;
import com.areoplane.game.Controller.AirController;
import com.areoplane.game.Controller.singlePlayerController;
import com.areoplane.game.model.AirBoardImpl;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;


public class WinScene extends ScreenAdapter {
    private final AreoPlaneGame game;
    private final GlyphLayout layout = new GlyphLayout();
    private final BitmapFont font = new BitmapFont();
    private final Rectangle newGame;
    private final Rectangle quit;
    private final float fontY;
    private final float fontX;
    private ShapeRenderer shape = new ShapeRenderer();


    public WinScene(AreoPlaneGame game, String winner) {
        this.game = game;
        this.newGame = new Rectangle(34, 40, 31, 10);
        this.quit = new Rectangle(34, 24, 31, 10);
        this.font.getData().setScale(0.5f, 0.5f);
        this.layout.setText(this.font, winner);
        this.fontY = 62;
        this.fontX = 21 + (91 - this.layout.width) / 2;
    }

    private void update() {
        if (Gdx.input.justTouched()) {
            Vector3 vector = game.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            if (this.quit.contains(vector.x, vector.y)) {
                this.game.setScreen(new MainMenuScreen(this.game));
                return;
            }

            if (this.newGame.contains(vector.x, vector.y)) {
                GameScene scene = new GameScene(this.game);
                AirController controller = new singlePlayerController(
                        new AirBoardImpl(), scene);
                controller.play();
                this.game.setScreen(scene);
            }
        }
    }

    public void render(float delta) {
        this.update();
        this.draw();
    }

    public void draw() {
        GL20 gl = Gdx.gl;
        gl.glClearColor(0, 0, 1, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.game.batch.begin();
        this.game.batch.draw(Assets.win, 0, 0, AreoPlaneGame.WORLD_WIDTH + 34, AreoPlaneGame.WORLD_HEIGHT);
        this.font.draw(this.game.batch, this.layout, fontX, fontY);
        this.game.batch.end();

//        shape.setProjectionMatrix(this.game.camera.combined);
//
//        shape.begin(ShapeRenderer.ShapeType.Filled);
//        shape.setColor(1, 0, 0, 1);
//        shape.rect(21, 52, 91, 16);
//        shape.end();
    }
}
