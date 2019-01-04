package sublimedisruptors.quoridor;

import com.google.auto.value.AutoValue;

/**
 * An individual square on the board.
 *
 * <p>A {@code Square} is {@linkplain #borderingEdge bordered} by an {@link Edge} in each
 * {@link Direction}.
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

  private Vertex upLeftVertex() {
    return Direction.UP.andThen(Direction.LEFT).apply(vertex());
  }
}
