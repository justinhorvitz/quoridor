package sublimedisruptors.quoridor;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Tests for {@link Square}. */
@RunWith(JUnit4.class)
public final class SquareTest {

  @Test
  public void borderingEdge_up() {
    Square square = Square.create(Vertex.at('c', 3));
    Edge edgeAbove = square.borderingEdge(Direction.UP);
    assertThat(edgeAbove).isEqualTo(Edge.horizontal(Vertex.at('b', 2)));
  }

  @Test
  public void borderingEdge_down() {
    Square square = Square.create(Vertex.at('c', 3));
    Edge edgeBelow = square.borderingEdge(Direction.DOWN);
    assertThat(edgeBelow).isEqualTo(Edge.horizontal(Vertex.at('c', 3)));
  }

  @Test
  public void borderingEdge_left() {
    Square square = Square.create(Vertex.at('c', 3));
    Edge edgeLeft = square.borderingEdge(Direction.LEFT);
    assertThat(edgeLeft).isEqualTo(Edge.vertical(Vertex.at('b', 2)));
  }

  @Test
  public void borderingEdge_right() {
    Square square = Square.create(Vertex.at('c', 3));
    Edge edgeRight = square.borderingEdge(Direction.RIGHT);
    assertThat(edgeRight).isEqualTo(Edge.vertical(Vertex.at('c', 3)));
  }
}

