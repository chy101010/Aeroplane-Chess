import com.areoplane.game.Model.AreoBoardImpl;
import com.areoplane.game.Model.AreoModel;
import com.areoplane.game.Model.Player;
import com.areoplane.game.Model.PlayerImpl;
import com.areoplane.game.FileHandler.AreoFileHandler;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AreoFileHandlerTest {

    private Player a;
    private Player b;
    private Player c;
    private Player d;
    private Player e;
    private Player f;
    private Player g;
    private Player h;
    private AreoModel m;
    private AreoModel n;
    private AreoModel q;
    private AreoModel p;
    private StringBuilder builder;

    @Before
    public void testFixture() {
        this.a = new PlayerImpl("A", 1, 1, 1, 1);
        this.b = new PlayerImpl("B", 2, 3, 4, 5);
        this.c = new PlayerImpl("C", 10, 13, 5, 20);
        this.d = new PlayerImpl("d", 10, 50, 56, 41);
        this.e = new PlayerImpl("E", 40, 10, 20, 41);
        this.f = new PlayerImpl("F", -1, -1, 5, 1);
        this.g = new PlayerImpl("G", 5, 14, 2, 15);
        this.h = new PlayerImpl("H", 10, 21, 40, 31);
        this.m = new AreoBoardImpl(a, b, c, d);
        this.n = new AreoBoardImpl(e, f, g, h);
        this.q = new AreoBoardImpl(d, c, b, a);
        this.p = new AreoBoardImpl(h, g, f, e);
        this.builder = new StringBuilder();
    }

    @Test
    public void testConstructor() {
        AreoFileHandler h = new AreoFileHandler("constructor-test.txt", this.builder);
        assertEquals("10 10 14 10 10 20 30 10 15 -1 1 40 50 21 32 13\n" +
                "20 20 30 40 50 10 50 10 1 4 5 5 6 6 4 4\n", this.builder.toString());
    }

    @Test
    public void testWriteToData() {
        AreoFileHandler h = new AreoFileHandler("empty-test.txt", this.builder);
        assertEquals("", this.builder.toString());
        h.writeToData(this.m, 0);
        assertEquals("1 1 1 1 2 3 4 5 10 13 5 20 10 50 56 41\n", this.builder.toString());
        h.writeToData(this.n, 0);
        assertEquals("1 1 1 1 2 3 4 5 10 13 5 20 10 50 56 41\n", this.builder.toString());
        h.writeToData(this.n, 1);
        assertEquals("1 1 1 1 2 3 4 5 10 13 5 20 10 50 56 41\n" +
                "40 10 20 41 -1 -1 5 1 5 14 2 15 10 21 40 31\n", this.builder.toString());
        h.writeToData(this.p, 3);
        assertEquals("1 1 1 1 2 3 4 5 10 13 5 20 10 50 56 41\n" +
                "40 10 20 41 -1 -1 5 1 5 14 2 15 10 21 40 31\n" +
                "10 21 40 31 5 14 2 15 -1 -1 5 1 40 10 20 41\n", this.builder.toString());
    }

    @Test
    public void testDeleteFromData() {
        AreoFileHandler h = new AreoFileHandler("empty-test.txt", this.builder);
        assertEquals("", this.builder.toString());
        h.writeToData(this.m, 0);
        assertEquals("1 1 1 1 2 3 4 5 10 13 5 20 10 50 56 41\n", this.builder.toString());
        h.writeToData(this.n, 1);
        assertEquals("1 1 1 1 2 3 4 5 10 13 5 20 10 50 56 41\n" +
                "40 10 20 41 -1 -1 5 1 5 14 2 15 10 21 40 31\n", this.builder.toString());
        h.writeToData(this.q, 2);
        assertEquals("1 1 1 1 2 3 4 5 10 13 5 20 10 50 56 41\n" +
                "40 10 20 41 -1 -1 5 1 5 14 2 15 10 21 40 31\n" +
                "10 50 56 41 10 13 5 20 2 3 4 5 1 1 1 1\n", this.builder.toString());
        h.writeToData(this.p, 3);
        assertEquals("1 1 1 1 2 3 4 5 10 13 5 20 10 50 56 41\n" +
                "40 10 20 41 -1 -1 5 1 5 14 2 15 10 21 40 31\n" +
                "10 50 56 41 10 13 5 20 2 3 4 5 1 1 1 1\n" +
                "10 21 40 31 5 14 2 15 -1 -1 5 1 40 10 20 41\n", this.builder.toString());
        h.deleteFromData(2);
        assertEquals("1 1 1 1 2 3 4 5 10 13 5 20 10 50 56 41\n" +
                "40 10 20 41 -1 -1 5 1 5 14 2 15 10 21 40 31\n" +
                "10 21 40 31 5 14 2 15 -1 -1 5 1 40 10 20 41\n", this.builder.toString());
        h.deleteFromData(2);
        assertEquals("1 1 1 1 2 3 4 5 10 13 5 20 10 50 56 41\n" +
                "40 10 20 41 -1 -1 5 1 5 14 2 15 10 21 40 31\n", this.builder.toString());
        h.deleteFromData(0);
        assertEquals("40 10 20 41 -1 -1 5 1 5 14 2 15 10 21 40 31\n", this.builder.toString());
        h.deleteFromData(0);
        assertEquals("", this.builder.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteToDataException1() {
        AreoFileHandler h = new AreoFileHandler("empty-test.txt", this.builder);
        h.writeToData(this.m, 4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteToDataException2() {
        AreoFileHandler h = new AreoFileHandler("empty-test.txt", this.builder);
        h.writeToData(this.m, -1);
    }

    @Test
    public void testLoadModel() {
        AreoFileHandler h = new AreoFileHandler("constructor-test.txt", this.builder);
        AreoModel model = h.loadModel(0, new AreoBoardImpl.Builder(), new PlayerImpl.Builder());
        Player[] players = model.getPlayers();
        assertArrayEquals(new int[]{10, 10, 14, 10}, players[0].getPositions());
        assertEquals("Red", players[0].getName());
        assertArrayEquals(new int[]{10, 20, 30, 10}, players[1].getPositions());
        assertEquals("Yellow", players[1].getName());
        assertArrayEquals(new int[]{15, -1, 1, 40}, players[2].getPositions());
        assertEquals("Blue", players[2].getName());
        assertArrayEquals(new int[]{50, 21, 32, 13}, players[3].getPositions());
        assertEquals("Green", players[3].getName());
        AreoModel model2 = h.loadModel(3, new AreoBoardImpl.Builder(), new PlayerImpl.Builder());
        assertNull(model2);
        AreoModel model3 = h.loadModel(1, new AreoBoardImpl.Builder(), new PlayerImpl.Builder());
        Player[] players3 = model3.getPlayers();
        assertArrayEquals(new int[]{20, 20, 30, 40}, players3[0].getPositions());
        assertEquals("Red", players3[0].getName());
        assertArrayEquals(new int[]{50, 10, 50, 10}, players3[1].getPositions());
        assertEquals("Yellow", players3[1].getName());
        assertArrayEquals(new int[]{1, 4, 5, 5}, players3[2].getPositions());
        assertEquals("Blue", players3[2].getName());
        assertArrayEquals(new int[]{6, 6, 4, 4}, players3[3].getPositions());
        assertEquals("Green", players3[3].getName());
    }

//    @Test
//    public void testWriteToFile() {
//        AreoFileHandler h = new AreoFileHandler("constructor-test.txt", this.builder);
//        h.writeToFile();
//        assertEquals("10 10 14 10 10 20 30 10 15 -1 1 40 50 21 32 13\n" +
//                "20 20 30 40 50 10 50 10 1 4 5 5 6 6 4 4\n", this.builder.toString());
//    }

    @Test
    public void invalidFile() {
        // 1 2 23 4
        AreoFileHandler h = new AreoFileHandler("invalid-test.txt", this.builder);
        assertEquals("", this.builder.toString());
    }
}
