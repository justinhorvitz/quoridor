package sublimedisruptors.quoridor;

import java.util.function.IntUnaryOperator;
import java.util.function.UnaryOperator;

/**
 * The four ordinal directions.
 *
 * <p>A direction acts as a {@link UnaryOperator} on a {@link Vertex} to provide the next vertex in
 * a given direction.
 *
 * <p>Each direction is intended to be interpreted from the perspective of the diagram in the
 * Javadoc for {@link Vertex}, with player one at the bottom and player two at the top.
 */
public enum Direction implements UnaryOperator<Vertex> {
  UP(col -> col, row -> row - 1),
  DOWN(col -> col, row -> row + 1),
  LEFT(col -> col - 1, row -> row),
  RIGHT(col -> col + 1, row -> row);

  private final IntUnaryOperator columnFn;
  private final IntUnaryOperator rowFn;

  Direction(IntUnaryOperator columnFn, IntUnaryOperator rowFn) {
    this.columnFn = columnFn;
    this.rowFn = rowFn;
  }

  /**
   * Returns the next vertex in this direction in relation to the given {@code vertex}.
   *
   * <p>It is possible that the returned vertex is not within the bounds of a quoridor board.
   */
  @Override
  public Vertex apply(Vertex vertex) {
    char column = (char) columnFn.applyAsInt(vertex.column());
    int row = rowFn.applyAsInt(vertex.row());
    return Vertex.at(column, row);
  }
}
