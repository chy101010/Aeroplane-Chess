package com.areoplane.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {
    public static Texture mainMenu;
    public static Texture board;
    public static Texture win;
    public static Texture red;
    public static Texture yellow;
    public static Texture blue;
    public static Texture green;
    public static Texture dice1;
    public static Texture dice2;
    public static Texture dice3;
    public static Texture dice4;
    public static Texture dice5;
    public static Texture dice6;

    private static final Texture buttons = loadTexture("buttons.png");;
    public static TextureRegion activePause;
    public static TextureRegion inActivePause;
    public static TextureRegion activeResume;
    public static TextureRegion inActiveResume;
    public static TextureRegion quit;



    public static Texture loadTexture(String file) {
        return new Texture(Gdx.files.internal(file));

    }

    public static void load() {
        win = loadTexture("win.png");
        board = loadTexture("board.png");
        mainMenu = loadTexture("main_menu.png");
        red = loadTexture("red_plane.png");
        yellow = loadTexture("yellow_plane.png");
        blue = loadTexture("blue_plane.png");
        green = loadTexture("green_plane.png");
        dice1 = loadTexture("dice_1.png");
        dice2 = loadTexture("dice_2.png");
        dice3 = loadTexture("dice_3.png");
        dice4 = loadTexture("dice_4.png");
        dice5 = loadTexture("dice_5.png");
        dice6 = loadTexture("dice_6.png");
        activePause = new TextureRegion(buttons, 10,0,300,100);
        inActivePause = new TextureRegion(buttons, 310,0,300,100);
        activeResume = new TextureRegion(buttons, 10,110,361,100);
        inActiveResume = new TextureRegion(buttons, 369,110,361,100);
        quit = new TextureRegion(buttons, 10,210,256,100);
    }


}
