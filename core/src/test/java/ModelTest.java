import com.areoplane.game.Model.AreoBoardImpl;
import com.areoplane.game.Model.AreoModel;
import com.areoplane.game.Model.Player;
import com.areoplane.game.Model.PlayerImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class ModelTest {
    private final Random rand = new Random();
    private AreoModel model;
    private Player red;
    private Player yellow;
    private Player blue;
    private Player green;

    @Before
    public void testFixture() {
        this.rand.setSeed(1);
        this.red = new PlayerImpl("R");
        this.yellow = new PlayerImpl("Y");
        this.blue = new PlayerImpl("B");
        this.green = new PlayerImpl("G");
        this.model = new AreoBoardImpl(this.red, this.yellow, this.blue, this.green, this.rand);
    }

    //     Random Seed 1
    @Test
    public void testToString() {
        assertEquals("-1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1", this.model.toString());
        this.red.setPath(1);
        this.red.setPath(0);
        this.red.move(1, 2);
        this.red.move(1, 6);
        this.red.move(1, 6);
        this.red.move(1, 6);
        this.red.move(1, 6);
        this.red.move(1, 6);
        this.red.move(0, 6);
        this.red.move(0, 6);
        this.red.move(0, 2);
        this.yellow.setPath(1);
        this.yellow.move(1, 6);
        this.blue.setPath(3);
        this.blue.move(3, 1);
        this.blue.move(3, 3);
        this.blue.setPath(1);
        this.green.setPath(1);
        this.green.move(1, 6);
        this.green.move(1, 4);
        this.green.setPath(0);
        this.green.move(0, 5);
        this.green.move(0, 5);
        assertEquals("34 56 -1 -1 -1 10 -1 -1 -1 0 -1 4 14 30 -1 -1", this.model.toString());
        this.green.move(0, 5);
        this.green.move(0, 3);
        this.green.move(0, 4);
        this.green.move(0, 4);
        this.green.move(0, 4);
        this.green.move(0, 1);
        assertEquals("34 56 -1 -1 -1 10 -1 -1 -1 0 -1 4 51 30 -1 -1", this.model.toString());
        this.yellow.move(1, 4);
        this.yellow.move(1, 4);
        this.yellow.move(1, 4);
        this.yellow.move(1, 6);
        assertEquals("34 56 -1 -1 -1 52 -1 -1 -1 0 -1 4 51 30 -1 -1", this.model.toString());
    }

    @Test
    public void rollDice() {
        assertEquals(4, this.model.rollDice());
        assertEquals(5, this.model.rollDice());
        assertEquals(2, this.model.rollDice());
        assertEquals(4, this.model.rollDice());
        assertEquals(3, this.model.rollDice());
        assertEquals(5, this.model.rollDice());
        assertEquals(3, this.model.rollDice());
        assertEquals(5, this.model.rollDice());
        assertEquals(5, this.model.rollDice());
        assertEquals(5, this.model.rollDice());
        assertEquals(2, this.model.rollDice());
        assertEquals(2, this.model.rollDice());
        assertEquals(2, this.model.rollDice());
        assertEquals(4, this.model.rollDice());
        assertEquals(1, this.model.rollDice());
        assertEquals(5, this.model.rollDice());
        assertEquals(3, this.model.rollDice());
        assertEquals(1, this.model.rollDice());
        assertEquals(1, this.model.rollDice());
        assertEquals(6, this.model.rollDice());
    }

    @Test
    public void testMove() {
        this.model.move(this.red, 0, 5);
        this.model.move(this.yellow, 0, 6);
        this.model.move(this.blue, 0, 6);
        this.model.move(this.green, 0, 5);
        assertEquals(
                "0 -1 -1 -1 0 -1 -1 -1 0 -1 -1 -1 0 -1 -1 -1", this.model.toString());
        this.model.move(this.red, 0, 2);
        this.model.move(this.yellow, 0, 1);
        this.model.move(this.blue, 0, 4);
        this.model.move(this.green, 0, 3);
        assertEquals(
                "6 -1 -1 -1 1 -1 -1 -1 4 -1 -1 -1 3 -1 -1 -1", this.model.toString());
        // Jump Crash
        this.model.move(this.red, 0, 4);
        this.model.move(this.blue, 0, 4);
        this.model.move(this.blue, 1, 5);
        this.model.move(this.blue, 1, 4);
        this.model.move(this.green, 0, 3);
        assertEquals(
                "14 -1 -1 -1 -1 -1 -1 -1 8 4 -1 -1 10 -1 -1 -1", this.model.toString());
        // Jumping from tile 18 that crashes plane at 30 and 34
        // Short Cut Crash Jump Crash
        this.model.move(this.red, 0, 4);
        this.model.move(this.yellow, 0, 5);
        this.model.move(this.yellow, 1, 6);
        this.model.move(this.yellow, 2, 5);
        this.model.move(this.yellow, 3, 6);
        this.model.move(this.green, 0, 3);
        this.model.move(this.green, 1, 5);
        assertEquals(
                "34 -1 -1 -1 0 0 0 0 -1 -1 -1 -1 13 0 -1 -1", this.model.toString());
        this.model.move(this.red, 0, 4);
        this.model.move(this.yellow, 0, 6);
        this.model.move(this.yellow, 1, 6);
        this.model.move(this.yellow, 1, 6);
        this.model.move(this.blue, 0, 5);
        this.model.move(this.blue, 1, 6);
        this.model.move(this.green, 0, 5);
        assertEquals(
                "42 -1 -1 -1 10 16 0 0 0 0 -1 -1 34 0 -1 -1", this.model.toString());
        this.model.move(this.red, 0, 4);
        this.model.move(this.red, 1, 6);
        this.model.move(this.yellow, 0, 5);
        this.model.move(this.yellow, 1, 2);
        // Crash and Jump
        this.model.move(this.blue, 0, 2);
        this.model.move(this.blue, 1, 6);
        this.model.move(this.green, 0, 5);
        assertEquals(
                "50 0 -1 -1 -1 34 0 0 6 10 -1 -1 39 0 -1 -1", this.model.toString());
        this.model.move(this.red, 0, 6);
        this.model.move(this.yellow, 1, 2);
        this.model.move(this.blue, 0, 4);
        this.model.move(this.blue, 1, 2);
        this.model.move(this.green, 0, 6);
        this.model.move(this.green, 1, 2);
        assertEquals(
                "56 0 -1 -1 -1 36 0 0 14 12 -1 -1 45 6 -1 -1", this.model.toString());
        // Crash
        this.model.move(this.yellow, 1, 2);
        this.model.move(this.blue, 0, 1);
        this.model.move(this.green, 0, 6);
        // Crash and Jump
        this.model.move(this.green, 2, 5);
        this.model.move(this.green, 2, 2);
        this.model.move(this.green, 1, 6);
        this.model.move(this.green, 1, 2);
        assertEquals(
                "56 0 -1 -1 -1 42 0 0 -1 12 -1 -1 51 30 6 -1", this.model.toString());
        this.model.move(this.green, 0, 6);
        this.model.move(this.yellow, 1, 4);
        this.model.move(this.blue, 1, 6);
        this.model.move(this.blue, 1, 1);
        assertEquals(
                "56 0 -1 -1 -1 50 0 0 -1 35 -1 -1 55 30 6 -1", this.model.toString());
        this.model.move(this.yellow, 1, 6);
        this.model.move(this.yellow, 3, 2);
        this.model.move(this.green, 0, 1);
        this.model.move(this.green, 1, 6);
        this.model.move(this.blue, 1, 6);
        this.model.move(this.blue, 1, 1);
        assertEquals(
                "56 0 -1 -1 -1 56 0 6 -1 46 -1 -1 56 36 6 -1", this.model.toString());
        this.model.move(this.yellow, 3, 6);
        this.model.move(this.green, 1, 4);
        this.model.move(this.blue, 1, 6);
        assertEquals(
                "56 0 -1 -1 -1 56 0 12 -1 52 -1 -1 56 40 6 -1", this.model.toString());
        // Crash - Jump - Short Cut
        this.model.move(this.yellow, 3, 2);
        this.model.move(this.blue, 1, 5);
        assertEquals(
                "56 0 -1 -1 -1 56 0 30 -1 55 -1 -1 56 -1 6 -1", this.model.toString());
        this.model.move(this.yellow, 2, 5);
        this.model.move(this.blue, 2, 5);
        this.model.move(this.blue, 2, 4);
        assertEquals(
                "56 0 -1 -1 -1 56 5 30 -1 55 4 -1 56 -1 6 -1", this.model.toString());
        // Jump - Crash - Short Cut - Crash
        this.model.move(this.red, 1, 6);
        this.model.move(this.red, 1, 4);
        assertEquals(
                "56 30 -1 -1 -1 56 -1 30 -1 55 -1 -1 56 -1 6 -1", this.model.toString());
    }

    @Test
    public void getTurnSetNextPlayer() {
        assertEquals(this.red, this.model.getTurn());
        this.model.nextPlayer(5);
        assertEquals(this.yellow, this.model.getTurn());
        this.model.nextPlayer(1);
        assertEquals(this.blue, this.model.getTurn());
        this.model.nextPlayer(6);
        assertEquals(this.blue, this.model.getTurn());
        this.model.nextPlayer(1);
        assertEquals(this.green, this.model.getTurn());
        this.model.nextPlayer(4);
        assertEquals(this.red, this.model.getTurn());
        this.model.nextPlayer(4);
        assertEquals(this.yellow, this.model.getTurn());
    }

    @Test
    public void testIsGameOverGetWinner() {
        assertFalse(this.model.isGameOver());
        this.model.move(this.red, 0, 6);
        this.model.move(this.red, 1, 6);
        this.model.move(this.red, 2, 6);
        this.model.move(this.red, 3, 6);
        assertNull(this.model.getWinner());
        this.model.move(this.red, 0, 6);
        this.model.move(this.red, 1, 6);
        this.model.move(this.red, 2, 6);
        this.model.move(this.red, 3, 6);
        this.model.move(this.red, 0, 6);
        this.model.move(this.red, 1, 6);
        this.model.move(this.red, 2, 6);
        assertNull(this.model.getWinner());
        this.model.move(this.red, 3, 6);
        this.model.move(this.red, 0, 6);
        this.model.move(this.red, 1, 6);
        this.model.move(this.red, 2, 6);
        this.model.move(this.red, 3, 6);
        assertArrayEquals(new int[]{26, 26, 26, 26}, this.model.positions(this.red));
        this.model.move(this.red, 0, 6);
        this.model.move(this.red, 1, 6);
        this.model.move(this.red, 2, 6);
        this.model.move(this.red, 3, 6);
        this.model.move(this.red, 0, 6);
        this.model.move(this.red, 1, 6);
        this.model.move(this.red, 2, 6);
        this.model.move(this.red, 3, 6);
        this.model.move(this.red, 0, 6);
        assertNull(this.model.getWinner());
        this.model.move(this.red, 1, 6);
        this.model.move(this.red, 2, 6);
        this.model.move(this.red, 3, 6);
        this.model.move(this.red, 0, 6);
        this.model.move(this.red, 1, 6);
        this.model.move(this.red, 2, 6);
        this.model.move(this.red, 3, 6);
        assertArrayEquals(new int[]{74, 74, 74, 74}, this.model.positions(this.red));
        assertFalse(this.model.isGameOver());
        this.model.move(this.red, 0, 2);
        this.model.move(this.red, 1, 2);
        this.model.move(this.red, 2, 2);
        this.model.move(this.red, 3, 2);
        assertArrayEquals(new int[]{76, 76, 76, 76}, this.model.positions(this.red));
        assertTrue(this.model.isGameOver());
        assertEquals(this.red, this.model.getWinner());
    }


    // Testing invalid move
    @Test(expected = IllegalArgumentException.class)
    public void incorrectPlayer() {
        // Incorrect Player
        this.model.move(new PlayerImpl("Incorrect"), 1, 6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void incorrectPlane() {
        // Incorrect Plane
        this.model.move(this.red, -1, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void incorrectPlane1() {
        // Incorrect Plane
        this.model.move(this.red, 4, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void incorrectRoll() {
        // Incorrect Roll
        this.model.move(this.red, 1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void incorrectRoll1() {
        // Incorrect Roll
        this.model.move(this.red, 1, 7);
    }

    @Test(expected = IllegalArgumentException.class)
    public void incorrectPlayerPlane() {
        // Incorrect Player & roll
        this.model.move(new PlayerImpl("Incorrect"), 1, -6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void incorrectPlayerPlaneIdentity() {
        // Incorrect Player & roll & plane
        this.model.move(new PlayerImpl("Incorrect"), 10, -6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void incorrectPlanePlayer() {
        // Incorrect Player & plane
        this.model.move(new PlayerImpl("Incorrect"), -1, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void notMovable() {
        assertArrayEquals(new int[]{77, 77, 77, 77}, this.model.positions(this.red));
        // Incorrect Player & roll
        this.model.move(this.red, 1, 4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void notMovable2() {
        assertArrayEquals(new int[]{77, 77, 77, 77}, this.model.positions(this.red));
        // Incorrect Player & roll
        this.model.move(this.red, 1, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void notMovable3() {
        this.model.move(this.red, 0, 6);
        this.model.move(this.red, 0, 6);
        this.model.move(this.red, 0, 6);
        this.model.move(this.red, 0, 6);
        this.model.move(this.red, 0, 6);
        this.model.move(this.red, 0, 6);
        this.model.move(this.red, 0, 6);
        this.model.move(this.red, 0, 6);
        this.model.move(this.red, 0, 6);
        this.model.move(this.red, 0, 4);
        assertArrayEquals(new int[]{76, 77, 77, 77}, this.model.positions(this.red));
        this.model.move(this.red, 0, 1);
    }


    @Test
    public void testGetPlayers() {
        assertArrayEquals(new Player[]{this.red, this.yellow, this.blue, this.green},
                this.model.getPlayers());
    }


    @Test
    public void testMovablePlanes() {
        assertEquals(new ArrayList<>(), this.model.movablePlanes(this.red, 1));
        this.model.move(this.red, 1, 5);
        assertEquals(new ArrayList<>(Collections.singletonList(1)), this.model.movablePlanes(this.red, 1));
        this.model.move(this.red, 1, 6);
        this.model.move(this.red, 0, 5);
        assertEquals(new ArrayList<>(Arrays.asList(0, 1)), this.model.movablePlanes(this.red, 4));
        assertEquals(new ArrayList<>(Arrays.asList(0, 1, 2, 3)), this.model.movablePlanes(this.red, 6));
        assertEquals(new ArrayList<>(Arrays.asList(0, 1, 2, 3)), this.model.movablePlanes(this.red, 5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void IncorrectPlayer() {
        this.model.movablePlanes(new PlayerImpl("Incorrect"), 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void InCorrectRoll() {
        this.model.movablePlanes(this.red, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void InCorrectRoll1() {
        this.model.movablePlanes(this.red, 7);
    }

    @Test(expected = IllegalArgumentException.class)
    public void IncorrectRollAndPlayer() {
        this.model.movablePlanes(new PlayerImpl("Incorrect"), 7);
    }

    @Test
    public void testJumpTile() {
        assertArrayEquals(new int[]{2, 6, 10, 14, 18, 22, 26, 30, 34, 38, 42, 46, 50}, this.model.jumpTile(this.red));
        assertArrayEquals(new int[]{3, 7, 11, 15, 19, 23, 27, 31, 35, 39, 43, 47, 51}, this.model.jumpTile(this.yellow));
        assertArrayEquals(new int[]{4, 8, 12, 16, 20, 24, 28, 32, 36, 40, 44, 48, 52}, this.model.jumpTile(this.blue));
        assertArrayEquals(new int[]{1, 5, 9, 13, 17, 21, 25, 29, 33, 37, 41, 45, 49}, this.model.jumpTile(this.green));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPlayerJumpTile() {
        this.model.jumpTile(new PlayerImpl("incorrect"));
    }

    @Test
    public void testMovablePositions() {
        this.red = new PlayerImpl("Red", 0, 11, 14, 36);
        this.yellow = new PlayerImpl("Yellow", 0, 1, 18, 13);
        this.blue = new PlayerImpl("Blue", 0, 6, 13, 14);
        this.green = new PlayerImpl("Green", 12, 50, 17, 45);
        this.model = new AreoBoardImpl(this.red, this.yellow, this.blue, this.green, new Random());
        assertEquals(new ArrayList<>(Arrays.asList(1, 2, 6, 3, 4, 5, 10)), this.looper(this.red, 0));
        assertEquals(new ArrayList<>(Arrays.asList(12, 13, 14, 18, 30, 15, 16, 17)), this.looper(this.red, 1));
        assertEquals(new ArrayList<>(Arrays.asList(15, 16, 17, 18, 30, 34, 19, 20)), this.looper(this.red, 2));
        assertEquals(new ArrayList<>(Arrays.asList(37, 38, 42, 39, 40, 41, 46)), this.looper(this.red, 3));
        assertEquals(new ArrayList<>(Arrays.asList(14, 15, 19, 16, 17, 18, 23)), this.looper(this.yellow, 0));
        assertEquals(new ArrayList<>(Arrays.asList(15, 19, 16, 17, 18, 23, 20)), this.looper(this.yellow, 1));
        assertEquals(new ArrayList<>(Arrays.asList(32, 33, 34, 35, 39, 36, 37)), this.looper(this.yellow, 2));
        assertEquals(new ArrayList<>(Arrays.asList(27, 31, 43, 28, 29, 30, 47, 32)), this.looper(this.yellow, 3));
        assertEquals(new ArrayList<>(Arrays.asList(27, 28, 32, 29, 30, 31, 36)), this.looper(this.blue, 0));
        assertEquals(new ArrayList<>(Arrays.asList(33, 34, 35, 36, 40, 37, 38)), this.looper(this.blue, 1));
        assertEquals(new ArrayList<>(Arrays.asList(40, 44, 4, 41, 42, 43, 8, 45)), this.looper(this.blue, 2));
        assertEquals(new ArrayList<>(Arrays.asList(41, 42, 43, 44, 4, 8, 45, 46)), this.looper(this.blue, 3));
        assertEquals(new ArrayList<>(Arrays.asList(41, 42, 43, 44, 4, 8, 45, 46)), this.looper(this.blue, 3));
        assertEquals(new ArrayList<>(Arrays.asList(52, 1, 5, 17, 2, 3, 4, 21)), this.looper(this.green, 0));
        assertEquals(new ArrayList<>(Arrays.asList(65, 66, 67, 68, 69, 70)), this.looper(this.green, 1));
        assertEquals(new ArrayList<>(Arrays.asList(5, 17, 21, 6, 7, 8, 9, 13, 10)), this.looper(this.green, 2));
        assertEquals(new ArrayList<>(Arrays.asList(33, 37, 34, 35, 36, 65)), this.looper(this.green, 3));
    }

    @Test
    public void testIntermediatePositions() {
        this.red = new PlayerImpl("One", -1, 13, 14, 55);
        this.blue = new PlayerImpl("Two", 0, 24, 41, 49);
        this.model = new AreoBoardImpl(this.red, this.yellow, this.blue, this.green, new Random());
        // PlayerOne
        // testing set path
        assertEquals(new ArrayList<>(Arrays.asList(77, 78)), this.model.intermediatePositions(this.red, 0, 5));
        // testing set path
        assertEquals(new ArrayList<>(Arrays.asList(77, 78)), this.model.intermediatePositions(this.red, 0, 5));
        // testing set path
        assertEquals(new ArrayList<>(Arrays.asList(77, 78)), this.model.intermediatePositions(this.red, 0, 6));
        // testing set path
        assertEquals(new ArrayList<>(Collections.singletonList(77)), this.model.intermediatePositions(this.red, 0, 1));
        // testing jump from 14
        assertEquals(new ArrayList<>(Arrays.asList(13, 14, 15, 16, 17, 18, 30)), this.model.intermediatePositions(this.red, 1, 1));
        // testing move
        assertEquals(new ArrayList<>(Arrays.asList(13, 14, 15)), this.model.intermediatePositions(this.red, 1, 2));
        // testing jump from 18
        assertEquals(new ArrayList<>(Arrays.asList(13, 14, 15, 16, 17, 18, 30, 31, 32, 33, 34)), this.model.intermediatePositions(this.red, 1, 5));
        // testing move
        assertEquals(new ArrayList<>(Arrays.asList(13, 14, 15, 16, 17, 18, 19)), this.model.intermediatePositions(this.red, 1, 6));
        assertEquals(new ArrayList<>(Arrays.asList(14, 15)), this.model.intermediatePositions(this.red, 2, 1));
        // testing move
        assertEquals(new ArrayList<>(Arrays.asList(14, 15, 16)), this.model.intermediatePositions(this.red, 2, 2));
        // testing move
        assertEquals(new ArrayList<>(Arrays.asList(14, 15, 16, 17)), this.model.intermediatePositions(this.red, 2, 3));
        // testing jump from 18
        assertEquals(new ArrayList<>(Arrays.asList(14, 15, 16, 17, 18, 30, 31, 32, 33, 34)), this.model.intermediatePositions(this.red, 2, 4));
        // testing move
        assertEquals(new ArrayList<>(Arrays.asList(14, 15, 16, 17, 18, 19)), this.model.intermediatePositions(this.red, 2, 5));
        // testing move
        assertEquals(new ArrayList<>(Arrays.asList(14, 15, 16, 17, 18, 19, 20)), this.model.intermediatePositions(this.red, 2, 6));
        // testing move
        assertEquals(new ArrayList<>(Arrays.asList(75, 76)), this.model.intermediatePositions(this.red, 3, 1));
        // testing bounce
        assertEquals(new ArrayList<>(Arrays.asList(75, 76, 75)), this.model.intermediatePositions(this.red, 3, 2));
        // testing bounce
        assertEquals(new ArrayList<>(Arrays.asList(75, 76, 75, 74)), this.model.intermediatePositions(this.red, 3, 3));
        // testing bounce
        assertEquals(new ArrayList<>(Arrays.asList(75, 76, 75, 74, 73)), this.model.intermediatePositions(this.red, 3, 4));
        // testing bounce
        assertEquals(new ArrayList<>(Arrays.asList(75, 76, 75, 74, 73, 72)), this.model.intermediatePositions(this.red, 3, 5));
        // testing bounce
        assertEquals(new ArrayList<>(Arrays.asList(75, 76, 75, 74, 73, 72, 71)), this.model.intermediatePositions(this.red, 3, 6));
        // PlayerTwo
        // testing normal jump
        assertEquals(new ArrayList<>(Arrays.asList(82, 27, 28, 29, 30, 31, 32)), this.model.intermediatePositions(this.blue, 0, 2));
        assertEquals(new ArrayList<>(Arrays.asList(82, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36)), this.model.intermediatePositions(this.blue, 0, 6));
        assertEquals(new ArrayList<>(Arrays.asList(50, 51, 52, 1, 2, 3, 4)), this.model.intermediatePositions(this.blue, 1, 2));
        assertEquals(new ArrayList<>(Arrays.asList(50, 51, 52, 1, 2, 3, 4, 5, 6, 7, 8)), this.model.intermediatePositions(this.blue, 1, 6));
        assertEquals(new ArrayList<>(Arrays.asList(15, 16, 17, 18, 19, 20)), this.model.intermediatePositions(this.blue, 2, 1));
        // testing move
        assertEquals(new ArrayList<>(Arrays.asList(23, 24)), this.model.intermediatePositions(this.blue, 3, 1));
        assertEquals(new ArrayList<>(Arrays.asList(23, 24, 59, 60)), this.model.intermediatePositions(this.blue, 3, 3));
        assertEquals(new ArrayList<>(Arrays.asList(23, 24, 59, 60, 61, 62, 63)), this.model.intermediatePositions(this.blue, 3, 6));
    }

    private List<Integer> looper(Player player, int plane) {
        List<Integer> arr = new ArrayList<>();
        for (int i = 1; i < 7; i++) {
            for (int pos : this.model.stayedPositions(player, plane, i)) {
                if (!arr.contains(pos)) {
                    arr.add(pos);
                }
            }
        }
        return arr;
    }
}
