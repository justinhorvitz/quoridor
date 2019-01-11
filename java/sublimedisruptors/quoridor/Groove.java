package sublimedisruptors.quoridor;

import com.google.auto.value.AutoValue;

/**
 * Represents the groove between squares on a Quoridor board.
 *
 * <p>Each groove on the board is uniquely defined by three primitives: 1) column, 2) row, and 3)
 * orientation (vertical or horizontal). The first two are encapsulated by {@link #vertex}, while
 * the orientation is given by {@link #orientation}.
 *
 * <p>In the canonical board representation, a {@linkplain Orientation#VERTICAL vertical} groove is
 * located immediately to the right of the square that shares the same vertex. A {@linkplain
 * Orientation#HORIZONTAL horizontal} groove is located immediately below the square that shares the
 * same vertex.
 */
@AutoValue
public abstract class Groove {

  public enum Orientation {
    VERTICAL, HORIZONTAL
  }

  public static Groove vertical(Vertex vertex) {
    return new AutoValue_Groove(vertex, Orientation.VERTICAL);
  }

  public static Groove horizontal(Vertex vertex) {
    return new AutoValue_Groove(vertex, Orientation.HORIZONTAL);
  }

  public abstract Vertex vertex();
  public abstract Orientation orientation();
}
