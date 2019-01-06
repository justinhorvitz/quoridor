package sublimedisruptors.quoridor;

import com.google.auto.value.AutoValue;

/**
 * An individual square on the board.
 *
 * <p>A {@code Square} is bordered by an {@link Edge} and another {@code Square} in each {@link
 * Direction}.
 */
@AutoValue
public abstract class Square {

  public static Square create(Vertex vertex) {
    return new AutoValue_Square(vertex);
  }

  public abstract Vertex vertex();

  /**
   * Returns the {@link Edge} that borders this square in the given {@link Direction}.
   */
  public final Edge borderingEdge(Direction direction) {
    switch (direction) {
      case UP:
        return Edge.horizontal(upLeftVertex());
      case DOWN:
        return Edge.horizontal(vertex());
      case LEFT:
        return Edge.vertical(upLeftVertex());
      case RIGHT:
        return Edge.vertical(vertex());
    }
    throw new IllegalArgumentException("Unexpected direction: " + direction);
  }

  /** Returns the bordering {@link Square} in the given {@link Direction}. */
  public final Square borderingSquare(Direction direction) {
    return Square.create(direction.apply(vertex()));
  }

  private Vertex upLeftVertex() {
    return Direction.UP.andThen(Direction.LEFT).apply(vertex());
  }
}
