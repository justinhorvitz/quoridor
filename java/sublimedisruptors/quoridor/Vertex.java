package sublimedisruptors.quoridor;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

/**
 * The intersection of a distinct row and column on a quoridor board.
 *
 * <p>Rows are denoted by a lower case character beginning with {@code a} on player one's side of
 * the board and increasing toward player 2's side.
 *
 * <p>Columns are denoted by an integer beginning with {@code 1} for the leftmost column (from
 * player one's perspective) and increasing toward the right.
 *
 * <p>It is legal to construct a vertex instance with an out-of-bounds row or column (e.g.,
 * a column less than {@code a} or a row less than {@code 1}).
 */
@AutoValue
public abstract class Vertex {

  static Vertex at(char column, int row) {
    return new AutoValue_Vertex(column, row);
  }

  abstract char column();

  abstract int row();
}

