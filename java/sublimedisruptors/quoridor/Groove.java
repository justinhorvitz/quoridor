package sublimedisruptors.quoridor;

import com.google.auto.value.AutoValue;

/**
 * Represents the groove between squares on a Quoridor board.
 *
 * <p>Each groove on the board is uniquely defined by three primitives: 1) column, 2) row, and 3)
 * orientation (vertical or horizontal).
 *
 * <p>In the canonical board representation, a {@linkplain Orientation#VERTICAL vertical} groove is
 * located immediately to the right of the square that shares the same vertex. A {@linkplain
 * Orientation#HORIZONTAL horizontal} groove is located immediately below the square that shares the
 * same vertex.
 */
@AutoValue
public abstract class Groove implements Locatable {

  public enum Orientation {
    VERTICAL, HORIZONTAL
  }

  public static Groove vertical(Locatable locatable) {
    return new AutoValue_Groove(locatable.column(), locatable.row(), Orientation.VERTICAL);
  }

  public static Groove horizontal(Locatable locatable) {
    return new AutoValue_Groove(locatable.column(), locatable.row(), Orientation.HORIZONTAL);
  }

  public abstract Orientation orientation();
}
