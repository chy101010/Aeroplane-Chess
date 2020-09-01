package com.areoplane.game.FileHandler;

import com.areoplane.game.Model.AreoBoardBuilder;
import com.areoplane.game.Model.AreoModel;
import com.areoplane.game.Model.Player;
import com.areoplane.game.Model.PlayerBuilder;

import java.io.*;
import java.util.*;

/**
 * A helper class that can read from a file and temporarily store the its data. A client can write into its
 * temporary data, or delete. Then the client can write the temporary data into a file. A client can construct a
 * {@code AreoModel} from its temporary data.
 */
public class AreoFileHandler {
    private Scanner scanner;
    // a temporary storage of the data in the String form
    // that this data will be used to write into the file
    private StringBuilder builder;
    // name of the file - "name.txt" to be read and write into
    private final String file;
    // a temporary storage of the data in the Integer array form
    private final Integer[][] data = new Integer[4][16];

    /**
     * Constructs an AreoFileHandler, this is constructor should be used to test data in the String form.
     *
     * @param file    name of the file
     * @param builder a StringBuilder
     */
    public AreoFileHandler(String file, StringBuilder builder) {
        this.file = file;
        try {
            File f = new File(file);
            this.builder = builder;
            if (!f.createNewFile()) {
                this.scanner = new Scanner(f);
                this.preProcess();
            } else {
                this.scanner = new Scanner(f);
            }
        } catch (IOException ioe) {
            System.out.print(ioe.getMessage());
        }
    }

    /**
     * A convenient constructor.
     *
     * @param file name of the file
     */
    public AreoFileHandler(String file) {
        this(file, new StringBuilder());
    }


    /**
     * Writes the game data of {@code model} into the {@code slot} of {@code data}. If there  already a game data in
     * the {@code slot} of {@code data}, then do nothing.
     *
     * @param model an {@code AreoModel}
     * @param slot  the index of the Integer array data to write into
     * @throws IllegalArgumentException if the given {@code slot} is less than 0 of greater than 3.
     * @throws NullPointerException if the given {@code model} is null
     */
    public void writeToData(AreoModel model, int slot) {
        if (slot < 0 || slot > 3) {
            throw new IllegalArgumentException("Select a slot in between 0 - 3");
        }
        Objects.requireNonNull(model);
        if (this.data[slot][0] == null) {
            Player[] players = model.getPlayers();
            Integer[] game = new Integer[16];
            int index = 0;
            for (Player player : players) {
                int[] positions = player.getPositions();
                for (int pos : positions) {
                    game[index] = pos;
                    index++;
                }
            }
            this.data[slot] = game;
        }
    }

    /**
     * Deletes the game data in the {@code slot} of {@code data}.
     *
     * @param slot the index of the Integer array data to be deleted
     * @throws IllegalArgumentException if {@code slot} is less than 0 or greater than 3.
     */
    public void deleteFromData(int slot) {
        if (slot < 0 || slot > 3) {
            throw new IllegalArgumentException("Select a slot in between 0 - 3");
        }
        if (this.data[slot][0] != null) {
            this.data[slot] = new Integer[16];
        }
    }


    /**
     * Returns a {@code AreoModel} using the game data in the slot of {@code data}.
     *
     * @param slot     the index of the Integer array data of be read from
     * @param mBuilder a builder for {@code AreoModel}
     * @param pBuilder a builder for {@code Player}
     * @return the AreoModel
     * @throws IllegalArgumentException if {@code slot} is less than 0 or greater than 3.
     */
    public AreoModel loadModel(int slot, AreoBoardBuilder mBuilder, PlayerBuilder pBuilder) {
        if (slot < 0 || slot > 3) {
            throw new IllegalArgumentException("Select a  slot in between 0 - 3");
        }
        if (this.data[slot][0] != null) {
            String[] str = new String[]{"Red", "Yellow", "Blue", "Green"};
            int name = 0;
            for (int i = 1; i <= 16; i++) {
                if (i % 4 == 0) {
                    pBuilder.setName(str[name]);
                    name++;
                    pBuilder.addPlanes(data[slot][i - 1]);
                    mBuilder.addPlayer(pBuilder.build());
                } else {
                    pBuilder.addPlanes(data[slot][i - 1]);
                }
            }
            return mBuilder.build();
        }
        return null;
    }

    /**
     * Appends the string data in {@code builder} into the text file named {@code file}.
     */
    public void writeToFile() {
        try {
            this.updateStringData();
            new BufferedWriter(new FileWriter(this.file)).append(this.builder.toString()).close();
        } catch (IOException ioe) {
            System.out.print(ioe.getMessage());
        }
    }

    /**
     * Returns a copy of the temporary storage of the game data in the 2D Integer array form. Each row holds the game
     * data for a game. If the first value in a row is {@code Integer.MIN_VALUE}, the that row doesn't have a game data.
     *
     * @return a copy of the game data
     */
    public int[][] getData() {
        int[][] data = new int[4][16];
        for (int i = 0; i < 4; i++) {
            int[] game = new int[16];
            if (this.data[i][0] != null) {
                for (int j = 0; j < 16; j++) {
                    game[j] = this.data[i][j];
                }
            } else {
                game[0] = Integer.MIN_VALUE;
            }
            data[i] = game;
        }
        return data;
    }

    /**
     * Updates the data in the String form using the data in the Integer array form.
     */
    private void updateStringData() {
        // clears the String data
        this.builder.setLength(0);
        // append into the String data
        for (int i = 0; i < 4; i++) {
            if (this.data[i][0] != null) {
                Integer[] game = this.data[i];
                for (int j = 0; j < 16; j++) {
                    this.builder.append(game[j]).append(j != 15 ? " " : "");
                }
                this.builder.append("\n");
            }
        }
    }


    /**
     * Loads the game data from the file into the {@code data} and {@code builder}.
     */
    private void preProcess() {
        int index = 0;
        while (this.scanner.hasNextLine() && index < 4) {
            String line = this.scanner.nextLine();
            String[] parse = line.split(" ");
            try {
                if (parse.length == 16) {
                    Integer[] game = new Integer[16];
                    for (int i = 0; i < 16; i++) {
                        game[i] = Integer.parseInt(parse[i]);
                    }
                    this.data[index] = game;
                    index++;
                    this.builder.append(line).append("\n");
                }
            } catch (NumberFormatException ieo) {
                System.out.print(ieo.getMessage());
            }
        }
    }
}
