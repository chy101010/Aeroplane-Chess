package com.areoplane.game.Screens;

import com.areoplane.game.Controller.AreoController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.areoplane.game.Assets;
import com.areoplane.game.Controller.Features;
import com.areoplane.game.AreoPlaneGame;
import com.areoplane.game.View.AirView;

import java.util.*;

public class GameScene extends ScreenAdapter implements AirView {
    private final AreoPlaneGame game;
    private Features features;
    private final BitmapFont font;

    // Game State
    private final int GAME_RUNNING = 0;
    private final int GAME_PAUSE = 1;
    private final int GAME_MOVING = 2;
    private final int GAME_ROLLING = 3;
    private int state;

    // Data to be drawn
    private Queue<Integer> intermediateSteps = new LinkedList<>();
    private final Stack<Integer> rollingDice = new Stack();
    private int[][] planes;
    private int[][] prev_planes;

    // AirPlanes
    private int intermediatePlayer = -1;

    // Text
    private String player;

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

    // String
    private String message;

    // Textures
    private final Texture[] dices = new Texture[]{Assets.dice1, Assets.dice2, Assets.dice3, Assets.dice4, Assets.dice5, Assets.dice6};
    private final Texture[] textures = new Texture[]{Assets.red, Assets.yellow, Assets.blue, Assets.green};

    // Positions
    private Vector3 vector;
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

    // Click Points
    private final Rectangle DICE = new Rectangle(this.diceX, this.diceY, 20, 20);
    private final Rectangle PAUSE = new Rectangle(this.pauseX, this.pauseY, 20, 10);
    private final Rectangle RESUME = new Rectangle(this.resumeX, this.resumeY, 20, 10);
    private final Rectangle SAVE = new Rectangle(this.saveX, this.saveY, 20,5);
    private final Rectangle QUIT = new Rectangle(this.quitX, this.quitY, 20, 5);


    public GameScene(AreoPlaneGame game) {
        this.game = game;
        this.vector = new Vector3();
        this.coordinates = this.coordinate();
        this.font = new BitmapFont();
        this.font.getData().setScale(0.3f, 0.3f);
        this.state = this.GAME_RUNNING;
    }

    @Override
    public void setFeatures(Features features) {
        Objects.requireNonNull(features);
        this.features = features;
    }

    @Override
    public void setDice(int roll) {
        System.out.print("Set Dice");
        if (roll < 1 || roll > 6) {
            throw new IllegalArgumentException("The given roll can't be less than 1 or greater than 6");
        }
        this.dice = roll;
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


    private void update(float delta) {
        switch (this.state) {
            case GAME_RUNNING:
                this.features.isEndGame();
                this.update_timer += delta;
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

    private void updateRunning() {
        if (!this.intermediateSteps.isEmpty()) {
            this.state = GAME_MOVING;
        } else if (!this.rollingDice.isEmpty()) {
            this.state = GAME_ROLLING;
        } else if (Gdx.input.isTouched()) {
            this.vector = this.game.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            this.selectPlane(vector.x, vector.y);
            if (this.DICE.contains(vector.x, vector.y)) {
                this.features.rollDice();
            } else if (this.PAUSE.contains(vector.x, vector.y)) {
                this.state = GAME_PAUSE;
            } else if (this.QUIT.contains(vector.x, vector.y)) {
                this.game.setScreen(new MainMenuScreen(this.game));
            }
        }
    }

    private void updatePause() {
        if (Gdx.input.isTouched()) {
            this.vector = this.game.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if (this.RESUME.contains(vector.x, vector.y)) {
                if (!this.intermediateSteps.isEmpty()) {
                    this.state = GAME_MOVING;
                } else if (!this.rollingDice.isEmpty()) {
                    this.state = GAME_ROLLING;
                } else {
                    this.state = GAME_RUNNING;
                }
            } else if (this.QUIT.contains(vector.x, vector.y)) {
                this.game.setScreen(new MainMenuScreen(this.game));
            } else if (this.SAVE.contains(vector.x, vector.y)) {
                LoadScene loadScene = new LoadScene(this.game, true, this);
                ((AreoController) this.features).setView(loadScene);
                ((AreoController) this.features).play();
                this.game.setScreen(loadScene);
            }
        }
    }

    private void updateDraw() {
        if (this.intermediateSteps.isEmpty() && this.rollingDice.isEmpty()) {
            this.state = GAME_RUNNING;
        }
        if (Gdx.input.isTouched()) {
            this.vector = this.game.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if (this.PAUSE.contains(vector.x, vector.y)) {
                this.state = GAME_PAUSE;
            } else if (this.QUIT.contains(vector.x, vector.y)) {
                this.game.setScreen(new MainMenuScreen(this.game));
            }
        }
    }

    private void draw(float delta) {
        GL20 gl = Gdx.gl;
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.game.batch.begin();
        this.game.batch.draw(Assets.board, 0, 0, AreoPlaneGame.WORLD_WIDTH, AreoPlaneGame.WORLD_HEIGHT);
        this.font.draw(this.game.batch, "PLAYER:", diceX - 5, diceY - 10);
        this.font.draw(this.game.batch, this.player, diceX + 13, diceY - 10);
        this.font.draw(this.game.batch, "TIME:", diceX - 5, diceY - 20);
        this.font.draw(this.game.batch, String.format("%d", (long) this.update_timer), diceX + 10, diceY - 20);
        this.font.draw(this.game.batch, this.message, diceX - 5, diceY - 30);
        this.game.batch.draw(Assets.quit, quitX, quitY, 20, 5);
        this.game.batch.draw(Assets.dice0, diceX, diceY, 20, 20);
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

    private void drawPause() {
        this.game.batch.draw(Assets.activeResume, resumeX, resumeY, 20, 10);
        this.game.batch.draw(Assets.save, saveX, saveY, 20, 5);
        this.drawPlanes(this.planes);
    }

    private void drawRunning() {
        this.game.batch.draw(Assets.activePause, pauseX, pauseY, 20, 10);
        this.game.batch.draw(dices[this.dice - 1], diceX, diceY, 20, 20);
        this.drawPlanes(this.planes);
    }

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

    @Override
    public void render(float delta) {
        this.update(delta);
        this.draw(delta);
    }

    public void setWinner(String name) {
        this.game.setScreen(new WinScene(this.game, name));
    }


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

    private void selectPlane(float x, float y) {
        if (x < 33 && y < 33) {
            this.helper(1, 8, x, y);
            this.helper1(x, y, 2, 2, 18, 2, 77, 78);
        } else if (x < 33 && y > 68) {
            this.helper(14, 21, x, y);
            this.helper1(x, y, 2, 84, 2, 76, 79, 80);
        } else if (x > 68 && y > 68) {
            this.helper(27, 34, x, y);
            this.helper1(x, y, 84, 84, 76, 92, 81, 82);
        } else if (x > 68 && y < 33) {
            this.helper(40, 47, x, y);
            this.helper1(x, y, 84, 2, 92, 18, 83, 84);
        } else if (x < 7 && y < 68 && y > 26) {
            this.helper(9, 13, x, y);
        } else if (x > 93 && y < 68 && y > 26) {
            this.helper(35, 39, x, y);
        } else if (x > 33 && x < 68 && y > 93) {
            this.helper(22, 26, x, y);
        } else if (x > 33 && x < 68 && y < 8) {
            this.helper(48, 52, x, y);
        } else if (x > 8 && x < 49 && y > 47 && y < 53) {
            this.helper(53, 58, x, y);
        } else if (x > 51 && x < 92 && y > 47 && y < 53) {
            this.helper(65, 70, x, y);
        } else if (x > 47 && x < 53 && y > 51 && y < 92) {
            this.helper(59, 64, x, y);
        } else if (x > 47 && x < 53 && y > 8 && y < 49) {
            this.helper(71, 76, x, y);
        }
    }

    private void helper(int min, int max, float x, float y) {
        for (int i = min; i <= max; i++) {
            Vector2 vec = this.coordinates.get(i);
            if ((new Rectangle(vec.x, vec.y, 6, 6)).contains(new Vector2(x, y))) {
                this.features.playMove(i);
            }
        }
    }

    private void helper1(float x, float y, float baseX, float baseY, float pathX, float pathY, int base, int path) {
        if (new Rectangle(baseX, baseY, 14, 14).contains(x, y)) {
            this.features.playMove(base);
        } else if (new Rectangle(pathX, pathY, 6, 6).contains(x, y)) {
            this.features.playMove(path);
        }
    }
}
