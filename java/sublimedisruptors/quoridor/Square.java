package sublimedisruptors.quoridor;

import com.google.auto.value.AutoValue;

/**
 * An individual square on the board.
 *
 * <p>A {@code Square} is bordered by a {@link Groove} and another {@code Square} in each {@link
 * Direction}.
 */
@AutoValue
public abstract class Square implements Locatable {

  public static Square at(char column, int row) {
    return new AutoValue_Square(column, row);
  }

  /** Returns the {@link Groove} that borders this square in the given {@link Direction}. */
  public final Groove borderingGroove(Direction direction) {
    switch (direction) {
      case UP:
        return Groove.horizontal(Direction.UP.apply(this));
      case DOWN:
        return Groove.horizontal(this);
      case LEFT:
        return Groove.vertical(Direction.LEFT.apply(this));
      case RIGHT:
        return Groove.vertical(this);
    }
    throw new IllegalArgumentException("Unexpected direction: " + direction);
  }

  /** Returns the adjacent {@link Square} in the given {@link Direction}. */
  public final Square adjacentSquare(Direction direction) {
    Vertex adjacent = direction.apply(this);
    return Square.at(adjacent.column(), adjacent.row());
  }
}
