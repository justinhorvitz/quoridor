package sublimedisruptors.quoridor;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

import com.google.common.collect.ImmutableList;
import java.util.stream.Stream;
import sublimedisruptors.quoridor.Groove.Orientation;

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
 */
public final class Wall {

  private final Groove firstGroove;
  private final int length;

  Wall(Groove firstGroove, int length) {
    this.firstGroove = checkNotNull(firstGroove);
    this.length = length;
  }

  public ImmutableList<Groove> coveredGrooves() {
    return wallStream().limit(length).collect(toImmutableList());
  }

  public ImmutableList<Vertex> coveredVertices() {
    return wallStream()
        .limit(length - 1)
        .map(groove -> Vertex.at(groove.column(), groove.row()))
        .collect(toImmutableList());
  }

  private Stream<Groove> wallStream() {
    return Stream.iterate(firstGroove, Wall::nextGroove);
  }

  private static Groove nextGroove(Groove groove) {
    Direction direction =
        groove.orientation() == Orientation.VERTICAL ? Direction.DOWN : Direction.RIGHT;
    Vertex nextLocation = direction.apply(groove);
    return Groove.groove(nextLocation.column(), nextLocation.row(), groove.orientation());
  }
}
