import com.areoplane.game.Strategy.*;
import com.areoplane.game.Model.AreoBoardImpl;
import com.areoplane.game.Model.AreoModel;
import com.areoplane.game.Model.Player;
import com.areoplane.game.Model.PlayerImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class StrategyTest {
    Random rand = new Random();
    IStrategy reachEnd;
    IStrategy random;
    IStrategy putOnPath;
    IStrategy jump;
    IStrategy innerPath;
    IStrategy escape;
    IStrategy crash;
    Player one;
    Player two;
    Player three;
    Player four;
    AreoModel model;

    @Before
    public void textFixture() {
        this.rand.setSeed(1);
        this.reachEnd = new ReachEndStrategy();
        this.random = new RandomStrategy(this.rand);
        this.putOnPath = new PutOnPathStrategy();
        this.jump = new JumpStrategy();
        this.innerPath = new InnerPathStrategy();
        this.escape = new EscapeStrategy();
        this.crash = new CrashStrategy();
        this.one = new PlayerImpl("test1", -1, -1, 49, 50);
        this.two = new PlayerImpl("test2", 1, 2, 4, 3);
        this.three = new PlayerImpl("test3", -1, -1, -1, -1);
        this.four = new PlayerImpl("test4", 1, 16, 13, 17);
        this.model = new AreoBoardImpl(this.one, this.two, this.three, this.four, new Random());
    }

    @Test
    public void testEndReach() {
        // Checking Turn
        assertEquals(one, model.getTurn());
        assertEquals(3, (int) this.reachEnd.choosePlane(1, model));
        // Set next player
        model.nextPlayer(3);
        assertEquals(two, model.getTurn());
        assertEquals(2, (int) this.reachEnd.choosePlane(4, model));
        // Set next player
        model.nextPlayer(1);
        assertEquals(three, model.getTurn());
        assertNull(this.reachEnd.choosePlane(4, model));
        assertEquals(0, (int) this.reachEnd.choosePlane(5, model));
        // Set next player
        model.nextPlayer(1);
        assertEquals(four, model.getTurn());
        assertEquals(3, (int) this.reachEnd.choosePlane(5, model));
    }

    @Test
    public void testRandom() {
        assertEquals(one, model.getTurn());
        // 2-3 movable
        assertEquals(3, (int) this.random.choosePlane(1, model));
        assertEquals(2, (int) this.random.choosePlane(1, model));
        assertEquals(2, (int) this.random.choosePlane(1, model));
        // 0-3 movable
        assertEquals(1, (int) this.random.choosePlane(5, model));
        assertEquals(0, (int) this.random.choosePlane(6, model));
        assertEquals(0, (int) this.random.choosePlane(5, model));
        assertEquals(1, (int) this.random.choosePlane(6, model));
        assertEquals(2, (int) this.random.choosePlane(5, model));
        assertEquals(3, (int) this.random.choosePlane(6, model));
    }

    @Test
    public void testPutOnPath() {
        assertEquals(one, model.getTurn());
        assertNull(this.putOnPath.choosePlane(1, model));
        assertEquals(0, (int) this.putOnPath.choosePlane(5, model));
        assertEquals(0, (int) this.putOnPath.choosePlane(6, model));
        // Set next player
        model.nextPlayer(1);
        assertEquals(two, model.getTurn());
        assertNull(this.putOnPath.choosePlane(5, model));
        // Set next player
        model.nextPlayer(1);
        assertEquals(three, model.getTurn());
        assertEquals(0, (int) this.putOnPath.choosePlane(6, model));
        // Set next player
        model.nextPlayer(1);
        assertEquals(four, model.getTurn());
        assertNull(this.putOnPath.choosePlane(5, model));
    }

    @Test
    public void testJump() {
        assertEquals(one, model.getTurn());
        assertNull(this.jump.choosePlane(1, model));
        assertNull(this.jump.choosePlane(2, model));
        // Set next player
        model.nextPlayer(1);
        assertEquals(two, model.getTurn());
        assertEquals(0, (int) this.jump.choosePlane(1, model));
        assertEquals(2, (int) this.jump.choosePlane(2, model));
        assertEquals(1, (int) this.jump.choosePlane(4, model));
        assertEquals(3, (int) this.jump.choosePlane(3, model));
        assertEquals(2, (int) this.jump.choosePlane(6, model));
        // Set next player
        model.nextPlayer(1);
        assertEquals(three, model.getTurn());
        assertNull(this.jump.choosePlane(1, model));
        assertNull(this.jump.choosePlane(6, model));
        // Set next player
        model.nextPlayer(1);
        assertEquals(four, model.getTurn());
        assertEquals(1, (int) this.jump.choosePlane(2, model));
        assertEquals(0, (int) this.jump.choosePlane(1, model));
        assertEquals(0, (int) this.jump.choosePlane(5, model));
    }

    @Test
    public void testInnerPath() {
        this.one = new PlayerImpl("One", 48, 20, 46, 50);
        this.two = new PlayerImpl("Two", 45, 47, 41, -1);
        this.model = new AreoBoardImpl(this.one, this.two, this.three, this.four, this.rand);
        assertEquals(this.one, model.getTurn());
        assertEquals(3, (int) this.innerPath.choosePlane(1, model));
        assertEquals(0, (int) this.innerPath.choosePlane(3, model));
        assertEquals(0, (int) this.innerPath.choosePlane(4, model));
        assertEquals(0, (int) this.innerPath.choosePlane(5, model));
        assertEquals(0, (int) this.innerPath.choosePlane(6, model));
        // Set next player
        model.nextPlayer(1);
        assertEquals(two, model.getTurn());
        assertNull(this.innerPath.choosePlane(1, model));
        assertNull(this.innerPath.choosePlane(2, model));
        assertNull(this.innerPath.choosePlane(3, model));
        assertEquals(1, (int) this.innerPath.choosePlane(4, model));
        assertEquals(1, (int) this.innerPath.choosePlane(5, model));
        assertEquals(0, (int) this.innerPath.choosePlane(6, model));
        // Set next player
        model.nextPlayer(1);
        assertEquals(three, model.getTurn());
        assertNull(this.innerPath.choosePlane(6, model));
    }

    @Test
    public void testEscapeStrategy() {
        this.one = new PlayerImpl("One", 5, 14, 43, 51);
        this.two = new PlayerImpl("Two", -1, -1, -1, 5);
        this.three = new PlayerImpl("Three", -1, -1, 20, 7);
        this.four = new PlayerImpl("Four", -1, -1, -1, 0);
        this.model = new AreoBoardImpl(this.one, this.two, this.three, this.four, this.rand);
        // Plane two of Player "One" can be destroyed by Plane four of Player "Four"
        assertEquals(2, (int) this.escape.choosePlane(4, this.model));
        this.one.move(2, 2);
        assertEquals(2, (int) this.escape.choosePlane(4, this.model));
        this.one.move(2, 1);
        // Plane two of Player "One" can be destroyed by Plane two of Player "Two"
        assertEquals(2, (int) this.escape.choosePlane(4, this.model));
        // Plane two of Player "One" escaped into the inner path
        this.one.move(2, 1);
        assertNull(this.escape.choosePlane(4, this.model));
        assertArrayEquals(new int[]{5, 14, 51, 51}, this.one.getPositions());

        // Setting Plane one of Player "Two" to path so it can destroy Plane two of Player "One"
        this.two.setPath(0);
        assertEquals(1, (int) this.escape.choosePlane(4, this.model));
        this.one.move(1, 6);
        assertEquals(1, (int) this.escape.choosePlane(4, this.model));
        // Moving Plane one of Player "One" out of the attacking range of Player "Two"
        this.one.move(1, 2);
        assertNull(this.escape.choosePlane(4, this.model));

        // Moving Plane four of Player "Three" to right behind the Plane zero of Player "One"
        this.three.move(3, 6);
        this.three.move(3, 1);
        assertEquals(0, (int) this.escape.choosePlane(1, this.model));
    }

    @Test
    public void testCrash() {
        this.one = new PlayerImpl("One", 13, 25, 39, 45);
        this.two = new PlayerImpl("Two", 4, -1, -1, -1);
        this.three = new PlayerImpl("Three", 7, -1, -1, -1);
        this.four = new PlayerImpl("Four", 5, -1, -1, -1);
        this.model = new AreoBoardImpl(this.one, this.two, this.three, this.four, this.rand);
        // Plane zero of Player "Two" can be destroyed by Plane zero of Player "One" if the roll is 4
        // Plane zero of Player "four" can be destroyed by Plane two of Player "One" if the roll is 5
        assertNull(this.crash.choosePlane(1, this.model));
        assertNull(this.crash.choosePlane(2, this.model));
        assertNull(this.crash.choosePlane(3, this.model));
        assertEquals(0, (int) this.crash.choosePlane(4, this.model));
        assertEquals(2, (int) this.crash.choosePlane(5, this.model));
        assertNull(this.crash.choosePlane(6, this.model));
        // Moving Plane one of Player "One" so that Plane zero of Player "Three" is in its attacking range
        this.one.move(1, 1);
        assertEquals(1, (int) this.crash.choosePlane(3, this.model));
        // Moving Plane zero of Player "Three" into the attack-able short cut of Plane zero of Player "One"
        this.three.move(0, 1);
        assertEquals(0, (int) this.crash.choosePlane(5, this.model));
    }

}
