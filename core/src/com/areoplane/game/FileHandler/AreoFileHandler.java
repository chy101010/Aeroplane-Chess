package com.areoplane.game.FileHandler;

import com.areoplane.game.Model.AreoBoardBuilder;
import com.areoplane.game.Model.AreoModel;
import com.areoplane.game.Model.Player;
import com.areoplane.game.Model.PlayerBuilder;

import java.io.*;
import java.util.*;

/**
 * A helper to read Areo-Model data and construct an Areo-Model from it, and to append or delete Areo-Model data.
 */
public class AreoFileHandler {
    private Scanner scanner;
    private StringBuilder builder;
    private final String name;
    private final Integer[][] data = new Integer[4][16];


    public AreoFileHandler(String file, StringBuilder builder) {
        this.name = file;
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

    public AreoFileHandler(String file) {
        this(file, new StringBuilder());
    }


    public void writeToData(AreoModel model, int slot) {
        if (slot < 0 || slot > 3) {
            throw new IllegalArgumentException("Select a slot in between 0 - 3");
        }
        if (model == null) {
            throw new IllegalArgumentException("The model to be saved can not be null");
        }
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
            this.fixOrder();
            this.updateFile();
        }
    }

    public void deleteFromData(int slot) {
        if (slot < 0 || slot > 3) {
            throw new IllegalArgumentException("Select a slot in between 0 - 3");
        }
        if (this.data[slot][0] != null) {
            this.data[slot] = new Integer[16];
            this.fixOrder();
            this.updateFile();
        }
    }


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

    public void writeToFile() {
        try {
            new BufferedWriter(new FileWriter(this.name)).append(this.builder.toString()).close();
        } catch (IOException ioe) {
            System.out.print(ioe.getMessage());
        }
    }

    public int[][] getData() {
        List<int[]> data = new ArrayList<>();
        int size = 0;
        for (int i = 0; i < 4; i++) {
            if (this.data[i][0] != null) {
                int[] game = new int[16];
                for (int j = 0; j < 16; j++) {
                    game[j] = this.data[i][j];
                }
                data.add(game);
                size++;
            }
        }
        if (size > 0) {
            int[][] res = new int[size][16];
            for (int i = 0; i < data.size(); i++) {
                res[i] = data.get(i);
            }
            return res;
        }
        return null;
    }


    private void fixOrder() {
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < 4; i++) {
            if (this.data[i][0] == null) {
                stack.push(i);
            } else {
                if (!stack.isEmpty()) {
                    this.data[stack.pop()] = data[i];
                    this.data[i] = new Integer[16];
                }
            }
        }
    }

    private void updateFile() {
        this.builder.setLength(0);
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
     * Stores the layout into the {@code data} and {@code appendable}
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
