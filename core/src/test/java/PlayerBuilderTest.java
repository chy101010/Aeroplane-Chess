import com.areoplane.game.Model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class PlayerBuilderTest {
    private PlayerImpl.Builder builder;

    @Before
    public void testFixture(){
        this.builder = new PlayerImpl.Builder();
    }

    @Test
    public void testBuild() {
        assertArrayEquals(new int[]{-1, -1, -1, -1}, this.builder.build().getPositions());
    }

    @Test
    public void testAdd() {
        this.builder.addPlanes(0);
        this.builder.addPlanes(10);
        this.builder.addPlanes(41);
        this.builder.addPlanes(21);
        assertArrayEquals(new int[]{0, 10, 41, 21}, this.builder.build().getPositions());
        this.builder.addPlanes(51);
        assertArrayEquals(new int[]{51, 10, 41, 21}, this.builder.build().getPositions());
    }
}
