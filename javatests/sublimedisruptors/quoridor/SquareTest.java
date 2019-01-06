package sublimedisruptors.quoridor;

import static com.google.common.truth.Truth.assertThat;
import static sublimedisruptors.quoridor.testing.TestUtils.assertThrows;

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

  @Test
  public void borderingEdge_null_throws() {
    Square square = Square.create(Vertex.at('c', 3));
    assertThrows(RuntimeException.class, () -> square.borderingEdge(null));
  }

  @Test
  public void borderingSquare_up() {
    Square square = Square.create(Vertex.at('c', 3));
    Square squareAbove = square.borderingSquare(Direction.UP);
    assertThat(squareAbove).isEqualTo(Square.create(Vertex.at('c', 2)));
  }

  @Test
  public void borderingSquare_down() {
    Square square = Square.create(Vertex.at('c', 3));
    Square squareAbove = square.borderingSquare(Direction.DOWN);
    assertThat(squareAbove).isEqualTo(Square.create(Vertex.at('c', 4)));
  }

  @Test
  public void borderingSquare_left() {
    Square square = Square.create(Vertex.at('c', 3));
    Square squareAbove = square.borderingSquare(Direction.LEFT);
    assertThat(squareAbove).isEqualTo(Square.create(Vertex.at('b', 3)));
  }

  @Test
  public void borderingSquare_right() {
    Square square = Square.create(Vertex.at('c', 3));
    Square squareAbove = square.borderingSquare(Direction.RIGHT);
    assertThat(squareAbove).isEqualTo(Square.create(Vertex.at('d', 3)));
  }

  @Test
  public void borderingSquare_null_throws() {
    Square square = Square.create(Vertex.at('c', 3));
    assertThrows(RuntimeException.class, () -> square.borderingSquare(null));
  }
}
