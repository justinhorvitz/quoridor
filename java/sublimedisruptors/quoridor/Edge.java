package sublimedisruptors.quoridor;

import com.google.auto.value.AutoValue;

/**
 * Represents the edge of a square on a Quoridor board.
 *
 * <p>Each edge on the board is uniquely defined by three primitives: 1) column, 2) row, and 3)
 * orientation (vertical or horizontal). The first two are encapsulated by {@link #vertex}, while
 * the orientation is given by {@link #orientation}.
 *
 * <p>In the canonical board representation, a {@linkplain Orientation#VERTICAL vertical} edge is
 * located immediately to the right of the square that shares the same vertex. A {@linkplain
 * Orientation#HORIZONTAL horizontal} edge is located immediately below the square that shares the
 * same vertex.
 */
@AutoValue
public abstract class Edge {

  public enum Orientation {
    VERTICAL, HORIZONTAL
  }

  public static Edge vertical(Vertex vertex) {
    return new AutoValue_Edge(vertex, Orientation.VERTICAL);
  }

  public static Edge horizontal(Vertex vertex) {
    return new AutoValue_Edge(vertex, Orientation.HORIZONTAL);
  }

  public abstract Vertex vertex();
  public abstract Orientation orientation();
}
