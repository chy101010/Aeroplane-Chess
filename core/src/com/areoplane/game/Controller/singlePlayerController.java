package com.areoplane.game.Controller;

import com.areoplane.game.FileHandler.AreoFileHandler;
import com.areoplane.game.Model.AreoBoardImpl;
import com.areoplane.game.Model.PlayerImpl;
import com.areoplane.game.Strategy.*;
import com.areoplane.game.View.AirView;
import com.areoplane.game.Model.AreoModel;
import com.areoplane.game.Model.Player;

import java.util.List;
import java.util.Objects;

public class singlePlayerController implements AreoController, Features {
    private final AiStrategy bot;
    private AreoModel model;
    private AirView view;

    // To keep track whether the current player is human player.
    private int humanPlayer = 1;

    // A boolean to keep track whether the current player has executed a move.
    private boolean isMoved;

    // The dice rolled for the current player, {@code null} if the player has rolled
    private Integer roll;
    private boolean isRolled = false;

    //
    private Player curPlayer;

    // File Handler
    private final AreoFileHandler file;

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
        this.curPlayer = this.model.getTurn();
        this.view.setTurn(this.curPlayer.getName());
        if (this.isHumanPlayer()) {
            this.playerInstruction(time);
        } else {
            this.botInstruction(time);
        }
//        System.out.print("Controller");
    }

    private boolean isHumanPlayer() {
        return this.humanPlayer == 1;
    }


    private void playerInstruction(int time) {
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
                    this.view.setIntermediate(this.humanPlayer, this.model.intermediatePositions(this.curPlayer, move, this.roll));
                    this.model.move(this.curPlayer, move, this.roll);
                    this.view.setBoard(this.model.getBoard());
                    this.isMoved = true;
                }
            }
        }
    }

    private void setNextPlayer() {
        if (this.roll == null) {
            this.model.nextPlayer(1);
        } else {
            this.model.nextPlayer(roll);
        }
        this.roll = null;
        this.isMoved = false;
        this.isRolled = false;
        this.setHumanPlayer();
        this.view.setTime(1);
    }

    private void setHumanPlayer() {
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

    // todo hard coded builder
    @Override
    public void loadGame(int slot) {
        AreoModel model = this.file.loadModel(slot, new AreoBoardImpl.Builder(), new PlayerImpl.Builder());
        if (model != null) {
            this.model = model;
        }
    }

    @Override
    public void loadData() {
        this.view.setBoard(this.file.getData());
    }
}
