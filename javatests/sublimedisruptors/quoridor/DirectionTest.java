package sublimedisruptors.quoridor;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Tests for {@link Direction}. */
@RunWith(JUnit4.class)
public final class DirectionTest {

  @Test
  public void up_decrementsRow() {
    Vertex vertex = Vertex.at('c', 3);
    Vertex up = Direction.UP.apply(vertex);
    assertThat(up).isEqualTo(Vertex.at('c', 2));
  }

  @Test
  public void down_incrementsRow() {
    Vertex vertex = Vertex.at('c', 3);
    Vertex down = Direction.DOWN.apply(vertex);
    assertThat(down).isEqualTo(Vertex.at('c', 4));
  }

  @Test
  public void left_decrementsColumn() {
    Vertex vertex = Vertex.at('c', 3);
    Vertex left = Direction.LEFT.apply(vertex);
    assertThat(left).isEqualTo(Vertex.at('b', 3));
  }

  @Test
  public void right_incrementsColumn() {
    Vertex vertex = Vertex.at('c', 3);
    Vertex right = Direction.RIGHT.apply(vertex);
    assertThat(right).isEqualTo(Vertex.at('d', 3));
  }
}

