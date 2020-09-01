package com.areoplane.game.Controller;

import com.areoplane.game.FileHandler.AreoFileHandler;
import com.areoplane.game.Strategy.*;
import com.areoplane.game.View.AirView;
import com.areoplane.game.Model.AreoModel;
import com.areoplane.game.Model.Player;

import java.util.List;
import java.util.Objects;

/**
 * This class represents a single player Areoplane chess controller. This controller allows the client to play as red
 * planes. It allows the client to the roll dice, move the planes, load a game data, delete a game data,
 * write a game data. It also instruct the {@code AirView} what to display.
 */
public class singlePlayerController implements AreoController, Features {
    // the bot
    private final AiStrategy bot;
    // the model
    private AreoModel model;
    // the view
    private AirView view;
    // the file handler.
    private final AreoFileHandler file;
    // To keep track whether the current player is human player.
    private int humanPlayer = 1;
    // The current {@code Player} in turn.
    private Player curPlayer;
    // A boolean to keep track whether the current player has executed a move.
    private boolean isMoved;
    // The dice rolled for the current player, {@code null} if the player has rolled.
    private Integer roll;
    private boolean isRolled = false;

    /**
     * Constructs a {@code singlePlayerController} with {@code model} and links it to the {@code file}.
     *
     * @param model the {@code AreoModel}
     * @param file  the name of the file
     * @throws NullPointerException if {@code model} or {@code file} is null.
     */
    public singlePlayerController(AreoModel model, String file) {
        Objects.requireNonNull(model);
        Objects.requireNonNull(file);
        this.bot = new AiStrategy(new InnerPathStrategy(), new PutOnPathStrategy(),
                new EscapeStrategy(), new CrashStrategy(), new JumpStrategy(),
                new ReachEndStrategy(), new RandomStrategy());
        this.roll = null;
        this.model = model;
        this.file = new AreoFileHandler(file);
        this.isMoved = false;
    }

    @Override
    public void play() {
        this.view.setFeatures(this);
        this.view.setBoard(this.model.getBoard());
    }

    @Override
    public void setView(AirView view) {
        Objects.requireNonNull(view);
        this.view = view;
    }

    @Override
    public void instruction(int time) {
        // get the current {@code Player} in turn.
        this.curPlayer = this.model.getTurn();
        // tell the {@code view} to display the name of the current {@code Player}.
        this.view.setTurn(this.curPlayer.getName());
        if (this.isHumanPlayer()) {
            //
            this.humanInstruction(time);
        } else {
            this.botInstruction(time);
        }
    }

    /**
     * Checks whether the current player in turn is the human player.
     *
     * @return true if the human player is in turn, else false.
     */
    private boolean isHumanPlayer() {
        return this.humanPlayer == 1;
    }


    /**
     * Handles the control flow(roll dice -> select the plane to be moved) of the human player
     * at the given {@code time}, and instructs the {@code view} what to display.
     *
     * @param time the count down timer
     */
    private void humanInstruction(int time) {
        if (time > 5) {
            this.setNextPlayer();
            return;
        }
        if (this.roll == null) {
            this.view.setMessage("Roll Dice");
        } else {
            if (!isRolled) {
                this.view.setDice(this.roll);
                this.view.setMessage("Rolling..");
                this.isRolled = true;
            } else {
                if (model.movablePlanes(this.curPlayer, this.roll).size() == 0) {
                    this.view.setMessage("Not m ov able");
                } else if (!isMoved) {
                    this.view.setMessage("Select Plane");
                }
            }
        }
    }

    /**
     * Handles the control flow(roll dice -> select the plane to be moved) of the bot player
     * at the given {@code time}, and instructs the {@code view} what to display.
     *
     * @param time the count down timer
     */
    private void botInstruction(int time) {
        this.view.setTurn(this.model.getTurn().getName());
        if (time > 3) {
            this.setNextPlayer();
            return;
        }
        if (this.roll == null) {
            this.view.setMessage("Roll Dice");
            this.roll = this.model.rollDice();
        } else {
            if (!isRolled) {
                this.view.setDice(this.roll);
                this.view.setMessage("Rolling..");
                this.isRolled = true;
            } else if (!isMoved) {
                Integer move = this.bot.choosePlane(this.roll, this.model);
                if (move == null) {
                    this.view.setMessage("Not m ov able");
                } else {
                    this.view.setMessage("Moved Plane " + move);
                    this.view.setIntermediate(this.humanPlayer, this.model.intermediatePositions(this.curPlayer,
                            move, this.roll));
                    this.model.move(this.curPlayer, move, this.roll);
                    this.view.setBoard(this.model.getBoard());
                    this.isMoved = true;
                }
            }
        }
    }

    /**
     * Sets the next player in turn.
     * Resets the {@code roll}, {@code isMoved}, {@code isRolled}.
     * Resets the {@code view}'s timer to 1.
     * Updates the {@code humanPlayer}.
     */
    private void setNextPlayer() {
        if (this.roll == null) {
            this.model.nextPlayer(1);
        } else {
            this.model.nextPlayer(roll);
        }
        this.roll = null;
        this.isMoved = false;
        this.isRolled = false;
        this.updateTacker();
        this.view.setTime(1);
    }

    /**
     * Updates the {@code humanPlayer}.
     */
    private void updateTacker() {
        if (!this.curPlayer.equals(this.model.getTurn())) {
            this.humanPlayer = (this.humanPlayer + 1 > 4) ? 1 : this.humanPlayer + 1;
        }
    }

    @Override
    public void rollDice() {
        if (roll == null) {
            this.roll = this.model.rollDice();
        }
    }

    @Override
    public void playMove(int position) {
        if (!this.isMoved && this.roll != null) {
            int[] pos = this.model.positions(this.curPlayer);
            List<Integer> movable = this.model.movablePlanes(this.curPlayer, this.roll);
            for (int i : movable) {
                if (pos[i] == position) {
                    this.view.setIntermediate(this.humanPlayer,
                            this.model.intermediatePositions(this.curPlayer, i, this.roll));
                    this.model.move(this.curPlayer, i, this.roll);
                    this.view.setMessage("Moved Plane " + i);
                    this.view.setBoard(this.model.getBoard());
                    this.isMoved = true;
                    return;
                }
            }
        }
    }

    @Override
    public void isEndGame() {
        if (this.model.getWinner() != null) {
            this.view.setWinner(this.model.getWinner().getName());
        }
    }


    @Override
    public void saveData(int slot) {
        this.file.writeToData(this.model, slot);
        this.file.writeToFile();
    }

    @Override
    public void deleteData(int slot) {
        this.file.deleteFromData(slot);
        this.file.writeToFile();
    }

    @Override
    public void loadGame(int slot) {
        AreoModel model = this.file.loadModel(slot, this.model.getBuilder(), this.model.getPlayers()[0].getBuilder());
        if (model != null) {
            this.model = model;
        } else {
            throw new NullPointerException("Failed to load the model");
        }
    }

    @Override
    public void loadData() {
        this.view.setBoard(this.file.getData());
    }
}
