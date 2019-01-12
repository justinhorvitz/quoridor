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

  public static Groove vertical(char column, int row) {
    return groove(column, row, Orientation.VERTICAL);
  }

  public static Groove horizontal(char column, int row) {
    return groove(column, row, Orientation.HORIZONTAL);
  }

  static Groove groove(char column, int row, Orientation orientation) {
    return new AutoValue_Groove(column, row, orientation);
  }

  public abstract Orientation orientation();
}
