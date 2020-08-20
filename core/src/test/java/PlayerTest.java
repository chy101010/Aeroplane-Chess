import com.areoplane.game.model.Player;
import com.areoplane.game.model.PlayerImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class PlayerTest {
    Player playerOne;
    Player playerTwo;

    @Before
    public void textFixture() {
        this.playerOne = new PlayerImpl("One");
        this.playerTwo = new PlayerImpl("Two");
    }


    // Tested IsWin/GetName/SetPath/move/plansAt/getPositions/crash

    @Test
    public void testGetName() {
        assertEquals("One", this.playerOne.getName());
        assertEquals("Two", this.playerTwo.getName());
    }

    @Test
    public void testSetPath() {
        assertArrayEquals(new int[]{-1, -1, -1, -1}, this.playerOne.getPositions());
        this.playerOne.setPath(1);
        assertArrayEquals(new int[]{-1, 0, -1, -1}, this.playerOne.getPositions());
        this.playerOne.setPath(0);
        assertArrayEquals(new int[]{0, 0, -1, -1}, this.playerOne.getPositions());
        this.playerOne.setPath(2);
        assertArrayEquals(new int[]{0, 0, 0, -1}, this.playerOne.getPositions());
        this.playerOne.setPath(3);
        assertArrayEquals(new int[]{0, 0, 0, 0}, this.playerOne.getPositions());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPathWrongPlanes() {
        this.playerOne.setPath(10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPathWrongPlanes1() {
        this.playerTwo.setPath(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPathDoesntExist() {
        this.playerTwo.setPath(5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPathAlreadyPath() {
        this.playerTwo.setPath(1);
        assertArrayEquals(new int[]{-1, 0, -1, -1}, this.playerTwo.getPositions());
        this.playerTwo.setPath(1);
    }

    // Moving to the end
    @Test
    public void testMove() {
        assertArrayEquals(new int[]{-1, -1, -1, -1}, this.playerTwo.getPositions());
        this.playerTwo.setPath(1);
        assertArrayEquals(new int[]{-1, 0, -1, -1}, this.playerTwo.getPositions());
        // Moving into different color
        this.playerTwo.move(1, 1);
        assertArrayEquals(new int[]{-1, 1, -1, -1}, this.playerTwo.getPositions());
        // Moving into the same color
        this.playerTwo.move(1, 1);
        assertArrayEquals(new int[]{-1, 6, -1, -1}, this.playerTwo.getPositions());
        // Moving into the different color
        this.playerTwo.move(1, 6);
        assertArrayEquals(new int[]{-1, 12, -1, -1}, this.playerTwo.getPositions());
        // Moving into the same color at index 14 that will sends the plane to 28
        this.playerTwo.move(1, 2);
        assertArrayEquals(new int[]{-1, 30, -1, -1}, this.playerTwo.getPositions());
        // Moving into the same color
        this.playerTwo.move(1, 4);
        assertArrayEquals(new int[]{-1, 38, -1, -1}, this.playerTwo.getPositions());
        // Moving into the same color
        this.playerTwo.move(1, 4);
        assertArrayEquals(new int[]{-1, 46, -1, -1}, this.playerTwo.getPositions());
        // Moving into the same color
        this.playerTwo.move(1, 4);
        assertArrayEquals(new int[]{-1, 50, -1, -1}, this.playerTwo.getPositions());
        // Moving into the inner path
        this.playerTwo.move(1, 3);
        assertArrayEquals(new int[]{-1, 53, -1, -1}, this.playerTwo.getPositions());
        // Bounding off from the landing area
        this.playerTwo.move(1, 4);
        assertArrayEquals(new int[]{-1, 55, -1, -1}, this.playerTwo.getPositions());
        // Bounding off from the landing area
        this.playerTwo.move(1, 6);
        assertArrayEquals(new int[]{-1, 51, -1, -1}, this.playerTwo.getPositions());
        // Bounding off from the landing area
        this.playerTwo.move(1, 5);
        assertArrayEquals(new int[]{-1, 56, -1, -1}, this.playerTwo.getPositions());
    }

    // Moving the plane in the airport
    @Test(expected = IllegalArgumentException.class)
    public void testMovePort() {
        assertArrayEquals(new int[]{-1, -1, -1, -1}, this.playerTwo.getPositions());
        this.playerTwo.move(1, 10);
    }

    // Moving the plane with incorrect steps
    @Test(expected = IllegalArgumentException.class)
    public void testMoveIncorrectSteps() {
        assertArrayEquals(new int[]{-1, -1, -1, -1}, this.playerTwo.getPositions());
        this.playerTwo.setPath(3);
        assertArrayEquals(new int[]{-1, -1, -1, 0}, this.playerTwo.getPositions());
        this.playerTwo.move(3, -1);
    }

    // Moving the plane with incorrect steps
    @Test(expected = IllegalArgumentException.class)
    public void testMoveIncorrectSteps1() {
        assertArrayEquals(new int[]{-1, -1, -1, -1}, this.playerTwo.getPositions());
        this.playerTwo.setPath(3);
        assertArrayEquals(new int[]{-1, -1, -1, 0}, this.playerTwo.getPositions());
        this.playerTwo.move(3, 7);
    }

    // Moving the already landed plane
    @Test(expected = IllegalArgumentException.class)
    public void testMoveLandedPlane() {
        this.playerTwo.setPath(3);
        assertArrayEquals(new int[]{-1, -1, -1, 0}, this.playerTwo.getPositions());
        this.playerTwo.move(3, 2);
        assertArrayEquals(new int[]{-1, -1, -1, 6}, this.playerTwo.getPositions());
        this.playerTwo.move(3, 4);
        assertArrayEquals(new int[]{-1, -1, -1, 14}, this.playerTwo.getPositions());
        this.playerTwo.move(3, 4);
        assertArrayEquals(new int[]{-1, -1, -1, 34}, this.playerTwo.getPositions());
        this.playerTwo.move(3, 4);
        assertArrayEquals(new int[]{-1, -1, -1, 42}, this.playerTwo.getPositions());
        this.playerTwo.move(3, 4);
        assertArrayEquals(new int[]{-1, -1, -1, 50}, this.playerTwo.getPositions());
        this.playerTwo.move(3, 6);
        assertArrayEquals(new int[]{-1, -1, -1, 56}, this.playerTwo.getPositions());
        this.playerTwo.move(3, 1);
    }

    @Test
    public void testIsWin() {
        assertFalse(this.playerTwo.isWin());
        assertArrayEquals(new int[]{-1, -1, -1, -1}, this.playerTwo.getPositions());
        this.playerTwo.setPath(0);
        this.playerTwo.move(0, 5);
        assertFalse(this.playerTwo.isWin());
        assertArrayEquals(new int[]{5, -1, -1, -1}, this.playerTwo.getPositions());
        this.playerTwo.move(0, 5);
        assertArrayEquals(new int[]{14, -1, -1, -1}, this.playerTwo.getPositions());
        this.playerTwo.move(0, 5);
        assertArrayEquals(new int[]{19, -1, -1, -1}, this.playerTwo.getPositions());
        this.playerTwo.move(0, 5);
        assertArrayEquals(new int[]{24, -1, -1, -1}, this.playerTwo.getPositions());
        this.playerTwo.move(0, 5);
        assertArrayEquals(new int[]{29, -1, -1, -1}, this.playerTwo.getPositions());
        this.playerTwo.move(0, 5);
        assertArrayEquals(new int[]{38, -1, -1, -1}, this.playerTwo.getPositions());
        this.playerTwo.move(0, 4);
        assertArrayEquals(new int[]{46, -1, -1, -1}, this.playerTwo.getPositions());
        this.playerTwo.move(0, 6);
        assertArrayEquals(new int[]{52, -1, -1, -1}, this.playerTwo.getPositions());
        this.playerTwo.move(0, 4);
        assertArrayEquals(new int[]{56, -1, -1, -1}, this.playerTwo.getPositions());
        this.playerTwo.setPath(1);
        this.playerTwo.move(1, 2);
        assertArrayEquals(new int[]{56, 6, -1, -1}, this.playerTwo.getPositions());
        this.playerTwo.move(1, 6);
        assertArrayEquals(new int[]{56, 12, -1, -1}, this.playerTwo.getPositions());
        this.playerTwo.move(1, 5);
        assertFalse(this.playerTwo.isWin());
        assertArrayEquals(new int[]{56, 17, -1, -1}, this.playerTwo.getPositions());
        this.playerTwo.move(1, 5);
        assertArrayEquals(new int[]{56, 26, -1, -1}, this.playerTwo.getPositions());
        this.playerTwo.move(1, 4);
        assertArrayEquals(new int[]{56, 34, -1, -1}, this.playerTwo.getPositions());
        this.playerTwo.move(1, 5);
        assertArrayEquals(new int[]{56, 39, -1, -1}, this.playerTwo.getPositions());
        this.playerTwo.move(1, 5);
        assertArrayEquals(new int[]{56, 44, -1, -1}, this.playerTwo.getPositions());
        this.playerTwo.move(1, 4);
        assertArrayEquals(new int[]{56, 48, -1, -1}, this.playerTwo.getPositions());
        this.playerTwo.move(1, 6);
        assertArrayEquals(new int[]{56, 54, -1, -1}, this.playerTwo.getPositions());
        this.playerTwo.move(1, 3);
        assertArrayEquals(new int[]{56, 55, -1, -1}, this.playerTwo.getPositions());
        this.playerTwo.move(1, 1);
        assertArrayEquals(new int[]{56, 56, -1, -1}, this.playerTwo.getPositions());
        this.playerTwo.setPath(2);
        this.playerTwo.move(2, 1);
        assertArrayEquals(new int[]{56, 56, 1, -1}, this.playerTwo.getPositions());
        this.playerTwo.move(2, 1);
        assertArrayEquals(new int[]{56, 56, 6, -1}, this.playerTwo.getPositions());
        this.playerTwo.move(2, 1);
        assertArrayEquals(new int[]{56, 56, 7, -1}, this.playerTwo.getPositions());
        assertFalse(this.playerTwo.isWin());
        this.playerTwo.move(2, 1);
        assertArrayEquals(new int[]{56, 56, 8, -1}, this.playerTwo.getPositions());
        this.playerTwo.move(2, 1);
        assertArrayEquals(new int[]{56, 56, 9, -1}, this.playerTwo.getPositions());
        this.playerTwo.move(2, 1);
        assertArrayEquals(new int[]{56, 56, 14, -1}, this.playerTwo.getPositions());
        this.playerTwo.move(2, 4);
        assertArrayEquals(new int[]{56, 56, 34, -1}, this.playerTwo.getPositions());
        this.playerTwo.move(2, 1);
        assertArrayEquals(new int[]{56, 56, 35, -1}, this.playerTwo.getPositions());
        this.playerTwo.move(2, 1);
        assertArrayEquals(new int[]{56, 56, 36, -1}, this.playerTwo.getPositions());
        this.playerTwo.move(2, 1);
        assertArrayEquals(new int[]{56, 56, 37, -1}, this.playerTwo.getPositions());
        this.playerTwo.move(2, 1);
        assertFalse(this.playerTwo.isWin());
        assertArrayEquals(new int[]{56, 56, 42, -1}, this.playerTwo.getPositions());
        this.playerTwo.move(2, 1);
        assertArrayEquals(new int[]{56, 56, 43, -1}, this.playerTwo.getPositions());
        this.playerTwo.move(2, 1);
        assertArrayEquals(new int[]{56, 56, 44, -1}, this.playerTwo.getPositions());
        assertFalse(this.playerTwo.isWin());
        this.playerTwo.move(2, 1);
        assertArrayEquals(new int[]{56, 56, 45, -1}, this.playerTwo.getPositions());
        this.playerTwo.move(2, 6);
        assertFalse(this.playerTwo.isWin());
        assertArrayEquals(new int[]{56, 56, 51, -1}, this.playerTwo.getPositions());
        this.playerTwo.move(2, 6);
        assertArrayEquals(new int[]{56, 56, 55, -1}, this.playerTwo.getPositions());
        this.playerTwo.move(2, 3);
        assertArrayEquals(new int[]{56, 56, 54, -1}, this.playerTwo.getPositions());
        this.playerTwo.move(2, 2);
        assertArrayEquals(new int[]{56, 56, 56, -1}, this.playerTwo.getPositions());
        this.playerTwo.setPath(3);
        this.playerTwo.move(3, 6);
        assertArrayEquals(new int[]{56, 56, 56, 10}, this.playerTwo.getPositions());
        this.playerTwo.move(3, 6);
        assertArrayEquals(new int[]{56, 56, 56, 16}, this.playerTwo.getPositions());
        this.playerTwo.move(3, 6);
        assertArrayEquals(new int[]{56, 56, 56, 26}, this.playerTwo.getPositions());
        this.playerTwo.move(3, 6);
        assertArrayEquals(new int[]{56, 56, 56, 32}, this.playerTwo.getPositions());
        this.playerTwo.move(3, 6);
        assertArrayEquals(new int[]{56, 56, 56, 42}, this.playerTwo.getPositions());
        this.playerTwo.move(3, 6);
        assertArrayEquals(new int[]{56, 56, 56, 48}, this.playerTwo.getPositions());
        this.playerTwo.move(3, 6);
        assertArrayEquals(new int[]{56, 56, 56, 54}, this.playerTwo.getPositions());
        assertFalse(this.playerTwo.isWin());
        this.playerTwo.move(3, 2);
        assertArrayEquals(new int[]{56, 56, 56, 56}, this.playerTwo.getPositions());
        assertTrue(this.playerTwo.isWin());
    }

    @Test
    public void testPlanesAt() {
        assertEquals(new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3)),
                this.playerTwo.planesAt(-1));
        assertEquals(new ArrayList<>(), this.playerTwo.planesAt(0));
        this.playerTwo.setPath(1);
        assertEquals(new ArrayList<Integer>(Collections.singletonList(1)), this.playerTwo.planesAt(0));
        this.playerTwo.move(1, 5);
        assertEquals(new ArrayList<Integer>(), this.playerTwo.planesAt(0));
        assertEquals(new ArrayList<>(Collections.singletonList(1)), this.playerTwo.planesAt(5));
        this.playerTwo.move(1, 1);
        assertEquals(new ArrayList<Integer>(), this.playerTwo.planesAt(5));
        assertEquals(new ArrayList<Integer>(Collections.singletonList(1)), this.playerTwo.planesAt(10));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPlanesAtOutOfBound() {
        this.playerTwo.planesAt(57);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPlanesAtOutOfBound1() {
        this.playerTwo.planesAt(-2);
    }

    @Test
    public void testGetPositions() {
        this.playerOne.setPath(0);
        this.playerOne.setPath(1);
        this.playerOne.move(0, 4);
        this.playerOne.move(1, 5);
        assertArrayEquals(new int[]{4, 5, -1, -1}, this.playerOne.getPositions());
        this.playerOne.setPath(2);
        this.playerOne.move(2, 6);
        assertArrayEquals(new int[]{4, 5, 10, -1}, this.playerOne.getPositions());
    }

    @Test
    public void testCrash() {
        this.playerOne.setPath(0);
        this.playerOne.move(0, 2);
        assertArrayEquals(new int[]{6, -1, -1, -1}, this.playerOne.getPositions());
        this.playerOne.crash(0);
        assertArrayEquals(new int[]{-1, -1, -1, -1}, this.playerOne.getPositions());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCrashInPort() {
        this.playerOne.setPath(0);
        this.playerOne.crash(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCrashInPort1() {
        this.playerOne.crash(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCrashInnerPath() {
        this.playerOne.setPath(0);
        this.playerOne.move(0, 6);
        this.playerOne.move(0, 6);
        this.playerOne.move(0, 6);
        this.playerOne.move(0, 6);
        this.playerOne.move(0, 6);
        this.playerOne.move(0, 6);
        this.playerOne.move(0, 3);
        assertArrayEquals(new int[]{51, -1, -1, -1}, this.playerOne.getPositions());
        this.playerOne.crash(0);
    }

    @Test
    public void testMovablePosition() {
        this.playerOne = new PlayerImpl("One", -1, 0, 13, 50);
        assertEquals(new ArrayList<>(Collections.singletonList(0)), this.looper(this.playerOne, 0));
        assertEquals(new ArrayList<>(Arrays.asList(1, 6, 2, 3, 4, 5, 10)), this.looper(this.playerOne, 1));
        assertEquals(new ArrayList<>(Arrays.asList(30, 18, 14, 15, 16, 17, 34, 19)), this.looper(this.playerOne, 2));
        assertEquals(new ArrayList<>(Arrays.asList(51, 52, 53, 54, 55, 56)), this.looper(this.playerOne, 3));
    }

    @Test
    public void testIntermediateSteps() {
        this.playerOne = new PlayerImpl("One", -1, 13, 14, 55);
        // testing set path
        assertEquals(new ArrayList<>(Arrays.asList(-1, 0)), this.playerOne.intermediatePositions(0, 5));
        // testing set path
        assertEquals(new ArrayList<>(Arrays.asList(-1, 0)), this.playerOne.intermediatePositions(0, 6));
        // testing set path
        assertEquals(new ArrayList<>(Collections.singletonList(-1)), this.playerOne.intermediatePositions(0, 1));
        // testing jump from 14
        assertEquals(new ArrayList<>(Arrays.asList(13, 14, 15, 16, 17, 18, 30)), this.playerOne.intermediatePositions(1, 1));
        // testing move
        assertEquals(new ArrayList<>(Arrays.asList(13, 14, 15)), this.playerOne.intermediatePositions(1, 2));
        // testing jump from 18
        assertEquals(new ArrayList<>(Arrays.asList(13, 14, 15, 16, 17, 18, 30, 31, 32, 33, 34)), this.playerOne.intermediatePositions(1, 5));
        // testing move
        assertEquals(new ArrayList<>(Arrays.asList(13, 14, 15, 16, 17, 18, 19)), this.playerOne.intermediatePositions(1, 6));
        assertEquals(new ArrayList<>(Arrays.asList(14, 15)), this.playerOne.intermediatePositions(2, 1));
        // testing move
        assertEquals(new ArrayList<>(Arrays.asList(14, 15, 16)), this.playerOne.intermediatePositions(2, 2));
        // testing move
        assertEquals(new ArrayList<>(Arrays.asList(14, 15, 16, 17)), this.playerOne.intermediatePositions(2, 3));
        // testing jump from 18
        assertEquals(new ArrayList<>(Arrays.asList(14, 15, 16, 17, 18, 30, 31, 32, 33, 34)), this.playerOne.intermediatePositions(2, 4));
        // testing move
        assertEquals(new ArrayList<>(Arrays.asList(14, 15, 16, 17, 18, 19)), this.playerOne.intermediatePositions(2, 5));
        // testing move
        assertEquals(new ArrayList<>(Arrays.asList(14, 15, 16, 17, 18, 19, 20)), this.playerOne.intermediatePositions(2, 6));
        // testing move
        assertEquals(new ArrayList<>(Arrays.asList(55, 56)), this.playerOne.intermediatePositions(3, 1));
        // testing bounce
        assertEquals(new ArrayList<>(Arrays.asList(55, 56, 55)), this.playerOne.intermediatePositions(3, 2));
        // testing bounce
        assertEquals(new ArrayList<>(Arrays.asList(55, 56, 55, 54)), this.playerOne.intermediatePositions(3, 3));
        // testing bounce
        assertEquals(new ArrayList<>(Arrays.asList(55, 56, 55, 54, 53)), this.playerOne.intermediatePositions(3, 4));
        // testing bounce
        assertEquals(new ArrayList<>(Arrays.asList(55, 56, 55, 54, 53, 52)), this.playerOne.intermediatePositions(3, 5));
        // testing bounce
        assertEquals(new ArrayList<>(Arrays.asList(55, 56, 55, 54, 53, 52, 51)), this.playerOne.intermediatePositions(3, 6));
        this.playerTwo = new PlayerImpl("Two", 0, 24, 41, 49);
        // testing normal jump
        assertEquals(new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6)), this.playerTwo.intermediatePositions(0, 2));
        assertEquals(new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)), this.playerTwo.intermediatePositions(0, 6));
        assertEquals(new ArrayList<>(Arrays.asList(24, 25, 26, 27, 28, 29, 30)), this.playerTwo.intermediatePositions(1, 2));
        assertEquals(new ArrayList<>(Arrays.asList(24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34)), this.playerTwo.intermediatePositions(1, 6));
        assertEquals(new ArrayList<>(Arrays.asList(41, 42, 43, 44, 45, 46)), this.playerTwo.intermediatePositions(2, 1));
        // testing move
        assertEquals(new ArrayList<>(Arrays.asList(49, 50)), this.playerTwo.intermediatePositions(3, 1));
        assertEquals(new ArrayList<>(Arrays.asList(49, 50, 51, 52)), this.playerTwo.intermediatePositions(3, 3));
        assertEquals(new ArrayList<>(Arrays.asList(49, 50, 51, 52, 53, 54, 55)), this.playerTwo.intermediatePositions(3, 6));
    }

    private List<Integer> looper(Player player, int plane) {
        List<Integer> arr = new ArrayList<>();
        for (int i = 1; i < 7; i++) {
            for (int pos : player.stayedPositions(plane, i))
                if (!arr.contains(pos)) {
                    arr.add(pos);
                }
        }
        return arr;
    }
}
