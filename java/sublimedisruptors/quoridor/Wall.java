package sublimedisruptors.quoridor;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

import com.google.common.collect.ImmutableList;
import java.util.stream.Stream;
import sublimedisruptors.quoridor.Edge.Orientation;

/**
 * A quoridor wall.
 *
 * <p>A {@code Wall} is simply one or more {@linkplain Edge edges}. In a classic quoridor game,
 * each wall covers two edges. In this case, {@link #coveredEdges} returns a list of size two and
 * {@link #coveredVertices} returns a list of size 1.
 *
 * <p>Walls have two unique invariants that may be used to determine whether any two wall instances
 * can be placed on the same board:
 *
 * <ol>
 *   <li>No two walls on the board may share a common {@linkplain #coveredEdges covered edge}.
 *   <li>No two walls on the board may share a common {@linkplain #coveredVertices covered vertex}.
 * </ol>
 */
public final class Wall {

  private final Edge firstEdge;
  private final int length;

  Wall(Edge firstEdge, int length) {
    this.firstEdge = checkNotNull(firstEdge);
    this.length = length;
  }

  public ImmutableList<Edge> coveredEdges() {
    return wallStream().limit(length).collect(toImmutableList());
  }

  public ImmutableList<Vertex> coveredVertices() {
    return wallStream().limit(length - 1).map(Edge::vertex).collect(toImmutableList());
  }

  private Stream<Edge> wallStream() {
    return Stream.iterate(firstEdge, Wall::nextEdge);
  }

  private static Edge nextEdge(Edge edge) {
    if (edge.orientation() == Orientation.VERTICAL) {
      return Edge.vertical(Direction.DOWN.apply(edge.vertex()));
    } else {
      return Edge.horizontal(Direction.RIGHT.apply(edge.vertex()));
    }
  }
}

