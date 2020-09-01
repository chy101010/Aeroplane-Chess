package com.areoplane.game.Screens;

import com.areoplane.game.Controller.AreoController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.areoplane.game.Controller.Features;
import com.areoplane.game.AreoPlaneGame;
import com.areoplane.game.View.AirView;

import java.util.*;

/**
 * This class represents the GameScene of the game.
 */
public class GameScene extends ScreenAdapter implements AirView {
    // an AreoPlaneGame
    private final AreoPlaneGame game;
    // features
    private Features features;

    // Game State
    private final int GAME_RUNNING = 0;
    private final int GAME_PAUSE = 1;
    private final int GAME_MOVING = 2;
    private final int GAME_ROLLING = 3;
    private int state;

    // Data to be drawn
    // flying animation
    private Queue<Integer> intermediateSteps = new LinkedList<>();
    // rolling animation, this stack will be populated when the method setDice(int roll) is called.
    private final Stack<Integer> rollingDice = new Stack();
    // the plane that is flying
    private int intermediatePlayer = -1;
    // the current positions of the planes
    private int[][] planes;
    // the previous positions of hte planes
    private int[][] prev_planes;


    // Timers
    // This timer will not tick when the {@code rollingDice} or {@code intermediateSteps} is non-empty.
    // This is a the turn timer that will be used to communicate with the controller.
    private float update_timer = 1;
    // This timer wil not tick when the {@code rollingDice} or {@code intermediateSteps} is empty.
    // This is a the drawing timer to draw the animations.
    private float draw_timer = 0;

    // Dice
    private int dice = 1;
    private final Random random = new Random();

    // the message that shows the current action
    private String message;
    // the name of the current player in turn
    private String player;

    // Textures
    // dices
    private final Texture[] dices = new Texture[]{Assets.dice1, Assets.dice2, Assets.dice3, Assets.dice4,
            Assets.dice5, Assets.dice6};
    // positions
    private final Texture[] textures = new Texture[]{Assets.red, Assets.yellow, Assets.blue, Assets.green};

    // Positions
    private Vector3 vector;
    // mapping the index of each tile to its bottom-left coordinate.
    private final Map<Integer, Vector2> coordinates;
    private final int diceX = 107;
    private final int diceY = 75;
    private final int pauseX = 107;
    private final int pauseY = 30;
    private final int resumeX = 107;
    private final int resumeY = 18;
    private final int saveX = 107;
    private final int saveY = 12;
    private final int quitX = 107;
    private final int quitY = 5;

    // Buttons
    private final Rectangle DICE = new Rectangle(this.diceX, this.diceY, 20, 20);
    private final Rectangle PAUSE = new Rectangle(this.pauseX, this.pauseY, 20, 10);
    private final Rectangle RESUME = new Rectangle(this.resumeX, this.resumeY, 20, 10);
    private final Rectangle SAVE = new Rectangle(this.saveX, this.saveY, 20, 5);
    private final Rectangle QUIT = new Rectangle(this.quitX, this.quitY, 20, 5);


    /**
     * Constructs a {@code GameScene}.
     *
     * @param game the AreoPlaneGame
     */
    public GameScene(AreoPlaneGame game) {
        this.game = game;
        this.vector = new Vector3();
        this.coordinates = this.coordinate();
        this.game.font.getData().setScale(0.3f, 0.3f);
        this.state = this.GAME_RUNNING;
    }

    /**
     * @throws NullPointerException if the given {@code features} is null.
     */
    @Override
    public void setFeatures(Features features) {
        Objects.requireNonNull(features);
        this.features = features;
    }

    @Override
    public void setDice(int roll) {
        if (roll < 1 || roll > 6) {
            throw new IllegalArgumentException("The given roll can't be less than 1 or greater than 6");
        }
        this.dice = roll;
        // populate the dice animation.
        for (int i = 0; i < 20; i++) {
            this.rollingDice.push(this.random.nextInt(6));
        }
    }

    @Override
    public void setBoard(int[][] positions) {
        this.prev_planes = this.planes;
        this.planes = positions;
    }

    private void drawPlanes(int[][] planes) {
        for (int i = 0; i < 4; i++) {
            int[] dirY = new int[]{0, 1, 1, 0};
            int[] dirX = new int[]{0, 0, 1, 1};
            for (int j = 0; j < 4; j++) {
                int pos = planes[i][j];
                Vector2 vec = this.coordinates.get(planes[i][j]);
                if (pos == 77 || pos == 79 || pos == 81 || pos == 83) {
                    this.game.batch.draw(textures[i], vec.x + 8 * dirX[j], vec.y + 8 * dirY[j], 6, 6);
                } else {
                    this.game.batch.draw(textures[i], vec.x, vec.y, 6, 6);
                }
            }
        }
    }

    @Override
    public void setIntermediate(int player, List<Integer> positions) {
        if (player < 1 || player > 4) {
            throw new IllegalArgumentException("The given player can't be less than 1 or greater than 4");
        }
        this.intermediatePlayer = player - 1;
        // populates the {@code intermediateSteps}
        this.intermediateSteps = new LinkedList<>(positions);
    }

    @Override
    public void setTurn(String player) {
        this.player = player;
    }

    @Override
    public void setTime(int time) {
        this.update_timer = time;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }


    /**
     * Handles the control flow of this game. Determines what to display when this game is running, pausing, or running.
     *
     * @param delta timer
     */
    private void update(float delta) {
        switch (this.state) {
            case GAME_RUNNING:
                // increases the count down timer
                this.update_timer += delta;
                // asks the controller what to display
                this.features.instruction((int) this.update_timer);
                this.updateRunning();
                break;
            case GAME_PAUSE:
                this.updatePause();
                break;
            default:
                this.updateDraw();
                break;
        }
    }

    /**
     * Handles the control flow of this game when it is running.
     */
    private void updateRunning() {
        // sets the state to Moving when there are intermediate steps to be drawn.
        if (!this.intermediateSteps.isEmpty()) {
            this.state = GAME_MOVING;
            return;
        }
        // sets the state to Rolling when there are rolling animations to be drawn.
        else if (!this.rollingDice.isEmpty()) {
            this.state = GAME_ROLLING;
            return;
        }
        // checks whether the game has ended
        this.features.isEndGame();
        // handles the buttons clicking
        if (Gdx.input.isTouched()) {
            this.vector = this.game.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            // selects the plane to be moved
            this.selectPlane(vector.x, vector.y);
            // roll the dice
            if (this.DICE.contains(vector.x, vector.y)) {
                this.features.rollDice();
            }
            // pause the game
            else if (this.PAUSE.contains(vector.x, vector.y)) {
                this.state = GAME_PAUSE;
            }
            // quit the game
            else if (this.QUIT.contains(vector.x, vector.y)) {
                this.game.setScreen(new MainMenuScreen(this.game));
            }
        }
    }

    /**
     * Handles the control flow of this game when it is pausing.
     *
     * @throws ClassCastException if {@code features} is not a {@code AreoController}
     */
    private void updatePause() {
        // handles the buttons clicking
        if (Gdx.input.isTouched()) {
            this.vector = this.game.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            // resume the game
            if (this.RESUME.contains(vector.x, vector.y)) {
                // if one of the plane is flying, sets the state to Moving
                if (!this.intermediateSteps.isEmpty()) {
                    this.state = GAME_MOVING;
                }
                // if the dice is rolling, sets the state to Rolling
                else if (!this.rollingDice.isEmpty()) {
                    this.state = GAME_ROLLING;
                }
                // sets the state to Running
                else {
                    this.state = GAME_RUNNING;
                }
            }
            // quit the game
            else if (this.QUIT.contains(vector.x, vector.y)) {
                this.game.setScreen(new MainMenuScreen(this.game));
            }
            // set to the load screen
            else if (this.SAVE.contains(vector.x, vector.y)) {
                LoadScene loadScene = new LoadScene(this.game, true, this);
                ((AreoController) this.features).setView(loadScene);
                ((AreoController) this.features).play();
                this.game.font.getData().setScale(0.4f);
                this.game.setScreen(loadScene);
            }
        }
    }

    /**
     * Handles the control flow of this game when it is drawing(Moving || Rolling).
     */
    private void updateDraw() {
        // sets the state to Running if there is no animation to draw
        if (this.intermediateSteps.isEmpty() && this.rollingDice.isEmpty()) {
            // checks whether the game has ended
            this.features.isEndGame();
            this.state = GAME_RUNNING;
        } else if (Gdx.input.isTouched()) {
            this.vector = this.game.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            // pause the game
            if (this.PAUSE.contains(vector.x, vector.y)) {
                this.state = GAME_PAUSE;
            }
            // quit the game
            else if (this.QUIT.contains(vector.x, vector.y)) {
                this.game.setScreen(new MainMenuScreen(this.game));
            }
        }
    }

    /**
     * Draws the elements of this screen.
     *
     * @param delta timer
     */
    private void draw(float delta) {
        GL20 gl = Gdx.gl;
        gl.glClearColor(0, 0, 1, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.game.batch.begin();
        // Background img
        this.game.batch.draw(Assets.board, 0, 0, AreoPlaneGame.WORLD_WIDTH, AreoPlaneGame.WORLD_HEIGHT);
        // Text
        this.drawText();
        // quit button
        this.game.batch.draw(Assets.quit, quitX, quitY, 20, 5);
        // dice back background
        this.game.batch.draw(Assets.dice0, diceX, diceY, 20, 20);
        // handles what to draw according to the state of the game
        switch (this.state) {
            case GAME_MOVING:
                this.drawMoving(delta);
                break;
            case GAME_ROLLING:
                this.drawRolling(delta);
                break;
            case GAME_RUNNING:
                this.drawRunning();
                break;
            default:
                this.drawPause();
        }
        this.game.batch.end();
    }

    /**
     * Draws the player in turn, the count down timer, and the message.
     */
    private void drawText() {
        this.game.font.draw(this.game.batch, "PLAYER:", diceX - 5, diceY - 10);
        this.game.font.draw(this.game.batch, this.player, diceX + 13, diceY - 10);
        this.game.font.draw(this.game.batch, "TIME:", diceX - 5, diceY - 20);
        this.game.font.draw(this.game.batch, String.format("%d", (long) this.update_timer), diceX + 10, diceY - 20);
        this.game.font.draw(this.game.batch, this.message, diceX - 5, diceY - 30);
    }

    /**
     * Draws the elements of this game when it is paused.
     * -Save Button
     * -Quit Button
     * -The current positions of the planes
     */
    private void drawPause() {
        this.game.batch.draw(Assets.activeResume, resumeX, resumeY, 20, 10);
        this.game.batch.draw(Assets.save, saveX, saveY, 20, 5);
        this.drawPlanes(this.planes);
    }

    /**
     * Draws the elements of this game when it is running.
     * -Pause Button
     * -Dice Rolled
     * -The current positions of the planes
     */
    private void drawRunning() {
        this.game.batch.draw(Assets.activePause, pauseX, pauseY, 20, 10);
        this.game.batch.draw(dices[this.dice - 1], diceX, diceY, 20, 20);
        this.drawPlanes(this.planes);
    }

    /**
     * Draws the elements of this game when it is moving. It draws one intermediate steps per 0.4 second.
     * -The {@code intermediatePlayer}'s {@code intermediateSteps}
     *
     * @param delta timer
     */
    private void drawMoving(float delta) {
        this.game.batch.draw(Assets.activePause, pauseX, pauseY, 20, 10);
        if (!this.intermediateSteps.isEmpty()) {
            this.game.batch.draw(dices[this.dice - 1], diceX, diceY, 20, 20);
            this.drawPlanes(this.prev_planes);
            this.draw_timer += delta;
            if (this.draw_timer >= 0.4) {
                Vector2 vec = this.coordinates.get(this.intermediateSteps.poll());
                this.game.batch.draw(textures[this.intermediatePlayer], vec.x, vec.y, 6, 6);
                this.draw_timer = 0;
            }
        }
    }

    /**
     * Draws the elements of this game when it is moving. It draws one intermediate dice per 0.1 second.
     * -The {@code rollingDice}
     *
     * @param delta timer
     */
    private void drawRolling(float delta) {
        this.game.batch.draw(Assets.activePause, pauseX, pauseY, 20, 10);
        if (!this.rollingDice.isEmpty()) {
            this.drawPlanes(this.planes);
            this.draw_timer += delta;
            if (this.draw_timer >= 0.1) {
                this.game.batch.draw(dices[this.rollingDice.pop()], diceX, diceY, 20, 20);
                this.draw_timer = 0;
            }
        }
    }

    /**
     * This method is called consistently to draw the board and handles the game flow/user interaction.
     *
     * @param delta timer
     */
    @Override
    public void render(float delta) {
        this.update(delta);
        this.draw(delta);
    }

    /**
     * Sets the screen to {@code WinScene}.
     *
     * @param name of the winner
     */
    public void setWinner(String name) {
        this.game.setScreen(new WinScene(this.game, name));
    }


    /**
     * Mapping the index of each tile to its bottom-left coordinate.
     *
     * @return a {@code Map}
     */
    private Map<Integer, Vector2> coordinate() {
        Map<Integer, Vector2> coordinate = new HashMap<>();
        for (int y = 0; y < 4; y++) {
            coordinate.put(1 + y, new Vector2(26, 1 + y * 7));
            coordinate.put(8 - y, new Vector2(1 + y * 7, 26));
            coordinate.put(14 + y, new Vector2(1 + y * 7, 68));
            coordinate.put(18 + y, new Vector2(26, 72 + y * 7));
            coordinate.put(30 - y, new Vector2(68, 72 + y * 7));
            coordinate.put(31 + y, new Vector2(72 + y * 7, 68));
            coordinate.put(47 - y, new Vector2(68, 1 + y * 7));
            coordinate.put(43 - y, new Vector2(72 + y * 7, 26));
        }
        for (int y = 0; y < 5; y++) {
            coordinate.put(9 + y, new Vector2(1, 33 + y * 7));
            coordinate.put(22 + y, new Vector2(33 + y * 7, 93));
            coordinate.put(39 - y, new Vector2(93, 33 + y * 7));
            coordinate.put(52 - y, new Vector2(33 + y * 7, 1));
        }
        for (int y = 0; y < 6; y++) {
            coordinate.put(53 + y, new Vector2(8 + y * 7, 47));
            coordinate.put(70 - y, new Vector2(51 + y * 7, 47));
            coordinate.put(71 + y, new Vector2(47, 8 + y * 7));
            coordinate.put(64 - y, new Vector2(47, 51 + y * 7));
        }
        coordinate.put(77, new Vector2(2, 2));
        // RED PORT PATH
        coordinate.put(78, new Vector2(18, 2));

        coordinate.put(79, new Vector2(2, 84));
        // YELLOW PORT PATH
        coordinate.put(80, new Vector2(2, 76));

        coordinate.put(81, new Vector2(84, 84));
        // BLUE PORT PATH
        coordinate.put(82, new Vector2(76, 92));

        coordinate.put(83, new Vector2(84, 2));
//         GREEN PORT PATH
        coordinate.put(84, new Vector2(92, 18));
        return coordinate;
    }

    /**
     * Selects and moves the plane with the coordinate of {@code x} and {@code y} .
     *
     * @param x coordinate
     * @param y coordinate
     */
    private void selectPlane(float x, float y) {
        if (x < 33 && y < 33) {
            this.selectPath(1, 8, x, y);
            this.seletPort(x, y, 2, 2, 18, 2, 77, 78);
        } else if (x < 33 && y > 68) {
            this.selectPath(14, 21, x, y);
            this.seletPort(x, y, 2, 84, 2, 76, 79, 80);
        } else if (x > 68 && y > 68) {
            this.selectPath(27, 34, x, y);
            this.seletPort(x, y, 84, 84, 76, 92, 81, 82);
        } else if (x > 68 && y < 33) {
            this.selectPath(40, 47, x, y);
            this.seletPort(x, y, 84, 2, 92, 18, 83, 84);
        } else if (x < 7 && y < 68 && y > 26) {
            this.selectPath(9, 13, x, y);
        } else if (x > 93 && y < 68 && y > 26) {
            this.selectPath(35, 39, x, y);
        } else if (x > 33 && x < 68 && y > 93) {
            this.selectPath(22, 26, x, y);
        } else if (x > 33 && x < 68 && y < 8) {
            this.selectPath(48, 52, x, y);
        } else if (x > 8 && x < 49 && y > 47 && y < 53) {
            this.selectPath(53, 58, x, y);
        } else if (x > 51 && x < 92 && y > 47 && y < 53) {
            this.selectPath(65, 70, x, y);
        } else if (x > 47 && x < 53 && y > 51 && y < 92) {
            this.selectPath(59, 64, x, y);
        } else if (x > 47 && x < 53 && y > 8 && y < 49) {
            this.selectPath(71, 76, x, y);
        }
    }

    private void selectPath(int min, int max, float x, float y) {
        for (int i = min; i <= max; i++) {
            Vector2 vec = this.coordinates.get(i);
            if ((new Rectangle(vec.x, vec.y, 6, 6)).contains(new Vector2(x, y))) {
                this.features.playMove(i);
            }
        }
    }

    private void seletPort(float x, float y, float baseX, float baseY, float pathX, float pathY, int base, int path) {
        if (new Rectangle(baseX, baseY, 14, 14).contains(x, y)) {
            this.features.playMove(base);
        } else if (new Rectangle(pathX, pathY, 6, 6).contains(x, y)) {
            this.features.playMove(path);
        }
    }
}
