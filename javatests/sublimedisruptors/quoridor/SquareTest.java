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
    Square square = Square.at('c', 3);
    Groove grooveAbove = square.borderingGroove(Direction.UP);
    assertThat(grooveAbove).isEqualTo(Groove.horizontal(Vertex.at('c', 2)));
  }

  @Test
  public void borderingGroove_down() {
    Square square = Square.at('c', 3);
    Groove grooveBelow = square.borderingGroove(Direction.DOWN);
    assertThat(grooveBelow).isEqualTo(Groove.horizontal(Vertex.at('c', 3)));
  }

  @Test
  public void borderingGroove_left() {
    Square square = Square.at('c', 3);
    Groove grooveLeft = square.borderingGroove(Direction.LEFT);
    assertThat(grooveLeft).isEqualTo(Groove.vertical(Vertex.at('b', 3)));
  }

  @Test
  public void borderingGroove_right() {
    Square square = Square.at('c', 3);
    Groove grooveRight = square.borderingGroove(Direction.RIGHT);
    assertThat(grooveRight).isEqualTo(Groove.vertical(Vertex.at('c', 3)));
  }

  @Test
  public void borderingGroove_null_throws() {
    Square square = Square.at('c', 3);
    assertThrows(RuntimeException.class, () -> square.borderingGroove(null));
  }

  @Test
  public void adjacentSquare_up() {
    Square square = Square.at('c', 3);
    Square squareAbove = square.adjacentSquare(Direction.UP);
    assertThat(squareAbove).isEqualTo(Square.at('c', 2));
  }

  @Test
  public void adjacentSquare_down() {
    Square square = Square.at('c', 3);
    Square squareBelow = square.adjacentSquare(Direction.DOWN);
    assertThat(squareBelow).isEqualTo(Square.at('c', 4));
  }

  @Test
  public void adjacentSquare_left() {
    Square square = Square.at('c', 3);
    Square squareLeft = square.adjacentSquare(Direction.LEFT);
    assertThat(squareLeft).isEqualTo(Square.at('b', 3));
  }

  @Test
  public void adjacentSquare_right() {
    Square square = Square.at('c', 3);
    Square squareRight = square.adjacentSquare(Direction.RIGHT);
    assertThat(squareRight).isEqualTo(Square.at('d', 3));
  }

  @Test
  public void adjacentSquare_null_throws() {
    Square square = Square.at('c', 3);
    assertThrows(RuntimeException.class, () -> square.adjacentSquare(null));
  }
}
