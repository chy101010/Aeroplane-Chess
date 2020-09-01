package com.areoplane.game.Screens;

import com.areoplane.game.AreoPlaneGame;
import com.areoplane.game.Assets;
import com.areoplane.game.Controller.AreoController;
import com.areoplane.game.Controller.Features;
import com.areoplane.game.View.AirView;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.Arrays;
import java.util.List;

public class LoadScene extends ScreenAdapter implements AirView {
    private final AreoPlaneGame game;
    private Features features;
    private int[][] data;
    private final BitmapFont font;
    private final Vector2[] loadPoints = new Vector2[4];
    private final Vector2[] deletePoints = new Vector2[4];
    private Vector2[][] coordinates;
    private ScreenAdapter prev;
    private final int backX = 120;
    private final int backY = 92;

    private final boolean isSave;

    public LoadScene(AreoPlaneGame game, boolean isSave, ScreenAdapter prev) {
        this.game = game;
        this.font = new BitmapFont();
        this.isSave = isSave;
        this.prev = prev;
        this.coordinates();
        this.font.getData().setScale(0.4f);
        int[] points = new int[]{67, 47, 27, 7};
        for (int i = 0; i < 4; i++) {
            this.loadPoints[i] = new Vector2(5, points[i]);
            this.deletePoints[i] = new Vector2(119, points[i]);
        }
    }


    @Override
    public void render(float delta) {
        this.update();
        this.draw();
    }

    private void update() {
        this.features.loadData();
        if (Gdx.input.isTouched()) {
            Vector3 vector = this.game.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            for (int i = 0; i < 4; i++) {
                if (new Rectangle(this.loadPoints[i].x, this.loadPoints[i].y, 11, 7).contains(
                        new Vector2(vector.x, vector.y))) {
                    if (!this.isSave) {
                        this.features.loadGame(i);
                        GameScene gameScene = new GameScene(this.game);
                        ((AreoController) this.features).setView(gameScene);
                        ((AreoController) this.features).play();
                        this.game.setScreen(gameScene);
                    } else {
                        this.features.saveData(i);
                    }
                } else if (new Rectangle(this.deletePoints[i].x, this.deletePoints[i].y, 11, 7).contains(
                        new Vector2(vector.x, vector.y))) {
                    this.features.deleteData(i);
                } else if (new Rectangle(this.backX, this.backY, 14, 8).contains(new Vector2(vector.x, vector.y))) {
                    if (this.isSave) {
                        ((AreoController) this.features).setView((AirView) this.prev);
                        ((AreoController) this.features).play();
                    }
                    this.game.setScreen(this.prev);
                }
            }
        }
    }


    private void draw() {
        GL20 gl = Gdx.gl;
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.game.batch.begin();
        this.game.batch.draw(Assets.load, 0, 0, AreoPlaneGame.WORLD_WIDTH + 34, AreoPlaneGame.WORLD_HEIGHT);
        this.game.batch.draw(Assets.back, this.backX, this.backY, 14, 8);
        this.drawData();
        this.game.batch.end();
    }

    private void drawData() {
        if (this.data != null) {
            for (int i = 0; i < this.data.length; i++) {
                Vector2[] coordinate = this.coordinates[i];
                int[] data = this.data[i];
                int start = 0;
                for (int j = 0; j < 4; j++) {
                    this.font.draw(this.game.batch, Arrays.toString(Arrays.copyOfRange(data, start, start + 4)),
                            coordinate[j].x, coordinate[j].y);
                    start += 4;
                }
            }
        }
    }

    private void coordinates() {
        Vector2[][] coord = new Vector2[4][4];
        int col = 32;
        int row = 67;
        int[] c = new int[]{0, 0, 1, 1};
        int[] r = new int[]{0, 1, 1, 0};
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                coord[i][j] = new Vector2(col + 46 * c[j], row + 11 * r[j]);
            }
            row -= 20;
        }

        this.coordinates = coord;
    }

    @Override
    public void setFeatures(Features features) {
        this.features = features;
    }

    @Override
    public void setBoard(int[][] data) {
        this.data = data;
    }


    @Override
    public void setDice(int roll) {
    }

    @Override
    public void setIntermediate(int player, List<Integer> steps) {
    }

    @Override
    public void setTurn(String player) {
    }

    @Override
    public void setTime(int time) {
    }

    @Override
    public void setMessage(String message) {
    }

    @Override
    public void setWinner(String winner) {
    }
}
