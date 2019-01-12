package sublimedisruptors.quoridor.board;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.ImmutableList.toImmutableList;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import sublimedisruptors.quoridor.board.Groove.Orientation;

/**
 * A quoridor wall.
 *
 * <p>A {@code Wall} is simply one or more {@linkplain Groove grooves}. In a classic quoridor game,
 * each wall covers two grooves. In this case, {@link #coveredGrooves} returns a list of size two
 * and {@link #coveredVertices} returns a list of size 1.
 *
 * <p>Walls have two unique invariants that may be used to determine whether any two wall instances
 * can be placed on the same board:
 *
 * <ol>
 *   <li>No two walls on the board may share a common {@linkplain #coveredGrooves covered groove}.
 *   <li>No two walls on the board may share a common {@linkplain #coveredVertices covered vertex}.
 * </ol>
 *
 * A {@code Wall} instance can be obtained via a fluent builder, for example:
 *
 * <pre>
 *   {@code Wall wall = Wall.vertical('c', 3).withLength(2);}
 * </pre>
 */
@AutoValue
public abstract class Wall {

  private static final Interner<Wall> interner = Interners.newStrongInterner();

  public static Builder vertical(char column, int row) {
    return builder(column, row, Orientation.VERTICAL);
  }

  public static Builder horizontal(char column, int row) {
    return builder(column, row, Orientation.HORIZONTAL);
  }

  private static Builder builder(char column, int row, Orientation orientation) {
    return new AutoValue_Wall.Builder()
        .setFirstVertex(Vertex.at(column, row))
        .setOrientation(orientation);
  }

  public final ImmutableList<Groove> coveredGrooves() {
    return vertexStream()
        .limit(length())
        .map(vertex -> Groove.groove(vertex.column(), vertex.row(), orientation()))
        .collect(toImmutableList());
  }

  public final ImmutableList<Vertex> coveredVertices() {
    return vertexStream().limit(length() - 1).collect(toImmutableList());
  }

  @AutoValue.Builder
  public abstract static class Builder {

    public final Wall withLength(int length) {
      checkArgument(length > 0, "Wall length must be positive, got %s", length);
      setLength(length);
      return interner.intern(build());
    }

    abstract Builder setFirstVertex(Vertex firstVertex);
    abstract Builder setOrientation(Orientation orientation);
    abstract Builder setLength(int length);
    abstract Wall build();
  }

  abstract Vertex firstVertex();

  abstract Orientation orientation();

  abstract int length();

  private Stream<Vertex> vertexStream() {
    return Stream.iterate(firstVertex(), direction());
  }

  private UnaryOperator<Vertex> direction() {
    return orientation() == Orientation.VERTICAL ? Direction.DOWN::apply : Direction.RIGHT::apply;
  }
}
