package sublimedisruptors.quoridor;

/**
 * A type that can be located on a Quoridor board via its {@link #column} and {@link #row}.
 *
 * <p>Columns are denoted by a lower case character beginning with {@code a} for the leftmost column
 * (from player one's perspective) and increasing toward the right.
 *
 * <p>Rows are denoted by an integer beginning with {@code 1} from player two's side and increasing
 * toward player one's side.
 *
 * <p>A typical 9x9 quoridor board is thus represented as follows:
 *
 * <pre>
 *                              Player 2
 *                 a   b   c   d   e   f   g   h   i
 *               -------------------------------------
 *             1 |   |   |   |   |   |   |   |   |   | 1
 *               -------------------------------------
 *             2 |   |   |   |   |   |   |   |   |   | 2
 *               -------------------------------------
 *             3 |   |   |   |   |   |   |   |   |   | 3
 *               -------------------------------------
 *             4 |   |   |   |   |   |   |   |   |   | 4
 *               -------------------------------------
 *             5 |   |   |   |   |   |   |   |   |   | 5
 *               -------------------------------------
 *             6 |   |   |   |   |   |   |   |   |   | 6
 *               -------------------------------------
 *             7 |   |   |   |   |   |   |   |   |   | 7
 *               -------------------------------------
 *             8 |   |   |   |   |   |   |   |   |   | 8
 *               -------------------------------------
 *             9 |   |   |   |   |   |   |   |   |   | 9
 *               -------------------------------------
 *                 a   b   c   d   e   f   g   h   i
 *                              Player 1
 * </pre>
 *
 * <p>It is legal to construct an instance with an out-of-bounds row or column (e.g., a column less
 * than {@code a} or a row less than {@code 1}).
 */
public interface Locatable {

  /** Returns this object's column on the board. */
  char column();

  /** Returns this object's row on the board. */
  int row();
}
