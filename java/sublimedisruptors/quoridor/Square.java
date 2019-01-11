package sublimedisruptors.quoridor;

import com.google.auto.value.AutoValue;

/**
 * An individual square on the board.
 *
 * <p>A {@code Square} is bordered by a {@link Groove} and another {@code Square} in each {@link
 * Direction}.
 */
@AutoValue
public abstract class Square {

  public static Square create(Vertex vertex) {
    return new AutoValue_Square(vertex);
  }

  public abstract Vertex vertex();

  /** Returns the {@link Groove} that borders this square in the given {@link Direction}. */
  public final Groove borderingGroove(Direction direction) {
    switch (direction) {
      case UP:
        return Groove.horizontal(Direction.UP.apply(vertex()));
      case DOWN:
        return Groove.horizontal(vertex());
      case LEFT:
        return Groove.vertical(Direction.LEFT.apply(vertex()));
      case RIGHT:
        return Groove.vertical(vertex());
    }
    throw new IllegalArgumentException("Unexpected direction: " + direction);
  }

  /** Returns the bordering {@link Square} in the given {@link Direction}. */
  public final Square borderingSquare(Direction direction) {
    return Square.create(direction.apply(vertex()));
  }
}
