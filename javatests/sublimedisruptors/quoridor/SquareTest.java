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
  public void borderingGroove_up() {
    Square square = Square.create(Vertex.at('c', 3));
    Groove grooveAbove = square.borderingGroove(Direction.UP);
    assertThat(grooveAbove).isEqualTo(Groove.horizontal(Vertex.at('c', 2)));
  }

  @Test
  public void borderingGroove_down() {
    Square square = Square.create(Vertex.at('c', 3));
    Groove grooveBelow = square.borderingGroove(Direction.DOWN);
    assertThat(grooveBelow).isEqualTo(Groove.horizontal(Vertex.at('c', 3)));
  }

  @Test
  public void borderingGroove_left() {
    Square square = Square.create(Vertex.at('c', 3));
    Groove grooveLeft = square.borderingGroove(Direction.LEFT);
    assertThat(grooveLeft).isEqualTo(Groove.vertical(Vertex.at('b', 3)));
  }

  @Test
  public void borderingGroove_right() {
    Square square = Square.create(Vertex.at('c', 3));
    Groove grooveRight = square.borderingGroove(Direction.RIGHT);
    assertThat(grooveRight).isEqualTo(Groove.vertical(Vertex.at('c', 3)));
  }

  @Test
  public void borderingGroove_null_throws() {
    Square square = Square.create(Vertex.at('c', 3));
    assertThrows(RuntimeException.class, () -> square.borderingGroove(null));
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
    Square squareBelow = square.borderingSquare(Direction.DOWN);
    assertThat(squareBelow).isEqualTo(Square.create(Vertex.at('c', 4)));
  }

  @Test
  public void borderingSquare_left() {
    Square square = Square.create(Vertex.at('c', 3));
    Square squareLeft = square.borderingSquare(Direction.LEFT);
    assertThat(squareLeft).isEqualTo(Square.create(Vertex.at('b', 3)));
  }

  @Test
  public void borderingSquare_right() {
    Square square = Square.create(Vertex.at('c', 3));
    Square squareRight = square.borderingSquare(Direction.RIGHT);
    assertThat(squareRight).isEqualTo(Square.create(Vertex.at('d', 3)));
  }

  @Test
  public void borderingSquare_null_throws() {
    Square square = Square.create(Vertex.at('c', 3));
    assertThrows(RuntimeException.class, () -> square.borderingSquare(null));
  }
}
