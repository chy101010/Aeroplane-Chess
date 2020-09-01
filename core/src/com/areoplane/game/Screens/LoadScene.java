package com.areoplane.game.Screens;

import com.areoplane.game.AreoPlaneGame;
import com.areoplane.game.Controller.AreoController;
import com.areoplane.game.Controller.Features;
import com.areoplane.game.View.AirView;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.Arrays;
import java.util.List;

/**
 * This class represents the load screen of the game.
 */
public class LoadScene extends ScreenAdapter implements AirView {
    // an AreoPlaneGame
    private final AreoPlaneGame game;
    // features
    private Features features;
    // game data to be displayed
    // each row represents a game
    // each column in a row represents a player
    // data[0][0] = the first player's planes in the data slot of 0
    private int[][] data;
    // the bottom left coordinates of the load buttons
    private final Vector2[] loadPoints = new Vector2[4];
    // the bottom left coordinates of the delete buttons
    private final Vector2[] deletePoints = new Vector2[4];
    // the coordinates of the game data
    // data[0][0] = the coordinate for the first player in the data slot of 0
    private Vector2[][] dataCoordinates;
    // the previous screen
    private final ScreenAdapter prev;
    // the x coordinate of the back button
    private final int backX = 120;
    // the y coordinate of the back button
    private final int backY = 92;
    // a boolean value ot indicate whether there is a model to be saved.
    private final boolean isSave;

    /**
     * Constructs a {@code LoadScene}.
     *
     * @param game   the AreoPlaneGame
     * @param isSave a boolean value ot indicate whether there is a model to be saved.
     * @param prev   the previous screen
     */
    public LoadScene(AreoPlaneGame game, boolean isSave, ScreenAdapter prev) {
        this.game = game;
        this.isSave = isSave;
        this.prev = prev;
        this.dataCoordinates();
        this.buttonCoordinates();
        // Sets the font size
        this.game.font.getData().setScale(0.4f);
    }

    /**
     * This method is called consistently to react to the user's mouse clicking of the available buttons and to draw
     * the screen.
     *
     * @param delta timer.
     */
    @Override
    public void render(float delta) {
        this.update();
        this.draw();
    }

    @Override
    public void setFeatures(Features features) {
        this.features = features;
    }

    @Override
    public void setBoard(int[][] data) {
        this.data = data;
    }

    /**
     * Handles the user's mouse clicking of save/load button, backing button, and delete button.
     *
     * @throws ClassCastException if this {@code features} is not an instance of {@code AreoController}.
     **/
    private void update() {
        // loads the data
        this.features.loadData();
        if (Gdx.input.isTouched()) {
            Vector3 vector = this.game.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            for (int i = 0; i < 4; i++) {
                // clicks load button
                if (new Rectangle(this.loadPoints[i].x, this.loadPoints[i].y, 11, 7).contains(
                        vector.x, vector.y)) {
                    this.clickLoadButton(i);
                    return;
                }
                // clicks delete button
                else if (new Rectangle(this.deletePoints[i].x, this.deletePoints[i].y, 11, 7).contains(
                        vector.x, vector.y)) {
                    this.clickDeleteButton(i);
                    return;
                }
            }
            // clicks back button
            if (new Rectangle(this.backX, this.backY, 14, 8).contains(vector.x, vector.y)) {
                this.clickBackButton();
            }
        }
    }

    /**
     * This methods handles the data saving/loading one of the load buttons is clicked. Data could only be saved in to
     * a slot that is empty, and data could only be loaded from a slot that is not empty.
     *
     * @param slot the slot of the data.
     * @throws ClassCastException if this {@code features} is not an instance of {@code AreoController}.
     */
    private void clickLoadButton(int slot) {
        if (!this.isSave) {
            try {
                this.features.loadGame(slot);
                GameScene gameScene = new GameScene(this.game);
                ((AreoController) this.features).setView(gameScene);
                ((AreoController) this.features).play();
                this.game.setScreen(gameScene);
            } catch (NullPointerException ignored) {
            }
        } else {
            this.features.saveData(slot);
        }
    }

    /**
     * This methods handles the screen switching when the {@code back} button is clicked.
     *
     * @param slot the slot of the data
     */
    private void clickDeleteButton(int slot) {
        this.features.deleteData(slot);
    }

    /**
     * This methods handles the screen switching when the {@code back} button is clicked.
     */
    private void clickBackButton() {
        if (this.isSave) {
            ((AreoController) this.features).setView((AirView) this.prev);
            ((AreoController) this.features).play();
            this.game.font.getData().setScale(0.3f, 0.3f);
        }
        this.game.setScreen(this.prev);
    }

    /**
     * Draws the elements of this screen.
     */
    private void draw() {
        this.game.batch.begin();
        this.game.batch.draw(Assets.load, 0, 0, AreoPlaneGame.WORLD_WIDTH + 34, AreoPlaneGame.WORLD_HEIGHT);
        this.game.batch.draw(Assets.back, this.backX, this.backY, 14, 8);
        this.drawData();
        this.game.batch.end();
    }

    /**
     * Draws the game {@code data} using {@code dataCoordinates}.
     */
    private void drawData() {
        if (this.data != null) {
            for (int i = 0; i < 4; i++) {
                if (this.data[i][0] != Integer.MIN_VALUE) {
                    Vector2[] coordinate = this.dataCoordinates[i];
                    int[] data = this.data[i];
                    int start = 0;
                    for (int j = 0; j < 4; j++) {
                        this.game.font.draw(this.game.batch,
                                Arrays.toString(Arrays.copyOfRange(data, start, start + 4)),
                                coordinate[j].x, coordinate[j].y);
                        start += 4;
                    }
                }
            }
        }
    }

    /**
     * Sets up the buttons' coordinates.
     */
    private void buttonCoordinates() {
        int[] points = new int[]{67, 47, 27, 7};
        for (int i = 0; i < 4; i++) {
            this.loadPoints[i] = new Vector2(5, points[i]);
            this.deletePoints[i] = new Vector2(119, points[i]);
        }
    }


    /**
     * Sets up the data's coordinates.
     */
    private void dataCoordinates() {
        Vector2[][] cord = new Vector2[4][4];
        int col = 32;
        int row = 67;
        int[] c = new int[]{0, 0, 1, 1};
        int[] r = new int[]{0, 1, 1, 0};
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                cord[i][j] = new Vector2(col + 46 * c[j], row + 11 * r[j]);
            }
            row -= 20;
        }
        this.dataCoordinates = cord;
    }


    /**
     * @throws UnsupportedOperationException this method is unsupported for this view.
     */
    @Override
    public void setDice(int roll) {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * @throws UnsupportedOperationException this method is unsupported for this view.
     */
    @Override
    public void setIntermediate(int player, List<Integer> steps) {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * @throws UnsupportedOperationException this method is unsupported for this view.
     */
    @Override
    public void setTurn(String player) {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * @throws UnsupportedOperationException this method is unsupported for this view.
     */
    @Override
    public void setTime(int time) {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * @throws UnsupportedOperationException this method is unsupported for this view.
     */
    @Override
    public void setMessage(String message) {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * @throws UnsupportedOperationException this method is unsupported for this view.
     */
    @Override
    public void setWinner(String winner) {
        throw new UnsupportedOperationException("Not supported");
    }
}
