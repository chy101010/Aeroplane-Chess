import com.areoplane.game.Model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ModelBuilderTest {
    private AreoBoardImpl.Builder builder;
    private Player red;
    private Player yellow;
    private Player blue;
    private Player green;

    @Before
    public void testFixture() {
        this.red = new PlayerImpl("Red");
        this.yellow = new PlayerImpl("Yellow");
        this.blue = new PlayerImpl("Blue");
        this.green = new PlayerImpl("Green");
        this.builder = new AreoBoardImpl.Builder();
    }

    @Test
    public void testBuild() {
        AreoModel model = builder.build();
        assertEquals(this.red, model.getPlayers()[0]);
        assertEquals(this.yellow, model.getPlayers()[1]);
        assertEquals(this.blue, model.getPlayers()[2]);
        assertEquals(this.green, model.getPlayers()[3]);
    }

    @Test
    public void testAdd() {
        Player red = new PlayerImpl("Red", 0, -1, 4, 50);
        Player blue = new PlayerImpl("Yellow", 5, 10, 14, 50);
        Player yellow = new PlayerImpl("Blue", -1, 40, 20, 14);
        Player green = new PlayerImpl("Green", 21, 24, 56, 14);
        Player pink = new PlayerImpl("Pink", 25, 30, 49, 14);
        this.builder.addPlayer(red);
        this.builder.addPlayer(blue);
        this.builder.addPlayer(yellow);
        this.builder.addPlayer(green);
        this.builder.addPlayer(pink);
        AreoModel model = this.builder.build();
        assertEquals(red, model.getPlayers()[0]);
        assertEquals(blue, model.getPlayers()[1]);
        assertEquals(yellow, model.getPlayers()[2]);
        assertEquals(green, model.getPlayers()[3]);
    }
}
